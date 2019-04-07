package sylvartore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TestAI extends AI {
    int depth;

    public TestAI(byte side) {
        super(side, "TEST AI");
    }

    public TestAI(byte side, String name) {
        super(side, name);
    }


    int max;
    byte[] best;

    byte[] getBestMove_1(byte[] state) {
        count = 0;
        max = Integer.MIN_VALUE;
        best = null;
        ExecutorService executor = Executors.newFixedThreadPool(4);
        for (byte[] move : getAllPossibleMoves(side, state)) {
            executor.execute(() -> {
                byte[] copy = new byte[state.length];
                System.arraycopy(state, 0, copy, 0, state.length);
                Game.move(move[0], move[1], move[2], copy);
                int utility = min_1(copy, Integer.MIN_VALUE, Integer.MAX_VALUE, 5);
                synchronized (this) {
                    if (utility > max) {
                        max = utility;
                        best = move;
                    }
                }
            });
        }
        try {
            executor.shutdown();
            executor.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(name + " node searched: " + count + " best node found: " + max);
        return best;
    }

    int min_1(byte[] state, int alpha, int beta, int depth) {
        if (depth == 0) {
            count++;
            return heuristic(state);
        }
        int value = Integer.MAX_VALUE;
        for (byte[] move : getAllPossibleMoves(counterSide, state)) {
            byte[] copy = new byte[state.length];
            System.arraycopy(state, 0, copy, 0, state.length);
            Game.move(move[0], move[1], move[2], copy);
            int utility = max_1(copy, alpha, beta, depth - 1);
            if (utility < value) value = utility;
            if (utility <= alpha) return utility;
            if (utility < beta) beta = utility;
        }
        return value;
    }

    int max_1(byte[] state, int alpha, int beta, int depth) {
        if (depth == 0) {
            count++;
            return heuristic(state);
        }
        int value = Integer.MIN_VALUE;
        for (byte[] move : getAllPossibleMoves(side, state)) {
            byte[] copy = new byte[state.length];
            System.arraycopy(state, 0, copy, 0, state.length);
            Game.move(move[0], move[1], move[2], copy);
            int utility = min_1(copy, alpha, beta, depth - 1);
            if (utility > value) value = utility;
            if (utility >= beta) return utility;
            if (utility > alpha) alpha = utility;
        }
        return value;
    }
}
