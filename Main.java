package sylvartore;

public class Main {

    public static void main(String[] args) {
        run();
    }

    public static void run() {
        Board b = new Board();
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
