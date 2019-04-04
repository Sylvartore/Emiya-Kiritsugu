package sylvartore;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Quiescent extends AI {
    public Quiescent(byte side, String name) {
        super(side, name);
    }

    byte[] getBestMove(int turnLeft, int aiTime, byte[] state) {
        int max = Integer.MIN_VALUE, depth = 2, actual = 1, actual_node = 0;
        this.state = state;
        byte[] bestMove = null;
        long limit = System.currentTimeMillis() + aiTime, left = aiTime, last;
        out:
        do {
            if (depth > turnLeft && depth != 2 || (depth == 7 && aiTime <= 5000)) break;
            long start = System.currentTimeMillis();
            bestMove_cur = null;
            max_cur = Integer.MIN_VALUE;
            List<byte[]> moves = getAllPossibleMoves(side, state);
            ExecutorService executor = Executors.newFixedThreadPool(4);
            for (byte[] move : moves) {
                if (System.currentTimeMillis() > limit - 20) {
                    System.out.println("EXIT            IN          ADVANCE!");
                    break out;
                }
                executor.execute(new Job(move, depth, turnLeft));
            }
            try {
                executor.shutdown();
                executor.awaitTermination(1, TimeUnit.DAYS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            actual = depth;
            last = System.currentTimeMillis() - start;
            left -= last;
            depth++;
            max = max_cur;
            bestMove = bestMove_cur;
        } while ((left / 7) > last);
        log(bestMove, actual, max, state);
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
            int utility = min(copy, Integer.MIN_VALUE, Integer.MAX_VALUE, depth - 1, turnLeft - 1);
            if (bestMove_cur == null || utility > max_cur) {
                max_cur = utility;
                bestMove_cur = move;
            }
        }
    }

    int quiesce_min(int alpha, int beta, int turn, byte[] state) {
        int score = heuristic(state);
        if (turn == 0) return score;
        if (score <= alpha) return score;
        for (byte[] move : getAllOffensiveMoves(counterSide, state)) {
            byte[] copy = new byte[state.length];
            System.arraycopy(state, 0, copy, 0, state.length);
            Game.move(move[0], move[1], move[2], copy);
            score = quiesce_max(alpha, beta, turn - 1, copy);
            if (score <= beta) {
                beta = score;
                if (score <= alpha) break;
            }
        }
        return score;
    }

    int min(byte[] state, int alpha, int beta, int depth, int turn) {
        if (depth == 0 || turn == 0) {
            if (turn != 0) return quiesce_min(alpha, beta, turn, state);
            return heuristic(state);
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

    int quiesce_max(int alpha, int beta, int turn, byte[] state) {
        int score = heuristic(state);
        if (turn == 0) return score;
        if (score >= beta) return score;
        for (byte[] move : getAllOffensiveMoves(side, state)) {
            byte[] copy = new byte[state.length];
            System.arraycopy(state, 0, copy, 0, state.length);
            Game.move(move[0], move[1], move[2], copy);
            score = quiesce_min(alpha, beta, turn - 1, copy);
            if (score >= alpha) {
                alpha = score;
                if (score >= beta) break;
            }
        }
        return score;
    }

    int max(byte[] state, int alpha, int beta, int depth, int turn) {
        if (depth == 0 || turn == 0) {
            if (turn != 0) return quiesce_max(alpha, beta, turn, state);
            return heuristic(state);
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

    int group_check(int cell, byte[] state) {
        int score = 0;
        for (int i = 3; i < 6; i++) {
            byte adjacent_cell = Game.TransitionMatrix[cell][i];
            if (adjacent_cell != -1 && state[adjacent_cell] == state[cell]) {
                score += 1;
            }
        }
        return score;
    }

    int heuristic(byte[] state) {
        int a = 0, e = 0, heuristic_value = 0;
        for (int i = 0; i < state.length; i++) {
            if (state[i] == 0) continue;
            if (state[i] == side) {
                a += 1;
                heuristic_value += central_weight[i]; //+ group_check(i, state) / 3;
            } else {
                e += 1;
                heuristic_value -= central_weight[i];//+ group_check(i, state) / 3;
            }
        }
        if (a == 8) return Integer.MIN_VALUE;
        if (e == 8) return Integer.MAX_VALUE;
        if (e == 9) heuristic_value += 500;
        if (a == 9) heuristic_value -= 500;
        return (a - e) * 100 + heuristic_value;
    }
}
