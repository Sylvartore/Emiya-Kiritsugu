package sylvartore;

import java.util.List;

public class CounterAI extends AI {
    public CounterAI(byte side, String name) {
        super(side, name);
    }

    byte[] getBestMove(int turnLeft, int aiTime, byte[] state) {
        int max = Integer.MIN_VALUE, depth = 2, actual = 1, actual_node = 0;
        nodeCount = 0;
        byte[] best = null, bestMove = null;
        long limit = System.currentTimeMillis() + aiTime, left = aiTime, last;
        out:
        do {
            if (depth > turnLeft && depth != 2) break;
            actual++;
            long start = System.currentTimeMillis();
            byte[] best_cur = null, bestMove_cur = null;
            int max_cur = Integer.MIN_VALUE;
            List<byte[]> moves = getAllPossibleMoves(side, state);
            for (byte[] move : moves) {
                if (System.currentTimeMillis() > limit - 20) {
                    System.out.println("EXIT            IN          ADVANCE!");
                    break out;
                }
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
            actual_node = nodeCount;
            last = System.currentTimeMillis() - start;
            left -= last;
            depth++;
            max = max_cur;
            best = best_cur;
            bestMove = bestMove_cur;
        } while ((left / 13) > last);
        byte[] uiMove = Board.toUiMove(bestMove[0], bestMove[1], bestMove[2], state);
        log(uiMove, actual, max, actual_node);
        return best;
    }

    int quiesce_min(int alpha, int beta, int turn, byte[] state) {
        int score = heuristic(state);
        if (turn == 0) return score;
        if (score <= alpha) return score;
        for (byte[] move : getAllOffensiveMoves(counterSide, state)) {
            byte[] copy = new byte[state.length];
            System.arraycopy(state, 0, copy, 0, state.length);
            Board.move(move[0], move[1], move[2], copy);
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
            Board.move(move[0], move[1], move[2], copy);
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
            Board.move(move[0], move[1], move[2], copy);
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
                heuristic_value += central_weight[i] + adjacency_check(i, state) / 3;
            } else {
                e += 1;
                heuristic_value -= central_weight[i] + adjacency_check(i, state) / 3;
            }
        }
        if (a == 8) return Integer.MIN_VALUE;
        if (e == 8 || (e == 9 && a > 9)) return Integer.MAX_VALUE;
        return (a - e) * 50 + heuristic_value;
    }

    int adjacency_check(int cell, byte[] state) {
        int score = 0;
        for (int i = 3; i < 6; i++) {
            byte adjacent_cell = Board.TransitionMatrix[cell][i];
            if (adjacent_cell != -1 && state[adjacent_cell] == state[cell]) {
                score += 1;
            }
        }
        return score;
    }

}
