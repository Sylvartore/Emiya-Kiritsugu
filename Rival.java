package sylvartore;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Rival extends AI {
    public Rival(byte side) {
        super(side, "Rival");
    }

    byte[] getBestMove(int turnLeft, int aiTime, byte[] state) {
        int max = Integer.MIN_VALUE, actual_depth = 0;
        this.state = state;
        depth = 3;
        sum2 = 0;
        bc = 0;
        cc = 0;
        sum = 0;
        c = 0;
        byte[] bestMove = null;
        long left = aiTime, last, limit = System.currentTimeMillis() + aiTime;
        do {
            if (depth > turnLeft && depth != 3 || (depth == 7 && aiTime <= 5000)) break;
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
        } while (left > last * 8);
        log(bestMove, actual_depth, max, state);
//        System.out.println("Ally: " + sum + " count " + c + " average " + (double) sum / c);
//        System.out.println("enemy: " + sum2 + " count " + cc + " average " + (double) sum2 / cc);
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
}
