package sylvartore;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Transposition extends AI {

    public Transposition(byte side, String name) {
        super(side, name);
    }

    byte[] getBestMove(int turnLeft, int aiTime, byte[] state) {
        int max = Integer.MIN_VALUE, depth = 2, actual = 0, actual_node = 0;
        nodeCount = 0;
        this.state = state;
        byte[] bestMove = null;
        long left = aiTime, last;
        do {
            if (depth > turnLeft && depth != 2 || (depth == 7 && aiTime <= 5000)) break;
            actual = depth;
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
                executor.awaitTermination(1, TimeUnit.DAYS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            actual_node = nodeCount;
            last = System.currentTimeMillis() - start;
            left -= last;
            depth++;
            max = max_cur;
            bestMove = bestMove_cur;
        } while ((left / 6) > last);
        log(bestMove, actual, max, actual_node, state);
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
}

