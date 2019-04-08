package sylvartore;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Q2 extends AI {

    public Q2(byte side) {
        super(side, "Q2");
    }

    byte[] getBestMove(int turnLeft, int aiTime, byte[] state) {
        int depth = 2;
        int max = Integer.MIN_VALUE, actual_depth = 0;
        this.state = state;
        byte[] bestMove = null;
        long left = aiTime, last, limit = System.currentTimeMillis() + aiTime;
        do {
            if (depth > turnLeft && depth != 2 || (depth == 7 && aiTime <= 5000)) break;
            long start = System.currentTimeMillis();
            bestMove_cur = null;
            max_cur = Integer.MIN_VALUE;
            ExecutorService executor = Executors.newFixedThreadPool(4);
            List<byte[]> moves = getAllPossibleMoves(side, state);
            for (byte[] move : moves) {
                executor.execute(new Job(move, depth - 1, turnLeft - 1));
            }
            try {
                executor.shutdown();
                if (!executor.awaitTermination(limit - System.currentTimeMillis(), TimeUnit.MILLISECONDS)) {
                    System.out.println("EXIT        IN          ADVANCE");
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            actual_depth = depth;
            last = System.currentTimeMillis() - start;
            left -= last;
            depth++;
            max = max_cur;
            bestMove = bestMove_cur;
        } while (left > last * 5);
        log(bestMove, actual_depth, max, state);
        return bestMove;
    }

    class Job implements Runnable {
        byte[] move;
        int depth;
        int turnLeft;

        public Job(byte[] move, int depth, int turnLeft) {
            this.move = move;
            this.depth = depth;
            this.turnLeft = turnLeft;
        }

        @Override
        public void run() {
            byte[] copy = new byte[state.length];
            System.arraycopy(state, 0, copy, 0, state.length);
            Game.move(move[0], move[1], move[2], copy);
            int utility = min(copy, Integer.MIN_VALUE, Integer.MAX_VALUE, depth, turnLeft);
            synchronized (this) {
                if (bestMove_cur == null || utility > max_cur) {
                    max_cur = utility;
                    bestMove_cur = move;
                }
            }
        }
    }

    int min(byte[] state, int alpha, int beta, int depth, int turn) {
        if (depth == 0 || turn == 0) {
            int val = heuristic(state);
            if (turn == 0 || val<=alpha) return val;
            if (val < beta) return min2(state, alpha, beta, 3, turn);
            return val;
        }
        int value = Integer.MAX_VALUE;
        for (byte[] move : getAllPossibleMoves(counterSide, state)) {
            byte[] copy = new byte[state.length];
            System.arraycopy(state, 0, copy, 0, state.length);
            Game.move(move[0], move[1], move[2], copy);
            int utility = max(copy, alpha, beta, depth - 1, turn - 1);
            if (utility < value) value = utility;
            if (utility <= alpha) return utility;
            if (utility < beta) beta = utility;
        }
        return value;
    }

    int max(byte[] state, int alpha, int beta, int depth, int turn) {
        if (depth == 0 || turn == 0) {
            int val = heuristic(state);
            if (turn == 0 || val>=beta) return val;
            if (val > alpha) return max2(state, alpha, beta, 3, turn);
            return val;
        }
        int value = Integer.MIN_VALUE;
        for (byte[] move : getAllPossibleMoves(side, state)) {
            byte[] copy = new byte[state.length];
            System.arraycopy(state, 0, copy, 0, state.length);
            Game.move(move[0], move[1], move[2], copy);
            int utility = min(copy, alpha, beta, depth - 1, turn - 1);
            if (utility > value) value = utility;
            if (utility >= beta) return utility;
            if (utility > alpha) alpha = utility;
        }
        return value;
    }

}

