package sylvartore;

import java.util.*;
import java.util.concurrent.*;

public class AI {

    byte side;
    byte counterSide;
    String name;
    byte[] bestMove_cur;
    int max_cur;
    byte[] state;

    public AI(byte side) {
        this(side, "Main");
    }

    public AI(byte side, String name) {
        this.side = side;
        this.counterSide = (byte) (-1 * side);
        this.name = name;
    }

    public void changeTo(byte side) {
        this.side = side;
        this.counterSide = (byte) (-1 * side);
    }

    static List<byte[]> getAllOffensiveMoves(byte side, byte[] state) {
        List<byte[]> res = new ArrayList<>();
        for (byte cell = 0; cell < state.length; cell++) {
            if (state[cell] != side) continue;
            for (byte dir = 0; dir < 6; dir++) {
                for (byte n = 1; n <= 3; n++) {
                    byte order = Game.isValidMove(cell, dir, n, state);
                    if (order >= 0 && order <= 3) {
                        res.add(new byte[]{cell, dir, n, order});
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
                    byte order = Game.isValidMove(cell, dir, n, state);
                    if (order != -1) {
                        res.add(new byte[]{cell, dir, n, order});
                    }
                }
            }
        }
        res.sort(Comparator.comparingInt(move -> move[3]));
        return res;
    }

    byte[] getBestMove(int turnLeft, int aiTime, byte[] state) {
        int max = Integer.MIN_VALUE, actual_depth = 0;
        int depth = 2;
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
                executor.awaitTermination(1, TimeUnit.DAYS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            actual_depth = depth;
            last = System.currentTimeMillis() - start;
            left -= last;
            depth++;
            max = max_cur;
            bestMove = bestMove_cur;
        } while (left > last * 6);
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
            return heuristic3(state);
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
            return heuristic3(state);
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

    int check(byte cell, byte[] state) {
        int val = 0, a = 0;
        for (byte dir = 0; dir < 6; dir++) {
            byte next = Game.TransitionMatrix[cell][dir];
            if (next == -1) continue;
            if (state[next] == state[cell]) {
                ++val;
                ++a;
            }
            for (byte n = 1; n <= 3; n++) {
                byte order = Game.isValidMove(cell, dir, n, state);
                if (order >= 0 && order <= 3) {
                    val += (4 - order) * 2;
                }
            }
        }
        if (a == 6) val += 2;
        return val;
    }

    int heuristic3(byte[] state) {
        int a = 0, e = 0, heuristic_value = 0;
        for (byte i = 0; i < state.length; i++) {
            if (state[i] == 0) continue;
            if (state[i] == side) {
                a += 1;
                heuristic_value += 100 + central_weight[i] * 3 + check(i, state) * 2;
            } else {
                e += 1;
                heuristic_value -= 100 + central_weight[i] * 3 + check(i, state) * 2;
            }
        }
        if (a <= 8) return -999999;
        if (e <= 8) return 999999;
        if (e <= 9 && a > 9) heuristic_value += 1000;
        return (a - e) * 100 + heuristic_value;
    }

    int group_check(int cell, byte[] state) {
        byte adjacent_cell = Game.TransitionMatrix[cell][0];
        if (adjacent_cell == -1) return 0;
        byte side = state[adjacent_cell];
        if (side == 0 || side != state[cell]) return 0;
        for (int i = 1; i < 6; i++) {
            adjacent_cell = Game.TransitionMatrix[cell][i];
            if (adjacent_cell == -1 || state[adjacent_cell] != side) return 0;
        }
        return 4;
    }

    int heuristic(byte[] state) {
        int a = 0, e = 0, heuristic_value = 0;
        for (int i = 0; i < state.length; i++) {
            if (state[i] == 0) continue;
            if (state[i] == side) {
                a += 1;
                heuristic_value += central_weight[i] + group_check(i, state);
            } else {
                e += 1;
                heuristic_value -= central_weight[i] + group_check(i, state);
            }
        }
        if (a <= 8) return -999999;
        if (e <= 8) return 999999;
        if (e <= 9 && a > 9) heuristic_value += 1000;
        return (a - e) * 100 + heuristic_value;
    }


    int heuristic2(byte[] state) {
        int a = 0, e = 0, heuristic_value = 0;
        for (int i = 0; i < state.length; i++) {
            if (state[i] == 0) continue;
            if (state[i] == side) {
                a += 1;
                heuristic_value += central_weight[i] + group_check(i, state) * 3 + adjacency_check(i, state) * 3;
            } else {
                e += 1;
                heuristic_value -= central_weight[i] + adjacency_check(i, state);
            }
        }
        if (a <= 8) return -999999;
        if (e <= 8) return 999999;
        if (e <= 9 && a > 9) heuristic_value += 1000;
        return (a - e) * 200 + heuristic_value;
    }

    static int adjacency_check(int cell, byte[] state) {
        int score = 0;
        for (int i = 3; i < 6; i++) {
            byte adjacent_cell = Game.TransitionMatrix[cell][i];
            if (adjacent_cell != -1 && state[adjacent_cell] == state[cell]) {
                score += 1;
            }
        }
        return score;
    }


//    int ad(int cell, byte[] state) {
//        int score = 0;
//        for (int i = 3; i < 6; i++) {
//            byte adjacent_cell = Game.TransitionMatrix[cell][i];
//            if (adjacent_cell != -1 && state[adjacent_cell] == state[cell]) {
//                score += 1;
//            }
//        }
//        return score;
//    }

    void log(byte[] move, int depth, int max, byte[] state) {
        System.out.println((side == 1 ? "WHITE " : "BLACK ") + name + " AI moved: "
                + Game.moveToString(move, state)
                + "     max depth: " + depth + ",    best node found: " + max);
    }

//    static byte[] central_weight = {
//            0, 0, 0, 0, 0,
//            0, 2, 2, 2, 2, 0,
//            0, 2, 3, 3, 3, 2, 0,
//            0, 2, 3, 4, 4, 3, 2, 0,
//            0, 2, 3, 5, 6, 5, 3, 2, 0,
//            0, 2, 3, 4, 4, 3, 2, 0,
//            0, 2, 3, 3, 3, 2, 0,
//            0, 2, 2, 2, 2, 0,
//            0, 0, 0, 0, 0,
//    };

    static byte[] central_weight = {
            0, 0, 0, 0, 0,
            0, 3, 3, 3, 3, 0,
            0, 3, 5, 5, 5, 3, 0,
            0, 3, 5, 7, 7, 5, 3, 0,
            0, 3, 5, 7, 9, 7, 5, 3, 0,
            0, 3, 5, 7, 7, 5, 3, 0,
            0, 3, 5, 5, 5, 3, 0,
            0, 3, 3, 3, 3, 0,
            0, 0, 0, 0, 0,
    };

    byte[][] stand = new byte[][]{
            {56, 2, 1, 4},
            {57, 2, 1, 4},
            {59, 1, 1, 4},
            {60, 1, 1, 4}
    };

    byte[][] bel = new byte[][]{
            {4, 5, 1, 4},
            {56, 2, 1, 4}
    };

    byte[][] ger = new byte[][]{
            {17, 5, 1, 5},
            {43, 2, 1, 5}
    };

    byte[] getFirstMove(byte[] state) {
        Random rand = new Random();
        if (Arrays.equals(state, Game.getStandardInitialLayout())) {
            int n = rand.nextInt(4);
            return stand[n];
        }
        if (Arrays.equals(state, Game.getBelgianDaisyLayout())) {
            int n = rand.nextInt(2);
            return bel[n];
        }
        int n = rand.nextInt(2);
        return ger[n];
    }

    int min2(byte[] state, int alpha, int beta, int depth, int turn) {
        if (depth == 0 || turn == 0) {
            return heuristic(state);
        }
        int value = Integer.MAX_VALUE;
        for (byte[] move : getAllPossibleMoves(counterSide, state)) {
            byte[] copy = new byte[state.length];
            System.arraycopy(state, 0, copy, 0, state.length);
            Game.move(move[0], move[1], move[2], copy);
            int utility = max2(copy, alpha, beta, depth - 1, turn - 1);
            if (utility < value) value = utility;
            if (utility <= alpha) return utility;
            if (utility < beta) beta = utility;
        }
        return value;
    }

    int max2(byte[] state, int alpha, int beta, int depth, int turn) {
        if (depth == 0 || turn == 0) {
            return heuristic(state);
        }
        int value = Integer.MIN_VALUE;
        for (byte[] move : getAllPossibleMoves(side, state)) {
            byte[] copy = new byte[state.length];
            System.arraycopy(state, 0, copy, 0, state.length);
            Game.move(move[0], move[1], move[2], copy);
            int utility = min2(copy, alpha, beta, depth - 1, turn - 1);
            if (utility > value) value = utility;
            if (utility >= beta) return utility;
            if (utility > alpha) alpha = utility;
        }
        return value;
    }
}
