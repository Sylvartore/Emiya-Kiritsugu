//
// Created by Sylvartore on 3/12/2019.
//
package sylvartore;

import java.util.*;
import java.util.concurrent.*;

public class AI {
    long count;
    byte side;
    byte counterSide;
    String name;
    byte[] bestMove_cur;
    int max_cur;
    byte[] state;
    int depth;

    public AI(byte side) {
        this(side, "Emiya Kiritsugu");
    }

    public AI(byte side, String name) {
        this.side = side;
        this.counterSide = side == 1 ? (byte) -1 : 1;
        this.name = name;
    }

    public void changeTo(byte side) {
        this.side = side;
        this.counterSide = side == 1 ? (byte) -1 : 1;
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
        this.state = state;
        depth = 3;
        byte[] bestMove = null;
        long left = aiTime, last, limit = System.currentTimeMillis() + aiTime;
        do {
            if (depth > turnLeft && depth != 3) break;
            long start = System.currentTimeMillis();
            bestMove_cur = null;
            max_cur = Integer.MIN_VALUE;
            ExecutorService executor = Executors.newFixedThreadPool(4);
            List<byte[]> moves = getAllPossibleMoves(side, state);
            for (byte[] move : moves) {
                executor.execute(() -> {
                    byte[] copy = new byte[state.length];
                    System.arraycopy(state, 0, copy, 0, state.length);
                    Game.move(move[0], move[1], move[2], copy);
                    int utility = min(copy, Integer.MIN_VALUE, Integer.MAX_VALUE, depth, turnLeft);
                    synchronized (this) {
                        if (utility > max_cur) {
                            max_cur = utility;
                            bestMove_cur = move;
                        }
                    }
                });
            }
            try {
                executor.shutdown();
                if (!executor.awaitTermination(limit - System.currentTimeMillis(), TimeUnit.MILLISECONDS)) {
                    break;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            last = System.currentTimeMillis() - start;
            left -= last;
            depth++;
            bestMove = bestMove_cur;
        } while (left > last * 8);
        return bestMove;
    }

    int min(byte[] state, int alpha, int beta, int depth, int turn) {
        if (depth == 0 || turn == 0) {
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

    int max(byte[] state, int alpha, int beta, int depth, int turn) {
        if (depth == 0 || turn == 0) {
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

    int check(int i, byte[] state) {
        int h = central_weight[i];
        for (byte dir = 0; dir < 3; dir++) {
            byte next = Game.TransitionMatrix[i][dir];
            byte counter = Game.TransitionMatrix[i][Game.CounterDirection[dir]];
            if (next == -1 && counter == -1) continue;
            h += next != -1 && state[next] == state[i] ? 1 : 0;
            h += counter != -1 && state[counter] == state[i] ? 1 : 0;
            h += next != -1 && counter != -1 && state[next] == state[counter] && state[next] != 0 ? 1 : 0;
        }
        return h;
    }

    int heuristic(byte[] state) {
        int a = 0, e = 0, ah = 0, eh = 0;
        for (byte i = 0; i < state.length; i++) {
            if (state[i] == 0) continue;
            if (state[i] == side) {
                a++;
                ah += 100 + check(i, state);
            } else {
                e++;
                eh += 100 + check(i, state);
            }
        }
        if (a <= 8) return -1000000;
        if (e <= 8) return 1000000;
        if (e <= 9 && a > 9) ah += 1000;
        ah *= 1.1;
        return ah - eh;
    }

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

    public static final int[] central_weight = new int[]{
            5, 5, 5, 5, 5,
            5, 14, 14, 14, 14, 5,
            5, 14, 21, 21, 21, 14, 5,
            5, 14, 21, 28, 28, 21, 14, 5,
            5, 14, 21, 28, 35, 28, 21, 14, 5,
            5, 14, 21, 28, 28, 21, 14, 5,
            5, 14, 21, 21, 21, 14, 5,
            5, 14, 14, 14, 14, 5,
            5, 5, 5, 5, 5,
    };
}
