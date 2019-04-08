package sylvartore;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class T3 extends AI {

    HashMap<Long, int[]> map;
    HashMap<Long, int[]> map2;

    public T3(byte side) {
        super(side, "TranspositionTable AI");
        map = new HashMap<>();
        map2 = new HashMap<>();
    }

    byte[] getBestMove(int turnLeft, int aiTime, byte[] state) {
        int max = Integer.MIN_VALUE;
        int actual_depth = 0;
        this.state = state;
        byte[] bestMove = null;
        depth = 3;
        long left = aiTime, last, limit = System.currentTimeMillis() + aiTime;
        do {
            if (depth > turnLeft && depth != 3 || (depth == 7 && aiTime <= 5000)) break;
            long start = System.currentTimeMillis();
            bestMove_cur = null;
            max_cur = Integer.MIN_VALUE;

            ExecutorService executor = Executors.newFixedThreadPool(4);
            for (byte[] move : getAllPossibleMoves(side, state)) {
                executor.execute(() -> {
                    byte[] copy = new byte[state.length];
                    System.arraycopy(state, 0, copy, 0, state.length);
                    Game.move(move[0], move[1], move[2], copy);
                    int utility = min(copy, Integer.MIN_VALUE, Integer.MAX_VALUE, depth);
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
        } while (left > last * 8);
        log(bestMove, actual_depth, max, state);
        return bestMove;
    }

    int min(byte[] state, int alpha, int beta, int depth) {
        int f = 2;
        long key = Zobrist.getInstance().hash(state);
        Integer pro = probe(key, depth, alpha, beta, true);
        if (pro != null) return pro;
        if (depth == 0) {
            count++;
            int val = belgian(state);
            record(key, depth, 0, val, true);
            return val;
        }
        int value = Integer.MAX_VALUE;
        for (byte[] move : getAllPossibleMoves(counterSide, state)) {
            byte[] copy = new byte[state.length];
            System.arraycopy(state, 0, copy, 0, state.length);
            Game.move(move[0], move[1], move[2], copy);
            int utility = max(copy, alpha, beta, depth - 1);
            if (utility < value) value = utility;
            if (utility <= alpha) return utility;
            if (utility < beta) {
                f = 0;
                beta = utility;
            }
        }
        record(key, depth, f, value, true);
        return value;
    }

    int max(byte[] state, int alpha, int beta, int depth) {
        int f = 1;
        long key = Zobrist.getInstance().hash(state);
        Integer pro = probe(key, depth, alpha, beta, false);
        if (pro != null) return pro;
        if (depth == 0) {
            count++;
            int val = belgian(state);
            record(key, depth, 0, val, false);
            return val;
        }
        int value = Integer.MIN_VALUE;
        for (byte[] move : getAllPossibleMoves(side, state)) {
            byte[] copy = new byte[state.length];
            System.arraycopy(state, 0, copy, 0, state.length);
            Game.move(move[0], move[1], move[2], copy);
            int utility = min(copy, alpha, beta, depth - 1);
            if (utility > value) value = utility;
            if (utility >= beta) return utility;
            if (utility > alpha) {
                f = 0;
                alpha = utility;
            }
        }
        record(key, depth, f, value, false);
        return value;
    }

    Integer probe(long zkey, int depth, int alpha, int beta, boolean isMin) {
        int[] struct = isMin ? map.get(zkey) : map2.get(zkey);
        if (struct != null && struct[0] >= depth) {
            if (struct[1] == 0) return struct[2];
            if (struct[1] == 1 && struct[2] <= alpha) return alpha;
            if (struct[1] == 2 && struct[2] >= beta) return beta;
        }
        return null;
    }

    void record(long zkey, int depth, int f, int val, boolean isMin) {
        if (isMin) {
            synchronized (this) {
                int[] struct = map.get(zkey);
                if (struct != null && struct[0] > depth) return;
                map.put(zkey, new int[]{depth, f, val});
            }
        } else {
            synchronized (this) {
                int[] struct = map2.get(zkey);
                if (struct != null && struct[0] > depth) return;
                map2.put(zkey, new int[]{depth, f, val});
            }
        }
    }
}
