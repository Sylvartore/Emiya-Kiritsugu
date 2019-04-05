package sylvartore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Transposition extends AI {

    public Transposition(byte side) {
        super(side,"Trans");
    }

    HashMap<Integer, int[]> map = new HashMap<>();
    int count;

    byte[] getBestMove(int turnLeft, int aiTime, byte[] state) {
        int max = Integer.MIN_VALUE, depth = 1, actual = 0, actual_node = 0;
        this.state = state;
        count = 0;
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
            actual = depth;
            last = System.currentTimeMillis() - start;
            left -= last;
            depth++;
            max = max_cur;
            bestMove = bestMove_cur;
        } while ((left) > last * 6);
        log(bestMove, actual, max, state);
        System.out.println(map.size());
        System.out.println("probed " + count);
        return bestMove;
    }

    int min(byte[] state, int alpha, int beta, int depth, int turn) {
        int f = 2;
        int key = Game.z.hash(state);
        Integer pro = probe(key, depth, alpha, beta);
        if (pro != null) return pro;
        if (depth == 0 || turn == 0) {
            int val = heuristic(state);
            record(key, depth, 0, val, state);
            return val;
        }
        int value = Integer.MAX_VALUE;
        for (byte[] move : getAllPossibleMoves(counterSide, state)) {
            byte[] copy = new byte[state.length];
            System.arraycopy(state, 0, copy, 0, state.length);
            Game.move(move[0], move[1], move[2], copy);
            int utility = max(copy, alpha, beta, depth - 1, turn - 1);
            if (utility < value) value = utility;
            if (utility <= alpha) {
                record(key, depth, 1, utility, state);
                return utility;
            }
            if (utility < beta) {
                f = 0;
                beta = utility;
            }
        }
        record(key, depth, f, value, state);
        return value;
    }

    Integer probe(int key, int depth, int alpha, int beta) {
        int[] struct = map.get(key);
        if (struct != null) {
            if (struct[0] >= depth) {
                count++;
                if (struct[1] == 0) return struct[2];
                if (struct[1] == 1 && struct[2] <= alpha) return alpha;
                if (struct[1] == 2 && struct[2] >= beta) return beta;
            }
        }
        return null;
    }

    synchronized void record(int key, int depth, int f, int val, byte[] state) {
        map.put(key, new int[]{depth, f, val});
        //    map.put(key, state);
    }

    int max(byte[] state, int alpha, int beta, int depth, int turn) {
        int f = 1;
        int key = Game.z.hash(state);
        Integer pro = probe(key, depth, alpha, beta);
        if (pro != null) return pro;
        if (depth == 0 || turn == 0) {
            int val = heuristic(state);
            record(key, depth, 0, val, state);
            return val;
        }
        int value = Integer.MIN_VALUE;
        for (byte[] move : getAllPossibleMoves(side, state)) {
            byte[] copy = new byte[state.length];
            System.arraycopy(state, 0, copy, 0, state.length);
            Game.move(move[0], move[1], move[2], copy);
            int utility = min(copy, alpha, beta, depth - 1, turn - 1);
            if (utility > value) value = utility;
            if (utility >= beta) {
                record(key, depth, 2, utility, state);
                return utility;
            }
            if (utility > alpha) {
                f = 0;
                alpha = utility;
            }
        }
        record(key, depth, f, value, state);
        return value;
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
}

