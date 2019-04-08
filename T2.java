package sylvartore;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class T2 extends TestAI {
    int depth;
    int Pcount;
    //    HashMap<Long, int[]> map;
//    HashMap<Long, int[]> map2;
    int col;
    final int size = 1 << 28;

    class struct {
        int depth;
        int f;
        int val;
        long key;

        public struct(int depth, int f, int val, long key) {
            this.depth = depth;
            this.f = f;
            this.val = val;
            this.key = key;
        }
    }

    struct[] t1;
    struct[] t2;

    public T2(byte side, int depth) {
        super(side, "TranspositionTable AI");
        this.depth = depth;
//        map = new HashMap<>();
//        map2 = new HashMap<>();

        t1 = new struct[size];
        t2 = new struct[size];
    }

    public T2(byte side) {
        this(side, 4);
    }


    byte[] getBestMove(int turnLeft, int aiTime, byte[] state) {
        return getBestMove_1(state);
    }

    byte[] getBestMove_1(byte[] state) {
        count = 0;
        Pcount = 0;
        max = Integer.MIN_VALUE;
        best = null;
        col = 0;
        ExecutorService executor = Executors.newFixedThreadPool(4);
        for (byte[] move : getAllPossibleMoves(side, state)) {
            executor.execute(() -> {
                byte[] copy = new byte[state.length];
                System.arraycopy(state, 0, copy, 0, state.length);
                Game.move(move[0], move[1], move[2], copy);
                int utility = min_1(copy, Integer.MIN_VALUE, Integer.MAX_VALUE, depth);
//                synchronized (this) {
                if (utility > max) {
                    max = utility;
                    best = move;
//                    }
                }
            });
        }
        try {
            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(name + " node searched: " + count + " probe success:" + Pcount + " collision: " + col + " best node found: " + max);
        return best;
    }

    int min_1(byte[] state, int alpha, int beta, int depth) {
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
            int utility = max_1(copy, alpha, beta, depth - 1);
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

    int max_1(byte[] state, int alpha, int beta, int depth) {
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
            int utility = min_1(copy, alpha, beta, depth - 1);
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
        int key = (int) (zkey % size);
        struct s = isMin ? t1[key] : t2[key];
        if (s != null && s.key == zkey && s.depth >= depth) {
            Pcount++;
            if (s.f == 0) return s.val;
            if (s.f == 1 && s.val <= alpha) return alpha;
            if (s.f == 2 && s.val >= beta) return beta;
        }
        return null;
    }

    void record(long zkey, int depth, int f, int val, boolean isMin) {
        int key = (int) (zkey % size);
        if (isMin) {
//            synchronized (this) {
            if (t1[key] != null) {
                if (t1[key].key != zkey)
                    col++;
                else if (t1[key].depth > depth) return;
            }
            t1[key] = new struct(depth, f, val, zkey);
//            }
        } else {
//            synchronized (this) {
            if (t2[key] != null) {
                if (t2[key].key != zkey)
                    col++;
                else if (t2[key].depth > depth) return;
            }
            t2[key] = new struct(depth, f, val, zkey);
//            }
        }
    }

//    Integer probe(long zkey, int depth, int alpha, int beta, boolean isMin) {
//        int[] struct = isMin ? map.get(zkey) : map2.get(zkey);
//        if (struct != null) {
//            if (struct[0] >= depth) {
//                Pcount++;
//                if (struct[1] == 0) return struct[2];
//                if (struct[1] == 1 && struct[2] <= alpha) return alpha;
//                if (struct[1] == 2 && struct[2] >= beta) return beta;
//            }
//        }
//        return null;
//    }
//
//    void record(long zkey, int depth, int f, int val, boolean isMin) {
//        if (isMin) {
//            synchronized (this) {
//                map.put(zkey, new int[]{depth, f, val});
//            }
//        } else {
//            synchronized (this) {
//                map2.put(zkey, new int[]{depth, f, val});
//            }
//        }
//    }
}
