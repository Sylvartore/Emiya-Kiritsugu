package sylvartore;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class BitBoard1D {

    public byte[] standardInitialLayout() {
        return new byte[]{
                1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1,
                0, 0, 1, 1, 1, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 10, 10, 10, 0, 0,
                10, 10, 10, 10, 10, 10,
                10, 10, 10, 10, 10,
        };
    }

    public static String[] toStandardNotation = new String[]{
            "I5", "I6", "I7", "I8", "I9",
            "H4", "H5", "H6", "H7", "H8", "H9",
            "G3", "G4", "G5", "G6", "G7", "G8", "G9",
            "F2", "F3", "F4", "F5", "F6", "F7", "F8", "F9",
            "E1", "E2", "E3", "E4", "E5", "E6", "E7", "E8", "E9",
            "D1", "D2", "D3", "D4", "D5", "D6", "D7", "D8",
            "C1", "C2", "C3", "C4", "C5", "C6", "C7",
            "B1", "B2", "B3", "B4", "B5", "B6",
            "A1", "A2", "A3", "A4", "A5"
    };
    public static int[][] ui = new int[][]{
            {0, 4}, {0, 6}, {0, 8}, {0, 10}, {0, 12},
            {1, 3}, {1, 5}, {1, 7}, {1, 9}, {1, 11}, {1, 13},
            {2, 2}, {2, 4}, {2, 6}, {2, 8}, {2, 10}, {2, 12}, {2, 14},
            {3, 1}, {3, 3}, {3, 5}, {3, 7}, {3, 9}, {3, 11}, {3, 13}, {3, 15},
            {4, 0}, {4, 2}, {4, 4}, {4, 6}, {4, 8}, {4, 10}, {4, 12}, {4, 14}, {4, 16},
            {5, 1}, {5, 3}, {5, 5}, {5, 7}, {5, 9}, {5, 11}, {5, 13}, {5, 15},
            {6, 2}, {6, 4}, {6, 6}, {6, 8}, {6, 10}, {6, 12}, {6, 14},
            {7, 3}, {7, 5}, {7, 7}, {7, 9}, {7, 11}, {7, 13},
            {8, 4}, {8, 6}, {8, 8}, {8, 10}, {8, 12},
    };


    public static final byte[][] TransitionMatrix = new byte[][]{
            {-1, -1, -1, 1, 6, 5},
            {0, -1, -1, 2, 7, 6},
            {1, -1, -1, 3, 8, 7},
            {2, -1, -1, 4, 9, 8},
            {3, -1, -1, -1, 10, 9},
            {-1, -1, 0, 6, 12, 11},
            {5, 0, 1, 7, 13, 12},
            {6, 1, 2, 8, 14, 13},
            {7, 2, 3, 9, 15, 14},
            {8, 3, 4, 10, 16, 15},
            {9, 4, -1, -1, 17, 16},
            {-1, -1, 5, 12, 19, 18},
            {11, 5, 6, 13, 20, 19},
            {12, 6, 7, 14, 21, 20},
            {13, 7, 8, 15, 22, 21},
            {14, 8, 9, 16, 23, 22},
            {15, 9, 10, 17, 24, 23},
            {16, 10, -1, -1, 25, 24},
            {-1, -1, 11, 19, 27, 26},
            {18, 11, 12, 20, 28, 27},
            {19, 12, 13, 21, 29, 28},
            {20, 13, 14, 22, 30, 29},
            {21, 14, 15, 23, 31, 30},
            {22, 15, 16, 24, 32, 31},
            {23, 16, 17, 25, 33, 32},
            {24, 17, -1, -1, 34, 33},
            {-1, -1, 18, 27, 35, -1},
            {26, 18, 19, 28, 36, 35},
            {27, 19, 20, 29, 37, 36},
            {28, 20, 21, 30, 38, 37},
            {29, 21, 22, 31, 39, 38},
            {30, 22, 23, 32, 40, 39},
            {31, 23, 24, 33, 41, 40},
            {32, 24, 25, 34, 42, 41},
            {33, 25, -1, -1, -1, 42},
            {-1, 26, 27, 36, 43, -1},
            {35, 27, 28, 37, 44, 43},
            {36, 28, 29, 38, 45, 44},
            {37, 29, 30, 39, 46, 45},
            {38, 30, 31, 40, 47, 46},
            {39, 31, 32, 41, 48, 47},
            {40, 32, 33, 42, 49, 48},
            {41, 33, 34, -1, -1, 49},
            {-1, 35, 36, 44, 50, -1},
            {43, 36, 37, 45, 51, 50},
            {44, 37, 38, 46, 52, 51},
            {45, 38, 39, 47, 53, 52},
            {46, 39, 40, 48, 54, 53},
            {47, 40, 41, 49, 55, 54},
            {48, 41, 42, -1, -1, 55},
            {-1, 43, 44, 51, 56, -1},
            {50, 44, 45, 52, 57, 56},
            {51, 45, 46, 53, 58, 57},
            {52, 46, 47, 54, 59, 58},
            {53, 47, 48, 55, 60, 59},
            {54, 48, 49, -1, -1, 60},
            {-1, 50, 51, 57, -1, -1},
            {56, 51, 52, 58, -1, -1},
            {57, 52, 53, 59, -1, -1},
            {58, 53, 54, 60, -1, -1},
            {59, 54, 55, -1, -1, -1},
    };

    public static final byte Left = 0;
    public static final byte LeftUp = 1;
    public static final byte RightUp = 2;
    public static final byte Right = 3;
    public static final byte RightDown = 4;
    public static final byte LeftDown = 5;
    public static final byte[] CounterDirection = new byte[]{3, 4, 5, 0, 1, 2};

    byte state[];
    byte stateP[];
    private byte black;
    private byte white;
    UI.Square[][] squares;

    public BitBoard1D() {
        state = standardInitialLayout();
        stateP = new byte[61];
    }

    public BitBoard1D(BitBoard1D source) {
        white = source.white;
        black = source.black;
        state = new byte[61];
        System.arraycopy(source.state, 0, state, 0, source.state.length);
    }

    public boolean isValidMove(byte cell, byte d, byte n) {
        return isValidMove(cell, d, n, state);
    }

    public boolean isValidMove(byte cell, byte d, byte n, byte[] state) {
        if (state[cell] == 0) return false;
        byte targetCell = TransitionMatrix[cell][d];
        if (targetCell == -1) return false;
        if (n == 1) {
            if (state[targetCell] == 0) return true;
            return canInline(cell, targetCell, d);
        } else {
            if (state[targetCell] != 0) return false;
            byte ssd = d;
            if (++ssd == 6) ssd = 0;
            if (canSideStep(cell, d, n, ssd)) return true;
            if (++ssd == 6) ssd = 0;
            return canSideStep(cell, d, n, ssd);
        }
    }

    public boolean canInline(byte cell, byte targetCell, byte d) {
        byte force = 0;
        while (++force <= 3 && targetCell != -1 && state[targetCell] == state[cell]) {
            targetCell = TransitionMatrix[targetCell][d];
        }
        if (force > 3 || targetCell == -1) return false;
        int[] counterForce = getF(targetCell, d);
        return force > counterForce[1] && (counterForce[0] == -1 || state[counterForce[0]] != state[cell]);
    }

    public int[] getF(byte cell, byte d) {
        if (state[cell] == 0) return new int[]{cell, 0};
        byte force = 0;
        byte targetCell = cell;
        while (targetCell != -1 && ++force <= 3 && TransitionMatrix[targetCell][d] == state[cell]) {
            targetCell = TransitionMatrix[targetCell][d];
        }
        if (targetCell != -1) {
            if (state[targetCell] != 0) {
                targetCell = TransitionMatrix[targetCell][d];
            }
        }
        return new int[]{targetCell, force};
    }

    public boolean canSideStep(byte cell, byte d, byte n, byte ssd) {
        byte ally1Cell = TransitionMatrix[cell][ssd];
        if (ally1Cell == -1 || state[cell] != state[ally1Cell])
            return false;
        byte ally1SsdCell = TransitionMatrix[ally1Cell][d];
        if (ally1SsdCell == -1 || state[ally1SsdCell] != 0) return false;
        if (n == 2) return true;
        byte ally2Cell = TransitionMatrix[ally1Cell][ssd];
        if (ally2Cell == -1 || state[cell] != state[ally2Cell])
            return false;
        byte ally2SsdCell = TransitionMatrix[ally2Cell][d];
        return ally2SsdCell != -1 && state[ally2SsdCell] == 0;
    }

    public void move(byte cell, byte d, byte n) {
        move(cell, d, n, state);
    }

    public void move(byte cell, byte d, byte n, byte[] state) {
        byte targetCell = TransitionMatrix[cell][d];
        if (n == 1) {
            if (state[targetCell] == 0) {
                state[targetCell] = state[cell];
                state[cell] = 0;
            } else inlineMove(cell, targetCell, d);
        } else {
            byte ssd = d;
            if (++ssd == 6) ssd = 0;
            if (sideStepMove(cell, d, n, ssd, targetCell)) return;
            if (++ssd == 6) ssd = 0;
            sideStepMove(cell, d, n, ssd, targetCell);
        }
    }

    public void inlineMove(byte cell, byte prev, byte d) {
        while (prev != -1 && TransitionMatrix[prev][d] != -1 && state[prev] != 0) {
            prev = TransitionMatrix[prev][d];
        }
        if (state[prev] != 0 && TransitionMatrix[prev][d] != -1) prev = TransitionMatrix[prev][d];
        byte next = TransitionMatrix[prev][CounterDirection[d]];
        byte to = prev;
        while ((prev == to || state[prev] != 0)
                && (prev != cell)) {
            if (TransitionMatrix[prev][d] == -1) {
                if (state[prev] == 1) black++;
                if (state[prev] == 10) white++;
            }
            state[prev] = state[next];
            prev = next;
            next = TransitionMatrix[prev][CounterDirection[d]];
        }
        state[cell] = 0;
    }

    public boolean sideStepMove(byte cell, byte d, byte n, byte ssd, byte targetCell) {
        byte ally1Cell = TransitionMatrix[cell][ssd];
        if (ally1Cell == -1 || state[cell] != state[ally1Cell]) return false;
        byte ally1SsdCell = TransitionMatrix[ally1Cell][d];
        if (state[ally1SsdCell] != 0) return false;
        if (n == 3) {
            byte ally2Cell = TransitionMatrix[ally1Cell][ssd];
            if (ally2Cell == -1 || state[cell] != state[ally2Cell]) return false;
            byte ally2SsdCell = TransitionMatrix[ally2Cell][d];
            if (state[ally2SsdCell] != 0) return false;
            state[ally2SsdCell] = state[ally2Cell];
            state[ally2Cell] = 0;
        }
        state[targetCell] = state[cell];
        state[cell] = 0;
        state[ally1SsdCell] = state[ally1Cell];
        state[ally1Cell] = 0;
        return true;
    }

    int countPossibleMoves() {
        int c = 0;
        for (byte cell = 0; cell < state.length; cell++) {
            byte p = 0;
            for (byte dir = 0; dir < 6; dir++) {
                for (byte allyN = 1; allyN <= 3; allyN++) {
                    if (isValidMove(cell, dir, allyN)) {
                        c++;
                        p++;
                    }
                }
            }
            stateP[cell] = p;
        }
        return c;
    }

    List<BitBoard1D> getAllPossibleMoves(byte side) {
        List<BitBoard1D> res = new ArrayList<>();
        for (byte cell = 0; cell < state.length; cell++) {
            if (state[cell] != side) continue;
            for (byte dir = 0; dir < 6; dir++) {
                for (byte n = 1; n <= 3; n++) {
                    if (isValidMove(cell, dir, n)) {
                        BitBoard1D copy = new BitBoard1D(this);
                        copy.move(cell, dir, n);
                        res.add(copy);
                    }
                }
            }
        }
        return res;
    }


    BitBoard1D getBestMove() {
        int max = -7;
        int depth = 6;
        BitBoard1D best = null;
        for (BitBoard1D move : getAllPossibleMoves((byte) 1)) {
            int utility = min(move, -7, 7, depth - 1);
            if (max == -7 || utility > max) {
                max = utility;
                best = move;
            }
        }
        return best;
    }

    int min(BitBoard1D b, int alpha, int beta, int depth) {
        if (depth == 0) return b.white - b.black;
        int value = 7;
        for (BitBoard1D move : getAllPossibleMoves((byte) 10)) {
            int utility = max(move, alpha, beta, depth - 1);
            if (utility < value) value = utility;
            if (utility <= alpha) return utility;
            if (utility < beta) beta = utility;
        }
        return value;
    }

    int max(BitBoard1D b, int alpha, int beta, int depth) {
        if (depth == 0) return b.white - b.black;
        int value = -7;
        for (BitBoard1D move : getAllPossibleMoves((byte) 1)) {
            int utility = min(move, alpha, beta, depth - 1);
            if (utility > value) value = utility;
            if (utility >= beta) return utility;
            if (utility > alpha) alpha = utility;
        }
        return value;
    }

    public void printP() {
        print(true);
    }

    public void print() {
        print(false);
    }

    public void print(boolean isP) {
        StringBuilder sb = new StringBuilder();
        int spaces = 4, n = 0, m = 5;
        boolean decre = true;
        char x = 'I', y = '9';
        for (byte aState : (isP ? stateP : state)) {
            if (n == m) {
                sb.append('\n');
                n = 0;
                m += decre ? 1 : -1;
            }
            if (n == 0) {
                for (int i = 0; i < spaces; i++) {
                    sb.append(' ');
                }
                sb.append(x--).append(' ');
            }
            if (!isP) {
                char a = ' ';
                switch (aState) {
                    case 10:
                        a = 'O';
                        break;
                    case 0:
                        a = '+';
                        break;
                    case 1:
                        a = '@';
                        break;
                }
                sb.append(a).append(' ');
            } else {
                sb.append(aState).append(' ');
            }
            n++;
            if (n == m) {
                if (n != 9 && !decre) sb.append(y--);

                if (m == 9) decre = !decre;
                spaces += decre ? -1 : 1;
            }
        }
        sb.append("\n      1 2 3 4 5 \n");
        System.out.println(String.valueOf(sb));
    }

    public void init(UI.Square[][] squares) {
        this.squares = squares;
        for (int i = 0; i < state.length; i++) {
            int[] coordinates = ui[i];
            squares[coordinates[0]][coordinates[1]].id = i;
            squares[coordinates[0]][coordinates[1]].setOnMouseClicked(new Listener(i));
        }
    }

    public void show() {
        for (UI.Square[] row : squares) {
            for (UI.Square square : row) {
                if (square.id != -1) {
                    ImageView a;
                    if (state[square.id] == 1) a = new ImageView(new Image("white-ball.png"));
                    else if (state[square.id] == 10) a = new ImageView(new Image("black-ball.png"));
                    else a = new ImageView(new Image("empty.png"));
                    a.setFitWidth(80);
                    a.setFitHeight(80);
                    square.getChildren().clear();
                    square.getChildren().add(a);
                }
            }
        }
        System.out.println(white + " w/b " + black);
    }


    public class Listener implements EventHandler<MouseEvent> {
        byte id;

        public Listener(int id) {
            this.id = (byte) id;
        }

        @Override
        public void handle(MouseEvent event) {
            String[] choices = {"Left", "LeftUp", "RightUp", "Right", "RightDown", "LeftDown"};
            String direction = (String) JOptionPane.showInputDialog(null, "Direction",
                    "Direction", JOptionPane.QUESTION_MESSAGE, null,
                    choices,
                    choices[0]);
            byte d = 0;
            for (byte i = 0; i < choices.length; i++) {
                if (choices[i].equals(direction)) d = i;
            }
            choices = new String[]{"1", "2", "3"};

            String N = (String) JOptionPane.showInputDialog(null, "N",
                    "N", JOptionPane.QUESTION_MESSAGE, null,
                    choices,
                    choices[0]);
            byte n = 0;
            for (byte i = 0; i < choices.length; i++) {
                if (choices[i].equals(N)) n = i;
            }
            n++;
            if (isValidMove(id, d, n)) {
                byte moved = state[id];
                move(id, d, n);
                show();
                if (moved == 10) {
                    BitBoard1D next = getBestMove();
                    state = next.state;
                    white = next.white;
                    black = next.black;
                    show();
                }
            } else {
                System.out.println("Invalid, Cell: " + id + " direction: " + direction + " N: " + N);
            }
        }

    }
}
