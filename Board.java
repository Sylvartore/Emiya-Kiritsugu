package sylvartore;

import static sylvartore.Board.Direction.*;

/**
 * ....I O O O O O
 * ...H O O O O O O
 * ..G + + O O O + +
 * .F + + + + + + + +
 * E + + + + + + + + +
 * .D + + + + + + + + 9
 * ..C + + @ @ @ + + 8
 * ...B @ @ @ @ @ @ 7
 * ....A @ @ @ @ @ 6
 * ......1 2 3 4 5
 */


public class Board {


    public static int[][] transDown = {
            {0, -1},    //left
            {0, 1},     //right
            {-1, 0},    //left up
            {1, -1},    //left down
            {-1, 1},    // right up
            {1, 0},     // right down
    };
    public static int[][] transUp = {
            {0, -1},    //left
            {0, 1},     //right
            {-1, -1},    //left up
            {1, 0},    //left down
            {-1, 0},    // right up
            {1, 1},     // right down
    };

    enum Direction {Left, Right, LeftUp, LeftDown, RightUp, RightDown}

    public static int rows = 9;
    char[][] state;

    public Board() {
        state = new char[rows][rows];
        for (int row = 0; row < rows; row++) {
            fillRow(row);
        }
    }

    //16 *
    public void move(int row1, int col1, int row2, int col2, Direction d) {

    }

    // 16 * 6 * 3 = 252
    public boolean move(int row, int col, Direction d, int n) {
        if (!isWithinBoard(row, col)) {
            System.out.println("Invalid: origin out of bound");
            return false;
        }
        if (state[row][col] == '+') {
            System.out.println("nothing there");
            return false;
        }
        int[] targetCor = transCell(row, col, d);
        if (!isWithinBoard(targetCor[0], targetCor[1])) {
            System.out.println("Invalid: suicide forbidden");
            return false;
        }
        if (n == 1) {
            if (state[targetCor[0]][targetCor[1]] == '+') {
                basicMove(row, col, d);
                return true;
            }
            return tryPush(row, col, d);
        } else {
            return true;
        }
    }

    public boolean tryPush(int row, int col, Direction d) {
        int force = 0;
        int[] targetCor = transCell(row, col, d);
        while (++force <= 3 && isWithinBoard(targetCor[0], targetCor[1]) && state[targetCor[0]][targetCor[1]] == state[row][col]) {
            targetCor = transCell(targetCor[0], targetCor[1], d);
        }
        if (force > 3 || !isWithinBoard(targetCor[0], targetCor[1])) {
            if (force > 3) {
                System.out.println("Invalid: too much marble to push");
                return false;
            }
            System.out.println("Invalid: suicide forbidden");
            return false;
        }
        int[] counterForce = getF(targetCor[0], targetCor[1], d);
        if (force > counterForce[2] && (!isWithinBoard(counterForce[0], counterForce[1])
                || state[counterForce[0]][counterForce[1]] != state[row][col])) {
            push(counterForce[0], counterForce[1], row, col, getCounterDirection(d));
        } else {
            if (force <= counterForce[2]) {
                System.out.println("Invalid: not enough force");
                return false;
            }
            System.out.println("Invalid: final place is not applicable");
            return false;
        }
        return true;
    }

    public void push(int toRow, int toCol, int fromRow, int fromCol, Direction d) {
        int[] prev = new int[]{toRow, toCol};
        int[] next = transCell(toRow, toCol, d);
        while ((prev[0] == toRow && prev[1] == toCol || state[prev[0]][prev[1]] != '+') && (prev[0] != fromRow || prev[1] != fromCol)) {
            if (isWithinBoard(prev[0], prev[1])) state[prev[0]][prev[1]] = state[next[0]][next[1]];
            prev[0] = next[0];
            prev[1] = next[1];
            next = transCell(prev[0], prev[1], d);
        }
        state[fromRow][fromCol] = '+';
    }

    public int[] getF(int row, int col, Direction d) {
        if (!isWithinBoard(row, col) || state[row][col] == '+') return new int[]{row, col, 0};
        int force = 1;
        int[] targetCor = transCell(row, col, d);
        while (isWithinBoard(targetCor[0], targetCor[1]) && state[row][col] == state[targetCor[0]][targetCor[1]] && ++force <= 3) {
            targetCor = transCell(targetCor[0], targetCor[1], d);
        }
        if (state[targetCor[0]][targetCor[1]] != '+') targetCor = transCell(targetCor[0], targetCor[1], d);
        return new int[]{targetCor[0], targetCor[1], force == 4 ? 0 : force};
    }

    public void basicMove(int row, int col, Direction d) {
        int[] targetCor = transCell(row, col, d);
        char temp = state[row][col];
        state[row][col] = state[targetCor[0]][targetCor[1]];
        state[targetCor[0]][targetCor[1]] = temp;
    }

    public int[] transCell(int row, int col, Direction d) {
        int targetRow = row + transDown[d.ordinal()][0];
        if (targetRow > 4 || targetRow == 4 && row > 4) return new int[]{targetRow, col + transDown[d.ordinal()][1]};
        return new int[]{targetRow, col + transUp[d.ordinal()][1]};
    }

    public void print(boolean testMode) {
        int spaces = 4;
        boolean decre = true;
        for (int row = 0; row < rows; row++) {
            StringBuilder sb = new StringBuilder();
            if (!testMode) {
                for (int i = 0; i < spaces; i++) {
                    sb.append(" ");
                }
            }
            for (int i = 0; i < state[row].length - spaces; i++) {
                sb.append(state[row][i]).append(" ");
            }
            System.out.println(String.valueOf(sb));
            spaces += decre ? -1 : 1;
            if (spaces == 0) decre = false;
        }
        System.out.println();
    }

    public void print() {
        print(false);
    }

    private void fillRow(int row) {
        char marble = '+';
        if (row >= 0 && row <= 2 || row >= rows - 3 && row <= rows - 1) marble = row <= 2 ? '@' : 'O';
        for (int i = 0; i < rowToCol(row); i++) {
            state[row][i] = marble;
        }
        if (row == 2 || row == rows - 3) {
            state[row][0] = '+';
            state[row][1] = '+';
            state[row][5] = '+';
            state[row][6] = '+';
        }
    }

    private boolean isWithinBoard(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < rowToCol(row);
    }

    private Direction getCounterDirection(Direction direction) {
        switch (direction) {
            case Left:
                return Right;
            case Right:
                return Left;
            case LeftUp:
                return RightDown;
            case LeftDown:
                return RightUp;
            case RightDown:
                return LeftUp;
            case RightUp:
                return LeftDown;
        }
        return null;
    }

    public static int rowToCol(int row) {
        switch (row) {
            case 4:
                return 9;
            case 3:
            case 5:
                return 8;
            case 2:
            case 6:
                return 7;
            case 1:
            case 7:
                return 6;
            default:
                return 5;
        }
    }
}
