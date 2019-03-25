package sylvartore;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import javax.swing.*;
import java.io.*;
import java.util.*;

public class BitBoard1D {

    public byte[] getStandardInitialLayout() {
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

    public byte[] getGermanDaisyLayout() {
        return new byte[]{
                0, 0, 0, 0, 0,
                10, 10, 0, 0, 1, 1,
                10, 10, 10, 0, 1, 1, 1,
                0, 10, 10, 0, 0, 1, 1, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 1, 1, 0, 0, 10, 10, 0,
                1, 1, 1, 0, 10, 10, 10,
                1, 1, 0, 0, 10, 10,
                0, 0, 0, 0, 0
        };
    }

    public byte[] getBelgianDaisyLayout() {
        return new byte[]{
                10, 10, 0, 1, 1,
                10, 10, 10, 1, 1, 1,
                0, 10, 10, 0, 1, 1, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 1, 1, 0, 10, 10, 0,
                1, 1, 1, 10, 10, 10,
                1, 1, 0, 10, 10
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
        state = new byte[61];//getStandardInitialLayout();
        stateP = new byte[61];
    }

    public BitBoard1D(BitBoard1D source) {
        white = source.white;
        black = source.black;
        state = new byte[61];
        System.arraycopy(source.state, 0, state, 0, source.state.length);
    }

    public static boolean tryMove(final byte cell, final byte d, final byte n, byte[] state) {
        if (state[cell] == 0) return false;
        byte targetCell = TransitionMatrix[cell][d];
        if (targetCell == -1) return false;
        if (n == 1) {
            if (state[targetCell] == 0) {
                state[targetCell] = state[cell];
                state[cell] = 0;
                return true;
            } else {
                byte force = 0;
                while (++force <= 3 && targetCell != -1 && state[targetCell] == state[cell]) {
                    targetCell = TransitionMatrix[targetCell][d];
                }
                if (force > 3 || targetCell == -1) return false;
                byte counterForce = 0;
                byte counter = state[targetCell];
                byte last = targetCell;
                if (counter != 0) {
                    while (targetCell != -1 && state[targetCell] == counter && ++counterForce <= 3) {
                        targetCell = TransitionMatrix[targetCell][d];
                        if (targetCell != -1) last = targetCell;
                    }
                    if (targetCell != -1) {
                        if (state[targetCell] == counter) {
                            targetCell = TransitionMatrix[targetCell][d];
                            if (targetCell != -1) last = targetCell;
                        }
                    }
                }
                if (force > counterForce && (targetCell == -1 || state[targetCell] != state[cell])) {
                    targetCell = last;
                    byte next = TransitionMatrix[targetCell][CounterDirection[d]];
                    byte to = targetCell;
                    while ((targetCell == to || state[targetCell] != 0)
                            && (targetCell != cell)) {
//                        if (TransitionMatrix[targetCell][d] == -1) {
//                            if (state[targetCell] == 1) black++;
//                            if (state[targetCell] == 10) white++;
//                        }
                        state[targetCell] = state[next];
                        targetCell = next;
                        next = TransitionMatrix[targetCell][CounterDirection[d]];
                    }
                    state[cell] = 0;
                    return true;
                }
                return false;
            }
        } else {
            if (state[targetCell] != 0) return false;
            byte ssd = d;
            if (++ssd == 6) ssd = 0;
            if (ssss(cell, d, n, ssd, state)) return true;
            if (++ssd == 6) ssd = 0;
            return ssss(cell, d, n, ssd, state);
        }
    }

    public static final byte[] table = new byte[]{55, 49, 42, 34, 25, 16, 8, 1, -5};

    public static byte standardNotationToCell(String str) {
        char[] ca = str.toLowerCase().toCharArray();
        if (ca[0] < 'a' || ca[0] > 'i' || ca[1] < '1' || ca[1] > '9') return -1;
        int a = ca[0] - 'a';
        int b = ca[1] - '0';
        return (byte) (table[a] + b);
    }

    public void readState(String str) {
        char[] ca = str.toLowerCase().toCharArray();
        if (ca[0] < 'a' || ca[0] > 'i' || ca[1] < '1' || ca[1] > '9' || ca[2] != 'w' && ca[2] != 'b') return;
        state[table[ca[0] - 'a'] + ca[1] - '0'] = ca[2] == 'w' ? (byte) 1 : (byte) 10;
    }

    public boolean isValidMove(byte cell, byte d, byte n) {
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

    public static boolean ssss(byte cell, byte d, byte n, byte ssd, byte[] state) {
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

    public byte printTable(byte cell, byte d) {
        byte ssd = d;
        if (++ssd == 6) ssd = 0;
        if (++ssd == 6) ssd = 0;
        return TransitionMatrix[cell][ssd];
    }

    public boolean canInline(byte cell, byte targetCell, byte d) {
        byte force = 0;
        while (++force <= 3 && targetCell != -1 && state[targetCell] == state[cell]) {
            targetCell = TransitionMatrix[targetCell][d];
        }
        if (force > 3 || targetCell == -1) return false;
        int[] counterForce = getForce(targetCell, d);
        return force > counterForce[1] && (counterForce[0] == -1 || state[counterForce[0]] != state[cell]);
    }

    public int[] getForce(byte cell, byte d) {
        if (state[cell] == 0) return new int[]{cell, 0};
        byte force = 0;
        byte targetCell = cell;
        while (targetCell != -1 && state[targetCell] == state[cell] && ++force <= 3) {
            targetCell = TransitionMatrix[targetCell][d];
        }
        if (targetCell != -1) {
            if (state[targetCell] == state[cell]) {
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

    public boolean tableSideStepMove(byte cell, byte d, byte n) {
        if (state[cell] == 0) return false;
        byte targetCell = TransitionMatrix[cell][d];
        if (targetCell == -1) return false;
        if (n == 1) {
            if (state[targetCell] == 0) return true;
            return canInline(cell, targetCell, d);
        } else {
            if (state[targetCell] != 0) return false;
            if (n == 2) {
                byte ally = ally1[cell][d];
                if (ally != -1 && state[ally] == state[cell] && TransitionMatrix[ally][d] != -1 && state[TransitionMatrix[ally][d]] == 0)
                    return true;
                ally = ally3[cell][d];
                return ally != -1 && state[ally] == state[cell] && TransitionMatrix[ally][d] != -1 && state[TransitionMatrix[ally][d]] == 0;
            } else {
                byte ally = ally1[cell][d];
                byte ally_2 = ally2[cell][d];
                if (ally != -1 && state[ally] == state[cell] && TransitionMatrix[ally][d] != -1 && state[TransitionMatrix[ally][d]] == 0 &&
                        ally_2 != -1 && state[ally_2] == state[cell] && TransitionMatrix[ally_2][d] != -1 && state[TransitionMatrix[ally_2][d]] == 0)
                    return true;
                ally = ally3[cell][d];
                ally_2 = ally4[cell][d];
                return (ally != -1 && state[ally] == state[cell] && TransitionMatrix[ally][d] != -1 && state[TransitionMatrix[ally][d]] == 0 &&
                        ally_2 != -1 && state[ally_2] == state[cell] && TransitionMatrix[ally_2][d] != -1 && state[TransitionMatrix[ally_2][d]] == 0);
            }
        }
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

    List<byte[]> getAllPossibleMove_in_one(byte side) {
        List<byte[]> res = new ArrayList<>();
        for (byte cell = 0; cell < state.length; cell++) {
            if (state[cell] != side) continue;
            for (byte dir = 0; dir < 6; dir++) {
                for (byte n = 1; n <= 3; n++) {
                    byte[] copy = new byte[state.length];
                    System.arraycopy(state, 0, copy, 0, state.length);
                    if (BitBoard1D.tryMove(cell, dir, n, copy)) {
                        res.add(copy);
                    }
                }
            }
        }
        return res;
    }

    BitBoard1D getBestMove() {
        int max = Integer.MIN_VALUE;
        int depth = 5;
        BitBoard1D best = null;
        for (BitBoard1D move : getAllPossibleMoves((byte) 1)) {
            int utility = min(move, Integer.MIN_VALUE, Integer.MAX_VALUE, depth - 1);
            if (best == null || utility > max) {
                max = utility;
                best = move;
            }
        }
        return best;
    }

    int min(BitBoard1D b, int alpha, int beta, int depth) {
        if (depth == 0 || b.white == 6 || b.black == 6) return b.white - b.black;
        int value = Integer.MAX_VALUE;
        for (BitBoard1D move : b.getAllPossibleMoves((byte) 10)) {
            int utility = max(move, alpha, beta, depth - 1);
            if (utility < value) value = utility;
            if (utility <= alpha) return utility;
            if (utility < beta) beta = utility;
        }
        return value;
    }

    int max(BitBoard1D b, int alpha, int beta, int depth) {
        if (depth == 0 || b.white == 6 || b.black == 6) return b.white - b.black;
        int value = Integer.MIN_VALUE;
        for (BitBoard1D move : b.getAllPossibleMoves((byte) 1)) {
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
                        a = '@';
                        break;
                    case 0:
                        a = '+';
                        break;
                    case 1:
                        a = 'O';
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

    public String stateToString() {
        List<String> note = new ArrayList<>();
        for (byte i = 0; i < state.length; i++) {
            if (state[i] != 0) {
                String sideStr = state[i] == 1 ? "w" : "b";
                note.add(toStandardNotation[i] + sideStr);
            }
        }
        note.sort((a, b) -> {
            if (a.charAt(2) == 'b' && b.charAt(2) == 'w') return -1;
            if (a.charAt(2) == 'w' && b.charAt(2) == 'b') return 1;
            return a.compareTo(b);
        });
        return String.join(",", note);
    }

    public static String randomStateGenerator() {
        Random rand = new Random();
        StringBuilder sb = new StringBuilder();
        int randomSide = rand.nextInt(2);
        if (randomSide == 0) sb.append('w');
        else sb.append('b');
        sb.append('\n');
        int black = rand.nextInt(8) + 7;
        int write = rand.nextInt(8) + 7;
        BitBoard1D b = new BitBoard1D();
        for (int i = 0; i < b.state.length && black > 0 && write > 0; i++) {
            randomSide = rand.nextInt(4);
            if (randomSide > 1) {
                if (randomSide == 3) {
                    b.state[i] = (byte) 1;
                    write--;
                } else {
                    b.state[i] = (byte) 10;
                    black--;
                }
            }
        }
        sb.append(b.stateToString());
        return String.valueOf(sb);
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
    }

    public void readLayout(int n) {
        try {
            FileReader fr = new FileReader("src/test_input/Test" + n + ".input");
            BufferedReader br = new BufferedReader(fr);
            String line;
            int side = 10;
            for (int i = 1; (line = br.readLine()) != null; i++) {
                if (i == 1) {
                    if (line.toLowerCase().charAt(0) == 'w') side = 1;
                } else {
                    String[] states = line.split(",");
                    for (String state : states) {
                        readState(state.trim());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void test(int n) {
        FileReader fr = null;
        BufferedReader br = null;
        FileWriter fw = null;
        BufferedWriter bw = null;
        final String path = ".";//"src/test_input";
        try {
            fr = new FileReader(path + "/Test" + n + ".input");
            br = new BufferedReader(fr);
            String line;
            int side = 10;
            for (int i = 1; (line = br.readLine()) != null; i++) {
                if (i == 1) {
                    if (line.toLowerCase().charAt(0) == 'w') side = 1;
                } else {
                    String[] states = line.split(",");
                    for (String state : states) {
                        readState(state.trim());
                    }
                }
            }
            Set<String> ans = new TreeSet<>();
            List<BitBoard1D> moves = getAllPossibleMoves((byte) side);
            for (BitBoard1D move : moves) {
                ans.add(move.stateToString());
            }
            fw = new FileWriter(path + "/Test" + n + ".board");
            bw = new BufferedWriter(fw);
            for (String s : ans) {
                bw.write(s + "\n");
            }
//            br = new BufferedReader(new FileReader("src/test_input/Test" + n + ".board"));
//            int count = 0;
//            while ((line = br.readLine()) != null) {
//                if (ans.contains(line)) System.out.println("Test " + n + " Failed: " + line);
//                count++;
//            }
//            if (ans.size() == count) System.out.println("Test " + n + " Passed!");
//            else System.out.println("Test " + n + " Failed: redundant answers");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (br != null) br.close();
                if (fr != null) fr.close();
                if (bw != null) bw.close();
                if (fw != null) fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
                System.out.println("Score: " + white + " W/B " + black);
                if (moved == 10) {
                    long start = System.currentTimeMillis();
                    BitBoard1D next = getBestMove();
                    long end = System.currentTimeMillis();
                    System.out.println("Used: " + (double) (end - start) / 1000);
                    state = next.state;
                    white = next.white;
                    black = next.black;
                    show();
                    System.out.println("Score: " + white + " W/B " + black);
                }
            } else {
                System.out.println("Invalid, Cell: " + id + " direction: " + direction + " N: " + N);
            }
        }

    }

    public static final byte[][] ally1 = new byte[][]{
            {-1, -1, 1, 6, 5, -1,},
            {-1, -1, 2, 7, 6, 0,},
            {-1, -1, 3, 8, 7, 1,},
            {-1, -1, 4, 9, 8, 2,},
            {-1, -1, -1, 10, 9, 3,},
            {-1, 0, 6, 12, 11, -1,},
            {0, 1, 7, 13, 12, 5,},
            {1, 2, 8, 14, 13, 6,},
            {2, 3, 9, 15, 14, 7,},
            {3, 4, 10, 16, 15, 8,},
            {4, -1, -1, 17, 16, 9,},
            {-1, 5, 12, 19, 18, -1,},
            {5, 6, 13, 20, 19, 11,},
            {6, 7, 14, 21, 20, 12,},
            {7, 8, 15, 22, 21, 13,},
            {8, 9, 16, 23, 22, 14,},
            {9, 10, 17, 24, 23, 15,},
            {10, -1, -1, 25, 24, 16,},
            {-1, 11, 19, 27, 26, -1,},
            {11, 12, 20, 28, 27, 18,},
            {12, 13, 21, 29, 28, 19,},
            {13, 14, 22, 30, 29, 20,},
            {14, 15, 23, 31, 30, 21,},
            {15, 16, 24, 32, 31, 22,},
            {16, 17, 25, 33, 32, 23,},
            {17, -1, -1, 34, 33, 24,},
            {-1, 18, 27, 35, -1, -1,},
            {18, 19, 28, 36, 35, 26,},
            {19, 20, 29, 37, 36, 27,},
            {20, 21, 30, 38, 37, 28,},
            {21, 22, 31, 39, 38, 29,},
            {22, 23, 32, 40, 39, 30,},
            {23, 24, 33, 41, 40, 31,},
            {24, 25, 34, 42, 41, 32,},
            {25, -1, -1, -1, 42, 33,},
            {26, 27, 36, 43, -1, -1,},
            {27, 28, 37, 44, 43, 35,},
            {28, 29, 38, 45, 44, 36,},
            {29, 30, 39, 46, 45, 37,},
            {30, 31, 40, 47, 46, 38,},
            {31, 32, 41, 48, 47, 39,},
            {32, 33, 42, 49, 48, 40,},
            {33, 34, -1, -1, 49, 41,},
            {35, 36, 44, 50, -1, -1,},
            {36, 37, 45, 51, 50, 43,},
            {37, 38, 46, 52, 51, 44,},
            {38, 39, 47, 53, 52, 45,},
            {39, 40, 48, 54, 53, 46,},
            {40, 41, 49, 55, 54, 47,},
            {41, 42, -1, -1, 55, 48,},
            {43, 44, 51, 56, -1, -1,},
            {44, 45, 52, 57, 56, 50,},
            {45, 46, 53, 58, 57, 51,},
            {46, 47, 54, 59, 58, 52,},
            {47, 48, 55, 60, 59, 53,},
            {48, 49, -1, -1, 60, 54,},
            {50, 51, 57, -1, -1, -1,},
            {51, 52, 58, -1, -1, 56,},
            {52, 53, 59, -1, -1, 57,},
            {53, 54, 60, -1, -1, 58,},
            {54, 55, -1, -1, -1, 59,},
    };

    public static final byte[][] ally2 = new byte[][]{
            {-1, -1, 2, 13, 11, -1,},
            {-1, -1, 3, 14, 12, -1,},
            {-1, -1, 4, 15, 13, 0,},
            {-1, -1, -1, 16, 14, 1,},
            {-1, -1, -1, 17, 15, 2,},
            {-1, -1, 7, 20, 18, -1,},
            {-1, -1, 8, 21, 19, -1,},
            {-1, -1, 9, 22, 20, 5,},
            {-1, -1, 10, 23, 21, 6,},
            {-1, -1, -1, 24, 22, 7,},
            {-1, -1, -1, 25, 23, 8,},
            {-1, 0, 13, 28, 26, -1,},
            {-1, 1, 14, 29, 27, -1,},
            {0, 2, 15, 30, 28, 11,},
            {1, 3, 16, 31, 29, 12,},
            {2, 4, 17, 32, 30, 13,},
            {3, -1, -1, 33, 31, 14,},
            {4, -1, -1, 34, 32, 15,},
            {-1, 5, 20, 36, -1, -1,},
            {-1, 6, 21, 37, 35, -1,},
            {5, 7, 22, 38, 36, 18,},
            {6, 8, 23, 39, 37, 19,},
            {7, 9, 24, 40, 38, 20,},
            {8, 10, 25, 41, 39, 21,},
            {9, -1, -1, 42, 40, 22,},
            {10, -1, -1, -1, 41, 23,},
            {-1, 11, 28, 43, -1, -1,},
            {-1, 12, 29, 44, -1, -1,},
            {11, 13, 30, 45, 43, 26,},
            {12, 14, 31, 46, 44, 27,},
            {13, 15, 32, 47, 45, 28,},
            {14, 16, 33, 48, 46, 29,},
            {15, 17, 34, 49, 47, 30,},
            {16, -1, -1, -1, 48, 31,},
            {17, -1, -1, -1, 49, 32,},
            {-1, 19, 37, 50, -1, -1,},
            {18, 20, 38, 51, -1, -1,},
            {19, 21, 39, 52, 50, 35,},
            {20, 22, 40, 53, 51, 36,},
            {21, 23, 41, 54, 52, 37,},
            {22, 24, 42, 55, 53, 38,},
            {23, 25, -1, -1, 54, 39,},
            {24, -1, -1, -1, 55, 40,},
            {26, 28, 45, 56, -1, -1,},
            {27, 29, 46, 57, -1, -1,},
            {28, 30, 47, 58, 56, 43,},
            {29, 31, 48, 59, 57, 44,},
            {30, 32, 49, 60, 58, 45,},
            {31, 33, -1, -1, 59, 46,},
            {32, 34, -1, -1, 60, 47,},
            {35, 37, 52, -1, -1, -1,},
            {36, 38, 53, -1, -1, -1,},
            {37, 39, 54, -1, -1, 50,},
            {38, 40, 55, -1, -1, 51,},
            {39, 41, -1, -1, -1, 52,},
            {40, 42, -1, -1, -1, 53,},
            {43, 45, 58, -1, -1, -1,},
            {44, 46, 59, -1, -1, -1,},
            {45, 47, 60, -1, -1, 56,},
            {46, 48, -1, -1, -1, 57,},
            {47, 49, -1, -1, -1, 58,},
    };

    public static final byte[][] ally3 = new byte[][]{
            {-1, 1, 6, 5, -1, -1,},
            {-1, 2, 7, 6, 0, -1,},
            {-1, 3, 8, 7, 1, -1,},
            {-1, 4, 9, 8, 2, -1,},
            {-1, -1, 10, 9, 3, -1,},
            {0, 6, 12, 11, -1, -1,},
            {1, 7, 13, 12, 5, 0,},
            {2, 8, 14, 13, 6, 1,},
            {3, 9, 15, 14, 7, 2,},
            {4, 10, 16, 15, 8, 3,},
            {-1, -1, 17, 16, 9, 4,},
            {5, 12, 19, 18, -1, -1,},
            {6, 13, 20, 19, 11, 5,},
            {7, 14, 21, 20, 12, 6,},
            {8, 15, 22, 21, 13, 7,},
            {9, 16, 23, 22, 14, 8,},
            {10, 17, 24, 23, 15, 9,},
            {-1, -1, 25, 24, 16, 10,},
            {11, 19, 27, 26, -1, -1,},
            {12, 20, 28, 27, 18, 11,},
            {13, 21, 29, 28, 19, 12,},
            {14, 22, 30, 29, 20, 13,},
            {15, 23, 31, 30, 21, 14,},
            {16, 24, 32, 31, 22, 15,},
            {17, 25, 33, 32, 23, 16,},
            {-1, -1, 34, 33, 24, 17,},
            {18, 27, 35, -1, -1, -1,},
            {19, 28, 36, 35, 26, 18,},
            {20, 29, 37, 36, 27, 19,},
            {21, 30, 38, 37, 28, 20,},
            {22, 31, 39, 38, 29, 21,},
            {23, 32, 40, 39, 30, 22,},
            {24, 33, 41, 40, 31, 23,},
            {25, 34, 42, 41, 32, 24,},
            {-1, -1, -1, 42, 33, 25,},
            {27, 36, 43, -1, -1, 26,},
            {28, 37, 44, 43, 35, 27,},
            {29, 38, 45, 44, 36, 28,},
            {30, 39, 46, 45, 37, 29,},
            {31, 40, 47, 46, 38, 30,},
            {32, 41, 48, 47, 39, 31,},
            {33, 42, 49, 48, 40, 32,},
            {34, -1, -1, 49, 41, 33,},
            {36, 44, 50, -1, -1, 35,},
            {37, 45, 51, 50, 43, 36,},
            {38, 46, 52, 51, 44, 37,},
            {39, 47, 53, 52, 45, 38,},
            {40, 48, 54, 53, 46, 39,},
            {41, 49, 55, 54, 47, 40,},
            {42, -1, -1, 55, 48, 41,},
            {44, 51, 56, -1, -1, 43,},
            {45, 52, 57, 56, 50, 44,},
            {46, 53, 58, 57, 51, 45,},
            {47, 54, 59, 58, 52, 46,},
            {48, 55, 60, 59, 53, 47,},
            {49, -1, -1, 60, 54, 48,},
            {51, 57, -1, -1, -1, 50,},
            {52, 58, -1, -1, 56, 51,},
            {53, 59, -1, -1, 57, 52,},
            {54, 60, -1, -1, 58, 53,},
            {55, -1, -1, -1, 59, 54,},
    };

    public static final byte[][] ally4 = new byte[][]{
            {-1, 2, 13, 11, -1, -1,},
            {-1, 3, 14, 12, -1, -1,},
            {-1, 4, 15, 13, 0, -1,},
            {-1, -1, 16, 14, 1, -1,},
            {-1, -1, 17, 15, 2, -1,},
            {-1, 7, 20, 18, -1, -1,},
            {-1, 8, 21, 19, -1, -1,},
            {-1, 9, 22, 20, 5, -1,},
            {-1, 10, 23, 21, 6, -1,},
            {-1, -1, 24, 22, 7, -1,},
            {-1, -1, 25, 23, 8, -1,},
            {0, 13, 28, 26, -1, -1,},
            {1, 14, 29, 27, -1, -1,},
            {2, 15, 30, 28, 11, 0,},
            {3, 16, 31, 29, 12, 1,},
            {4, 17, 32, 30, 13, 2,},
            {-1, -1, 33, 31, 14, 3,},
            {-1, -1, 34, 32, 15, 4,},
            {5, 20, 36, -1, -1, -1,},
            {6, 21, 37, 35, -1, -1,},
            {7, 22, 38, 36, 18, 5,},
            {8, 23, 39, 37, 19, 6,},
            {9, 24, 40, 38, 20, 7,},
            {10, 25, 41, 39, 21, 8,},
            {-1, -1, 42, 40, 22, 9,},
            {-1, -1, -1, 41, 23, 10,},
            {11, 28, 43, -1, -1, -1,},
            {12, 29, 44, -1, -1, -1,},
            {13, 30, 45, 43, 26, 11,},
            {14, 31, 46, 44, 27, 12,},
            {15, 32, 47, 45, 28, 13,},
            {16, 33, 48, 46, 29, 14,},
            {17, 34, 49, 47, 30, 15,},
            {-1, -1, -1, 48, 31, 16,},
            {-1, -1, -1, 49, 32, 17,},
            {19, 37, 50, -1, -1, -1,},
            {20, 38, 51, -1, -1, 18,},
            {21, 39, 52, 50, 35, 19,},
            {22, 40, 53, 51, 36, 20,},
            {23, 41, 54, 52, 37, 21,},
            {24, 42, 55, 53, 38, 22,},
            {25, -1, -1, 54, 39, 23,},
            {-1, -1, -1, 55, 40, 24,},
            {28, 45, 56, -1, -1, 26,},
            {29, 46, 57, -1, -1, 27,},
            {30, 47, 58, 56, 43, 28,},
            {31, 48, 59, 57, 44, 29,},
            {32, 49, 60, 58, 45, 30,},
            {33, -1, -1, 59, 46, 31,},
            {34, -1, -1, 60, 47, 32,},
            {37, 52, -1, -1, -1, 35,},
            {38, 53, -1, -1, -1, 36,},
            {39, 54, -1, -1, 50, 37,},
            {40, 55, -1, -1, 51, 38,},
            {41, -1, -1, -1, 52, 39,},
            {42, -1, -1, -1, 53, 40,},
            {45, 58, -1, -1, -1, 43,},
            {46, 59, -1, -1, -1, 44,},
            {47, 60, -1, -1, 56, 45,},
            {48, -1, -1, -1, 57, 46,},
            {49, -1, -1, -1, 58, 47,},
    };
}
