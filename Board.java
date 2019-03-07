package sylvartore;

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

    public static int[][] transitionValue = {
            {0, -1},    //left
            {0, 1},     //right
            {-1, 0},    //left up
            {1, -1},    //left down
            {-1, 1},    // right up
            {1, 0},     // right down
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
    public void move(int row, int col, Direction d, int n) {

    }

    public int getForce(int row, int col, char owner, int force, Direction d) {
        if(force == 3) return 3;
        if (state[row][col] != owner) return force;
        return getForce(row + transitionValue[d.ordinal()][0], col + transitionValue[d.ordinal()][1],
                owner, force + 1, d);
    }


    public void baseMove(int row, int col, Direction d) {
        int targetRow = row + transitionValue[d.ordinal()][0];
        int targetCol = col + transitionValue[d.ordinal()][1];
        char temp = state[row][col];
        state[row][col] = state[targetRow][targetCol];
        state[targetRow][targetCol] = temp;
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
    }

    private void fillRow(int row) {
        char marble = '+';
        if (row >= 0 && row <= 2 || row >= rows - 3 && row <= rows - 1) marble = row <= 2 ? '@' : 'O';
        for (int i = 0; i < rows; i++) {
            state[row][i] = marble;
        }
        if (row == 2 || row == rows - 3) {
            state[row][0] = '+';
            state[row][1] = '+';
            state[row][5] = '+';
            state[row][6] = '+';
        }
    }
}
