package sylvartore;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.*;

public class ThreadAI extends AI {

    private int depth;

    public ThreadAI(byte side, String name) {
        super(side, name);
    }

    byte[] best_cur;
    byte[] bestMove_cur;
    int max_cur = Integer.MIN_VALUE;

    byte[] getBestMove(int turnLeft, int aiTime, byte[] state) {
        int max = Integer.MIN_VALUE, depth = 2, actual = 0, actual_node = 0;
        nodeCount = 0;
        byte[] best = null, bestMove = null;
        long limit = System.currentTimeMillis() + aiTime, left = aiTime, last = 0;
        out:
        do {
            if (depth > turnLeft && depth != 2) break;
            actual = depth;
            long start = System.currentTimeMillis();
            best_cur = null;
            bestMove_cur = null;
            max_cur = Integer.MIN_VALUE;
            ExecutorService executor = Executors.newFixedThreadPool(4);
            List<byte[]> moves = getAllPossibleMoves(side, state);
            for (byte[] move : moves) {
                if (System.currentTimeMillis() > limit - 20) {
                    System.out.println("EXIT            IN          ADVANCE!");
                    break out;
                }
                executor.execute(new Job(state, move, depth, turnLeft));
            }
            try {
                executor.shutdown();
                executor.awaitTermination(1, TimeUnit.DAYS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            actual_node = nodeCount;
            last = System.currentTimeMillis() - start;
            left -= last;
            depth++;
            max = max_cur;
            best = best_cur;
            bestMove = bestMove_cur;
        } while ((left / 7) > last);
        byte[] uiMove = Board.toUiMove(bestMove[0], bestMove[1], bestMove[2], state);
        log(uiMove, actual, max, actual_node);
        return best;
    }

    class Job implements Runnable {
        byte[] state;
        byte[] move;
        int depth;
        int turnLeft;

        public Job(byte[] state, byte[] move, int depth, int turnLeft) {
            this.state = state;
            this.move = move;
            this.depth = depth;
            this.turnLeft = turnLeft;
        }

        @Override
        public void run() {
            byte[] copy = new byte[state.length];
            System.arraycopy(state, 0, copy, 0, state.length);
            Board.move(move[0], move[1], move[2], copy);
            int utility = min(copy, Integer.MIN_VALUE, Integer.MAX_VALUE, depth - 1, turnLeft - 1);
            if (best_cur == null || utility > max_cur) {
                max_cur = utility;
                best_cur = copy;
                bestMove_cur = move;
            }
        }
    }

    int min(byte[] state, int alpha, int beta, int depth, int turn) {
        if (depth == 0 || turn == 0) {
            return heuristic(state);
        }
        int value = Integer.MAX_VALUE;
        for (byte[] move : getAllPossibleMoves(counterSide, state)) {
            byte[] copy = new byte[state.length];
            System.arraycopy(state, 0, copy, 0, state.length);
            Board.move(move[0], move[1], move[2], copy);
            int utility = max(copy, alpha, beta, depth - 1, turn - 1);
            if (utility < value) value = utility;
            if (utility <= alpha) return utility;
            if (utility < beta) beta = utility;
        }
        return value;
    }

    int max(byte[] state, int alpha, int beta, int depth, int turn) {
        if (depth == 0 || turn == 0) {
            return heuristic(state);
        }
        int value = Integer.MIN_VALUE;
        for (byte[] move : getAllPossibleMoves(side, state)) {
            byte[] copy = new byte[state.length];
            System.arraycopy(state, 0, copy, 0, state.length);
            Board.move(move[0], move[1], move[2], copy);
            int utility = min(copy, alpha, beta, depth - 1, turn - 1);
            if (utility > value) value = utility;
            if (utility >= beta) return utility;
            if (utility > alpha) alpha = utility;
        }
        return value;
    }

    int heuristic(byte[] state) {
        nodeCount++;
        int a = 0, e = 0, heuristic_value = 0;
        for (int i = 0; i < state.length; i++) {
            if (state[i] == 0) continue;
            if (state[i] == side) {
                a += 1;
                heuristic_value += central_weight[i];
            } else {
                e += 1;
                heuristic_value -= central_weight[i];
            }
        }
        if (a == 8) return Integer.MIN_VALUE;
        if (e == 8) return Integer.MAX_VALUE;
        if (e == 9) heuristic_value += 500;
        if (a == 9) heuristic_value -= 500;
        return (a - e) * 50 + heuristic_value;
    }

}
