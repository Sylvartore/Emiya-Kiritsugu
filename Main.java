package sylvartore;

import java.util.*;

public class Main {

    public static void test() {
        Scanner scan = new Scanner(System.in);
        System.out.println("Info:\nPlease place input files under the same directory.");
        System.out.println("Please enter a range, the program will read input files from " +
                "\"Test1.input\" to \"TestN.input\". (N is input range)");
        while (!scan.hasNextInt()) {
            System.out.println("Invalid input, input must be an integer greater than 1");
            scan.next();
        }
        int range = scan.nextInt();
        while (range < 1) {
            System.out.println("Invalid input, input must be an integer greater than 1");
            while (!scan.hasNextInt()) {
                System.out.println("Invalid input, input must be an integer greater than 1");
                scan.next();
            }
            range = scan.nextInt();
        }
        for (int i = 1; i <= range; i++) {
            BitBoard1D b = new BitBoard1D();
            b.test(i);
        }
        System.out.println("FINISHED!");
    }

    public static void main(String[] args) {
        //test();
        //  debug();
        benchMarking2();
        //run();
        //  benchMarking();
        //  run2();
//        b1.state = b1.getBelgianDaisyLayout();
//        b1.print();
//        b1.tryMove((byte) 8, (byte) 0, (byte) 1);
//        b1.print();
    }

//    public static void debug() {
//        b1.state = b1.getBelgianDaisyLayout();
//        for (byte cell = 0; cell < b.state.length; cell++) {
//            for (byte dir = 0; dir < 6; dir++) {
//                for (byte n = 1; n <= 3; n++) {
//                    if (b1.state[cell] != (byte) 1) continue;
//                    BitBoard1D copy = new BitBoard1D(b1);
//                    boolean a = copy.tryMove(cell, dir, n);
//                    if (b1.isValidMove(cell, dir, n) != a) {
//                        System.out.println(a);
//                        System.out.println(cell + " / " + dir + " / " + n);
//                    }
//                }
//            }
//        }
//    }

    public static void benchMarking2() {
        //b1.readLayout();
        for (int i = 1; i < 8; i++) {
            System.out.println("\ntest " + i);
            b1.readLayout(i);
            benchMarker_1();
            benchMarker_in_one();

        }


    }


    public static void benchMarker_in_one() {
        long p1 = 0;
        long to = 0;
        long ac = System.nanoTime();
        List<byte[]> ans = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            for (byte cell = 0; cell < b1.state.length; cell++) {
                for (byte dir = 0; dir < 6; dir++) {
                    for (byte n = 1; n <= 3; n++) {
                        long start = System.nanoTime();
                        byte[] copy = new byte[b1.state.length];
                        System.arraycopy(b1.state, 0, copy, 0, b1.state.length);
                        p1 += System.nanoTime() - start;
                        if (BitBoard1D.tryMove(cell, dir, n, copy)) {
                            ans.add(copy);
                        }
                        to += System.nanoTime() - start;
                    }
                }
            }
        }
        long ace = System.nanoTime();
        System.out.println("new new： " + (double) p1 / 1000000 + "ms");
        System.out.println("new total： " + (double) to / 1000000 + "ms");
        System.out.println("new AC： " + (double) (ace - ac) / 1000000 + "ms");
        System.out.println(ans.size());
    }

    public static void benchMarker_1() {
        long p1 = 0;
        long to = 0;
        long ac = System.nanoTime();
        List<BitBoard1D> ans = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            for (byte cell = 0; cell < b1.state.length; cell++) {
                for (byte dir = 0; dir < 6; dir++) {
                    for (byte n = 1; n <= 3; n++) {
                        long start = System.nanoTime();
                        boolean a = b1.isValidMove(cell, dir, n);
                        p1 += System.nanoTime() - start;
                        if (a) {
                            BitBoard1D copy = new BitBoard1D(b1);
                            copy.move(cell, dir, n);
                            ans.add(copy);
                        }
                        to += System.nanoTime() - start;
                    }
                }
            }
        }
        long ace = System.nanoTime();
        System.out.println("old isValid： " + (double) p1 / 1000000 + "ms");
        System.out.println("old total：" + (double) to / 1000000 + "ms");
        System.out.println("old AC： " + (double) (ace - ac) / 1000000 + "ms");
        System.out.println(ans.size());
    }

//    public static void debug() {
////        b1.state = b1.getStandardInitialLayout();
////        long start = System.nanoTime();
////        List<BitBoard1D> ans = b1.getAllPossibleMove_in_one((byte)1);
////        long end = System.nanoTime();
////        System.out.println((double) (end - start) / 1000000 + "ms");
////        System.out.println(ans.size());
//    }

    static void printer() {
        for (byte i = 0; i < b1.state.length; i++) {
            System.out.print("{");
            for (byte j = 0; j < 6; j++) {
                System.out.print(b1.printTable(i, j) + ", ");
            }
            System.out.println("},");
        }
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
//        a.txt = move(a.txt, BitBoard.RightDown, 1);
//        move(59, BitBoard.RightUp, 1);
//        move(21, BitBoard.RightDown, 1);
//        a.txt = move(a.txt, BitBoard.Right, 1);
//        a.txt = move(a.txt, BitBoard.RightDown, 1);
//        a.txt = move(45, BitBoard.LeftUp, 3);
//        move(52, BitBoard.LeftUp, 3);
//        a.txt = move(42, BitBoard.LeftDown, 1);
//        a.txt = move(51, BitBoard.RightUp, 1);
//        a.txt = move(a.txt, BitBoard.RightUp, 1);
//       // a.txt = move(a.txt, BitBoard.RightUp, 1);
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
        benchMarker4();
        benchMarker3();
//        benchMarker2();
//        benchMarker1();
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
                            if (b.isValidMove(row, col, Board.Direction.values()[dir], allyN))
                                if (i == 0) c++;
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
                                if (b.isValidMove(row, col, dir, allyN))
                                    if (i == 0) c++;
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
                        if (b.isValidMove(cell, dir, allyN))
                            if (i == 0) c++;
                    }
                }
            }
        }
        long end = System.nanoTime();
        System.out.println((double) (end - start) / 1000000L + "ms");
        System.out.println(c);
    }

    public static void benchMarker4() {
        int c = 0;
        BitBoard1D b = new BitBoard1D();
        long start = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            for (byte cell = 0; cell < b.state.length; cell++) {
                for (byte dir = 0; dir < 6; dir++) {
                    for (byte allyN = 1; allyN <= 3; allyN++) {
                        if (b.tableSideStepMove(cell, dir, allyN))
                            if (i == 0) c++;
                    }
                }
            }
        }
        long end = System.nanoTime();
        System.out.println((double) (end - start) / 1000000L + "ms");
        System.out.println(c);
    }
}
