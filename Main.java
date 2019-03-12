package sylvartore;

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
                            System.out.println(BitBoard.DimensionReduction[row][col]);
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
        // debug();
        // run();
        // benchMarking();
        run2();
    }

    static BitBoard1D b1 = new BitBoard1D();
    static BitBoard b = new BitBoard();

    public static void run2() {
        System.out.println(b.countPossibleMoves() + " / " + b1.countPossibleMoves());
        b1.print();
        move(0, BitBoard.RightDown, 1);
        move(1, BitBoard.RightDown, 1);
        move(58, BitBoard.RightUp, 1);
//        move(7, BitBoard.RightDown, 1);
        byte a = move(6, BitBoard.RightDown, 1);
        a = move(a, BitBoard.RightDown, 1);
         move(47, BitBoard.Right, 1);
        a = move(a, BitBoard.RightDown, 1);
        move(45, BitBoard.Right, 1);
//        a = move(a, BitBoard.RightDown, 1);
//        move(59, BitBoard.RightUp, 1);
//        move(21, BitBoard.RightDown, 1);
//        a = move(a, BitBoard.Right, 1);
//        a = move(a, BitBoard.RightDown, 1);
//        a = move(45, BitBoard.LeftUp, 3);
//        move(52, BitBoard.LeftUp, 3);
//        a = move(42, BitBoard.LeftDown, 1);
//        a = move(51, BitBoard.RightUp, 1);
//        a = move(a, BitBoard.RightUp, 1);
//       // a = move(a, BitBoard.RightUp, 1);
    }

    static byte move(int cell, byte d, int n) {
        byte c = (byte) cell;
        if (b.isValidMove(BitBoard.DimensionIncrement[cell][0], BitBoard.DimensionIncrement[cell][1], d, (byte) 1)) {
            b.move(BitBoard.DimensionIncrement[cell][0], BitBoard.DimensionIncrement[cell][1], d, (byte) n);
        }
        if (b1.isValidMove(c, d, (byte) n)) b1.move(c, d, (byte) n);
      //  System.out.println(b.countPossibleMoves() + " / " + b1.countPossibleMoves());
        b1.print();
        //  b1.printP();
        return BitBoard1D.TransitionMatrix[cell][d];
    }

    public static void run() {
        BitBoard b = new BitBoard();
        m(b, 0, BitBoard.RightDown);
        b.print();
        m(b, 1, BitBoard.RightDown);
        b.print();
        m(b, 58, BitBoard.RightUp);
        b.print();
        m(b, 7, BitBoard.RightDown);
        b.print();
        byte a = m(b, 14, BitBoard.RightDown);
        b.print();
        a = m(b, a, BitBoard.RightDown);
        b.print();
        a = m(b, a, BitBoard.RightDown);
        b.print();
        m(b, 59, BitBoard.RightUp);
        b.print();
        a = m(b, 21, BitBoard.RightDown);
        b.print();
        a = m(b, a, BitBoard.Right);
        b.print();
        a = m(b, a, BitBoard.RightDown);
        b.print();
        b.move(BitBoard.DimensionIncrement[45][0], BitBoard.DimensionIncrement[45][1], BitBoard.LeftUp, (byte) 3);
        b.print();
        b.move(BitBoard.DimensionIncrement[52][0], BitBoard.DimensionIncrement[52][1], BitBoard.LeftUp, (byte) 3);
        b.print();
    }

    public static byte m(BitBoard b, int c, byte d) {
        byte cell = (byte) c;
        if (b.isValidMove(BitBoard.DimensionIncrement[cell][0], BitBoard.DimensionIncrement[cell][1], d, (byte) 1)) {
            b.move(BitBoard.DimensionIncrement[cell][0], BitBoard.DimensionIncrement[cell][1], d, (byte) 1);
        }
        return BitBoard.TransitionMatrix[cell][d];
    }

    public static void benchMarking() {
        benchMarker3();
        benchMarker2();
        benchMarker1();

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

        System.out.println((double) (end - start) / 1000000L + "ms");
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
        System.out.println((double) (end - start) / 1000000L + "ms");
        System.out.println(c);
    }

    public static void benchMarker3() {
        int c = 0;
        BitBoard1D b = new BitBoard1D();
        long start = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            for (byte cell = 0; cell < b.state.length; cell++) {
                for (byte dir = 0; dir < 6; dir++) {
                    for (byte allyN = 1; allyN <= 3; allyN++) {
                        if (b.isValidMove(cell, dir, allyN)) c++;
                    }
                }
            }
        }
        long end = System.nanoTime();
        System.out.println((double) (end - start) / 1000000L + "ms");
        System.out.println(c);
    }
}
