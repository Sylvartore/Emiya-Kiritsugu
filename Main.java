package sylvartore;

public class Main {

    public static void main(String[] args) {
        int c = 0;
        Board b = new Board();
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < Board.rowToCol(row); col++) {
                for (int dir = 0; dir < 6; dir++) {
                    for (int allyN = 1; allyN <= 3; allyN++) {
                        if (b.isValidMove(row, col, Board.Direction.values()[dir], allyN)) c++;
                    }
                }
            }
        }
        System.out.println(c);
    }

    public static void run() {
        Board b = new Board();
        b.move(6, 2, Board.Direction.LeftUp, 3);
        b.move(0, 2, Board.Direction.RightDown, 1);
        b.move(1, 3, Board.Direction.RightDown, 1);
        b.move(2, 4, Board.Direction.Right, 3);
        b.move(1, 2, Board.Direction.Right, 3);
        b.print();
        //    b.move(4,6,Board.Direction.LeftDown,3);
        b.print();
    }

    void testCase2(Board b) {
        b.move(8, Board.rowToCol(8) - 1, Board.Direction.LeftUp, 1);
        b.print();
        b.move(0, 0, Board.Direction.RightDown, 1);
        b.print();
        b.move(8, Board.rowToCol(8) - 2, Board.Direction.LeftUp, 1);
        b.print();
        b.move(0, 1, Board.Direction.RightDown, 1);
        b.print();
        b.move(8, 2, Board.Direction.RightUp, 1);
        b.print();
        b.move(1, 2, Board.Direction.RightDown, 1);
        b.print();
        b.move(7, 2, Board.Direction.RightUp, 1);
        b.move(6, 3, Board.Direction.RightUp, 1);
        b.print();
        b.move(5, 4, Board.Direction.RightUp, 1);
        b.print();
    }

    void testCase(Board b) {
        int[] targetCor;
        // System.out.println(b.getForce(7,5, Board.Direction.LeftDown));
        b.basicMove(1, 5, Board.Direction.LeftDown);
        targetCor = b.transCell(1, 5, Board.Direction.LeftDown);
        b.basicMove(targetCor[0], targetCor[1], Board.Direction.RightDown);
        targetCor = b.transCell(targetCor[0], targetCor[1], Board.Direction.RightDown);
        b.basicMove(targetCor[0], targetCor[1], Board.Direction.LeftDown);
        targetCor = b.transCell(targetCor[0], targetCor[1], Board.Direction.LeftDown);
        b.basicMove(targetCor[0], targetCor[1], Board.Direction.RightDown);
        targetCor = b.transCell(targetCor[0], targetCor[1], Board.Direction.RightDown);
        b.basicMove(targetCor[0], targetCor[1], Board.Direction.LeftDown);
        //  targetCor = b.transCell(targetCor[0], targetCor[1], Board.Direction.LeftDown);
        //   b.basicMove(targetCor[0], targetCor[1], Board.Direction.RightDown);
//        b.push(6,6,Board.Direction.Left);
//        b.push(6,7,Board.Direction.Left);
        b.print(false);
        b.move(6, 2, Board.Direction.Right, 1);
        b.move(6, 4, Board.Direction.Right, 1);
        b.move(6, 5, Board.Direction.Right, 1);
        System.out.println();
        b.print(false);
    }
}
