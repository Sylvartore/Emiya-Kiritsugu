package sylvartore;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

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

    public static String[][] toStandardNotation = new String[][]{
            {"I5", "I6", "I7", "I8", "I9"},
            {"H4", "H5", "H6", "H7", "H8", "H9"},
            {"G3", "G4", "G5", "G6", "G7", "G8", "G9"},
            {"F2", "F3", "F4", "F5", "F6", "F7", "F8", "F9"},
            {"E1", "E2", "E3", "E4", "E5", "E6", "E7", "E8", "E9"},
            {"D1", "D2", "D3", "D4", "D5", "D6", "D7", "D8"},
            {"C1", "C2", "C3", "C4", "C5", "C6", "C7"},
            {"B1", "B2", "B3", "B4", "B5", "B6"},
            {"A1", "A2", "A3", "A4", "A5"},
    };

    public static int[][] dimensionReduction = new int[][]{
            {0, 1, 2, 3, 4},
            {5, 6, 7, 8, 9, 10},
            {11, 12, 13, 14, 15, 16, 17,},
            {18, 19, 20, 21, 22, 23, 24, 25,},
            {26, 27, 28, 29, 30, 31, 32, 33, 34},
            {35, 36, 37, 38, 39, 40, 41, 42},
            {43, 44, 45, 46, 47, 48, 49},
            {50, 51, 52, 53, 54, 55},
            {56, 57, 58, 59, 60}
    };

    public static boolean[][] isOutOfBound = new boolean[][]{
            {true, true, true, true, true, true, true, true, true, true, true},
            {true, false, false, false, false, false, true, true, true, true, true},
            {true, false, false, false, false, false, false, true, true, true, true},
            {true, false, false, false, false, false, false, false, true, true, true},
            {true, false, false, false, false, false, false, false, false, true, true},
            {true, false, false, false, false, false, false, false, false, false, true},
            {true, false, false, false, false, false, false, false, false, true, true},
            {true, false, false, false, false, false, false, false, true, true, true},
            {true, false, false, false, false, false, false, true, true, true, true},
            {true, false, false, false, false, false, true, true, true, true, true},
            {true, true, true, true, true, true, true, true, true, true, true}
    };

    public static int[][] transDown = {
            {0, -1},    //left
            {-1, 0},    //left up
            {-1, 1},    // right up
            {0, 1},     //right
            {1, 0},     // right down
            {1, -1},    //left down
    };
    public static int[][] transUp = {
            {0, -1},    //left
            {-1, -1},    //left up
            {-1, 0},    // right up
            {0, 1},     //right
            {1, 1},     // right down
            {1, 0},    //left down
    };

    enum Direction {Left, LeftUp, RightUp, Right, RightDown, LeftDown}

    public static int rows = 9;
    char[][] state;


    public Board() {
        state = new char[rows][rows];
        for (int row = 0; row < rows; row++) {
            fillRow(row);
        }
    }

    public boolean isValidMove(int row, int col, Direction d, int allyN) {
        if (!isWithinBoard(row, col) || state[row][col] == '+') return false;
        int[] targetCor = transCell(row, col, d);
        if (!isWithinBoard(targetCor[0], targetCor[1])) return false;
        if (allyN == 1) {
            if (state[targetCor[0]][targetCor[1]] == '+') return true;
            return canInline(row, col, d);
        } else {
            if (state[targetCor[0]][targetCor[1]] != '+') return false;
            Direction sideStepDirection = getClockwiseNext(d);
            if (canSideStep(row, col, d, allyN, sideStepDirection)) return true;
            sideStepDirection = getClockwiseNext(sideStepDirection);
            return canSideStep(row, col, d, allyN, sideStepDirection);
        }
    }

    public boolean canInline(int row, int col, Direction d) {
        int force = 0;
        int[] targetCor = transCell(row, col, d);
        while (++force <= 3 && isWithinBoard(targetCor[0], targetCor[1])
                && state[targetCor[0]][targetCor[1]] == state[row][col]) {
            targetCor = transCell(targetCor[0], targetCor[1], d);
        }
        if (force > 3 || !isWithinBoard(targetCor[0], targetCor[1])) return false;
        int[] counterForce = getF(targetCor[0], targetCor[1], d);
        return force > counterForce[2] && (!isWithinBoard(counterForce[0], counterForce[1])
                || state[counterForce[0]][counterForce[1]] != state[row][col]);
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

    public boolean canSideStep(int row, int col, Direction d, int allyN, Direction sideStepDire) {
        int[] ally1Cor = transCell(row, col, sideStepDire);
        if (!isWithinBoard(ally1Cor[0], ally1Cor[1]) || state[row][col] != state[ally1Cor[0]][ally1Cor[1]])
            return false;
        int[] allyTarCor = transCell(ally1Cor[0], ally1Cor[1], d);
        if (state[allyTarCor[0]][allyTarCor[1]] != '+') return false;
        if (allyN == 2) return true;
        int[] ally2Cor = transCell(ally1Cor[0], ally1Cor[1], sideStepDire);
        if (!isWithinBoard(ally2Cor[0], ally2Cor[1]) || state[row][col] != state[ally2Cor[0]][ally2Cor[1]])
            return false;
        allyTarCor = transCell(ally2Cor[0], ally2Cor[1], d);
        return state[allyTarCor[0]][allyTarCor[1]] == '+';
    }


    public void move(int row1, int col1, int row2, int col2, Direction d) {

    }

    public boolean move(int row, int col, Direction d, int allyN) {
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
        if (allyN == 1) {
            if (state[targetCor[0]][targetCor[1]] == '+') {
                basicMove(row, col, d);
                return true;
            }
            return tryInline(row, col, d);
        } else {
            if (state[targetCor[0]][targetCor[1]] != '+') {
                System.out.println("Invalid: target blocked");
                return false;
            }
            Direction sideStepDirection = getClockwiseNext(d);
            if (trySideStep(row, col, d, allyN, sideStepDirection)) return true;
            else {
                sideStepDirection = getClockwiseNext(sideStepDirection);
                if (!trySideStep(row, col, d, allyN, sideStepDirection)) {
                    System.out.println("Invalid: cant bring the ally you want");
                    return false;
                }
                return true;
            }
        }
    }

    public boolean trySideStep(int row, int col, Direction d, int allyN, Direction sideStepDire) {
        int[] ally1Cor = transCell(row, col, sideStepDire);
        if (!isWithinBoard(ally1Cor[0], ally1Cor[1]) || state[row][col] != state[ally1Cor[0]][ally1Cor[1]])
            return false;
        int[] allyTarCor = transCell(ally1Cor[0], ally1Cor[1], d);
        if (state[allyTarCor[0]][allyTarCor[1]] != '+') {
            System.out.println("Invalid: ally1 target blocked");
            return false;
        }
        if (allyN == 3) {
            int[] ally2Cor = transCell(ally1Cor[0], ally1Cor[1], sideStepDire);
            if (!isWithinBoard(ally2Cor[0], ally2Cor[1]) || state[row][col] != state[ally2Cor[0]][ally2Cor[1]]) {
                System.out.println("Invalid: not enough ally");
                return false;
            }
            allyTarCor = transCell(ally2Cor[0], ally2Cor[1], d);
            if (state[allyTarCor[0]][allyTarCor[1]] != '+') {
                System.out.println("Invalid: ally2 target blocked");
                return false;
            }
            basicMove(ally2Cor[0], ally2Cor[1], d);
        }
        basicMove(row, col, d);
        basicMove(ally1Cor[0], ally1Cor[1], d);
        return true;
    }

    public boolean tryInline(int row, int col, Direction d) {
        int force = 0;
        int[] targetCor = transCell(row, col, d);
        while (++force <= 3 && isWithinBoard(targetCor[0], targetCor[1]) && state[targetCor[0]][targetCor[1]] == state[row][col]) {
            targetCor = transCell(targetCor[0], targetCor[1], d);
        }
        if (force > 3 || !isWithinBoard(targetCor[0], targetCor[1])) {
            if (force > 3) {
                System.out.println("Invalid: too much marble to massMove");
                return false;
            }
            System.out.println("Invalid: suicide forbidden");
            return false;
        }
        int[] counterForce = getF(targetCor[0], targetCor[1], d);
        if (force > counterForce[2] && (!isWithinBoard(counterForce[0], counterForce[1])
                || state[counterForce[0]][counterForce[1]] != state[row][col])) {
            massMove(counterForce[0], counterForce[1], row, col, getCounterDirection(d));
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

    public void massMove(int toRow, int toCol, int fromRow, int fromCol, Direction d) {
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

    public boolean isWithinBoard(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < rowToCol[row];
    }

    public static int rowToCol[] = new int[]{5, 6, 7, 8, 9, 8, 7, 6, 5};

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

    private Direction getClockwiseNext(Direction direction) {
        int ordinal = direction.ordinal() + 1;
        if (ordinal > 5) ordinal = 0;
        return Direction.values()[ordinal];
    }

    public void show(UI.Square[][] squares) {
        int spaces = 4;
        boolean decre = true;
        int r = 0, c = 0;
        for (int row = 0; row < rows; row++) {
            for (int i = 0; i < spaces; i++) {
                c++;
            }
            for (int col = 0; col < state[row].length - spaces; col++) {
                char cur = state[row][col];/*
                if (cur != '+') {
                    ImageView a = new ImageView(new Image(state[row][col] == '@' ? "white-ball.png" : "black-ball.png"));
                    a.setFitWidth(80);
                    a.setFitHeight(80);
                    squares[r][c].getChildren().add(a);
                } else {
                    Circle cir = new Circle();
                    cir.setRadius(40);
                    cir.setFill(Color.RED);
                    squares[r][c].getChildren().add(cir);
                }*/
                ImageView a = new ImageView(new Image(cur != '+' ? "white-ball.png" : "black-ball.png"));
                a.setFitWidth(80);
                a.setFitHeight(80);
                a.setOnMouseClicked(new Listener(this, row, col));
                squares[r][c].getChildren().add(a);
                c++;
                c++;
            }
            spaces += decre ? -1 : 1;
            if (spaces == 0) decre = false;
            c = 0;
            r++;
        }
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


    public static int[][] transitionMatrix = new int[][]{
    };

    public void print() {
        print(false);
    }

    private void fillRow(int row) {
        char marble = '+';
        if (row >= 0 && row <= 2 || row >= rows - 3 && row <= rows - 1) marble = row <= 2 ? '@' : 'O';
        for (int i = 0; i < rowToCol[row]; i++) {
            state[row][i] = marble;
        }
        if (row == 2 || row == rows - 3) {
            state[row][0] = '+';
            state[row][1] = '+';
            state[row][5] = '+';
            state[row][6] = '+';
        }
    }

    public class Listener implements EventHandler<MouseEvent> {
        Board b;
        int row;
        int col;

        public Listener(Board b, int row, int col) {
            this.b = b;
            this.row = row;
            this.col = col;
        }

        @Override
        public void handle(MouseEvent event) {
            System.out.println(row + " " + col);
        }


    }
}
