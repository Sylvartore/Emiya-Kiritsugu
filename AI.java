package sylvartore;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AI {

    byte side;
    byte counterSide;
    int nodeCount;
    String name;

    public AI(byte side, String name) {
        this.side = side;
        this.counterSide = (byte) (-1 * side);
        nodeCount = 0;
        this.name = name;
    }

    static List<byte[]> getAllOffensiveMoves(byte side, byte[] state) {
        List<byte[]> res = new ArrayList<>();
        for (byte cell = 0; cell < state.length; cell++) {
            if (state[cell] != side) continue;
            for (byte dir = 0; dir < 6; dir++) {
                for (byte n = 1; n <= 3; n++) {
                    int order = Board.isValidMove(cell, dir, n, state);
                    if (order >= 0 && order <= 2) {
                        res.add(new byte[]{cell, dir, n, (byte) order});
                    }
                }
            }
        }
        res.sort(Comparator.comparingInt(move -> move[3]));
        return res;
    }

    static List<byte[]> getAllPossibleMoves(byte side, byte[] state) {
        List<byte[]> res = new ArrayList<>();
        for (byte cell = 0; cell < state.length; cell++) {
            if (state[cell] != side) continue;
            for (byte dir = 0; dir < 6; dir++) {
                for (byte n = 1; n <= 3; n++) {
                    int order = Board.isValidMove(cell, dir, n, state);
                    if (order != -1) {
                        res.add(new byte[]{cell, dir, n, (byte) order});
                    }
                }
            }
        }
        res.sort(Comparator.comparingInt(move -> move[3]));
        return res;
    }

    byte[] getBestMove(int turnLeft, int aiTime, byte[] state) {
        int max = Integer.MIN_VALUE, depth = 1, actual = 0, actual_node = 0;
        nodeCount = 0;
        byte[] best = null, bestMove = null;
        long limit = System.currentTimeMillis() + aiTime, left = aiTime, last = 0;
        out:
        do {
            if (depth > turnLeft && depth != 1) break;
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
        } while ((left / 9) > last);
        byte[] uiMove = Board.toUiMove(bestMove[0], bestMove[1], bestMove[2], state);
        log(uiMove, actual, max, actual_node);
        return best;
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
                heuristic_value += central_weight[i] + adjacency_check(i, state) / 3;
            } else {
                e += 1;
                heuristic_value -= central_weight[i] + adjacency_check(i, state) / 3;//2
            }
        }
        if (a == 8) return Integer.MIN_VALUE;
        if (e == 8 ||(e == 9 && a > 9)) return Integer.MAX_VALUE;
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

    void log(byte[] uiMove, int depth, int max, int actual_nodes) {
        StringBuilder sb = new StringBuilder();
        sb.append(Board.ToStandardNotation[uiMove[0]]);
        sb.append(" ");
        if (uiMove.length == 2) {
            sb.append(Board.directionToString[uiMove[1]]);
        } else {
            sb.append(Board.ToStandardNotation[uiMove[1]]);
            sb.append(" ");
            sb.append(Board.directionToString[uiMove[2]]);
        }
        System.out.println(name + " AI moved: " + sb.toString());
        System.out.println("    max depth: " + depth + ",    " +
                "node searched: " + actual_nodes + ",    best node found: " + max);
    }

    static byte[] central_weight = {
            1, 1, 1, 1, 1,
            1, 2, 2, 2, 2, 1,
            1, 2, 3, 3, 3, 2, 1,
            1, 2, 3, 4, 4, 3, 2, 1,
            1, 2, 3, 4, 5, 4, 3, 2, 1,
            1, 2, 3, 4, 4, 3, 2, 1,
            1, 2, 3, 3, 3, 2, 1,
            1, 2, 2, 2, 2, 1,
            1, 1, 1, 1, 1,
    };
}
