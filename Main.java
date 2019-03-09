package sylvartore;

import java.util.Arrays;

public class Main {

    public static void debug() {
        int c = 0;
        BitBoard b = new BitBoard();
        Board bb = new Board();
        long start = System.nanoTime();
        for (byte row = 0; row < 9; row++) {
            for (byte col = 0; col < Board.rowToCol[row]; col++) {
                for (byte dir = 0; dir < 6; dir++) {
                    for (byte allyN = 1; allyN <= 3; allyN++) {
                        if (b.isValidMove(row, col, dir, allyN)
                                != bb.isValidMove(row - 1, col - 1, Board.Direction.values()[dir], allyN)) {
                            System.out.println(BitBoard.dimensionReduction[row][col]);
                            System.out.println(row + " r/c " + col + " di: " + dir + " n:" + allyN);
                            c++;
                        }
                    }
                }
            }
        }
        long end = System.nanoTime();
        System.out.println((double) (end - start) / 1000000 + "ms");
        System.out.println(c);
    }

    public static void main(String[] args) {
        debug();
        //run();
        benchMarking();

    }

    public static void run() {
        Board b = new Board();
        b.move(2, 3, Board.Direction.RightDown, 1);
        b.print();
    }

    public static void benchMarking() {
        benchMarker1();
        benchMarker2();

    }

    static void matrixPrinter() {
        Board b = new Board();
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < Board.rowToCol[row]; col++) {
                System.out.print("{");
                for (int i = 0; i < 6; i++) {
                    int[] cor = b.transCell(row, col, Board.Direction.values()[i]);
                    if (b.isWithinBoard(cor[0], cor[1]))
                        System.out.print(Board.dimensionReduction[cor[0]][cor[1]]);
                    else System.out.print("-1");
                    System.out.print(",");
                }
                System.out.print("},");
                System.out.println();
            }
        }
    }

    static void printer() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < Board.rowToCol[row]; col++) {
                System.out.print("{" + (row + 1) + "," + (col + 1) + "},");
            }
            System.out.println();
        }
    }

    public static void benchMarker1() {
        int c = 0;
        Board b = new Board();
        long start = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            for (int row = 0; row < 9; row++) {
                for (int col = 0; col < Board.rowToCol[row]; col++) {
                    for (int dir = 0; dir < 6; dir++) {
                        for (int allyN = 1; allyN <= 3; allyN++) {
                            if (b.isValidMove(row, col, Board.Direction.values()[dir], allyN)) c++;
                        }
                    }
                }
            }
        }
        long end = System.nanoTime();
        System.out.println((double) (end - start) / 1000000 + "ms");
        System.out.println(c);
    }

    public static void benchMarker2() {
        int c = 0;
        BitBoard b = new BitBoard();
        long start = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            for (byte row = 0; row < b.state.length; row++) {
                for (byte col = 0; col < b.state[row].length; col++) {
                    if (b.state[row][col] != 11) {
                        for (byte dir = 0; dir < 6; dir++) {
                            for (byte allyN = 1; allyN <= 3; allyN++) {
                                if (b.isValidMove(row, col, dir, allyN)) c++;
                            }
                        }
                    }
                }
            }
        }
        long end = System.nanoTime();
        System.out.println((double) (end - start) / 1000000 + "ms");
        System.out.println(c);
    }
}
