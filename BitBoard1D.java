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
    public static final String[] directionToString = new String[]{"Left", "LeftUp", "RightUp", "Right", "RightDown", "LeftDown"};

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

    public int isValidMove(byte cell, byte d, byte n) {
        if (state[cell] == 0) return -1;
        byte targetCell = TransitionMatrix[cell][d];
        if (targetCell == -1) return -1;
        if (n == 1) {
            if (state[targetCell] == 0) return 8;
            return canInline(cell, targetCell, d);
        } else {
            if (state[targetCell] != 0) return -1;
            byte ssd = d;
            if (++ssd == 6) ssd = 0;
            if (canSideStep(cell, d, n, ssd)) return n == 3 ? 6 : 7;
            if (++ssd == 6) ssd = 0;
            if (canSideStep(cell, d, n, ssd)) return n == 3 ? 6 : 7;
            return -1;
        }
    }

    public byte printTable(byte cell, byte d) {
        byte ssd = d;
        if (++ssd == 6) ssd = 0;
        if (++ssd == 6) ssd = 0;
        return TransitionMatrix[cell][ssd];
    }

    public int canInline(byte cell, byte targetCell, byte d) {
        byte force = 0;
        while (++force <= 3 && targetCell != -1 && state[targetCell] == state[cell]) {
            targetCell = TransitionMatrix[targetCell][d];
        }
        if (force > 3 || targetCell == -1) return -1;
        int[] counterForce = getForce(targetCell, d);
        if (!(force > counterForce[1] && (counterForce[0] == -1 || state[counterForce[0]] != state[cell]))) return -1;
        if (counterForce[0] == -1) return (force == 3) ? 0 : 1; // capturing
        if (counterForce[1] == 0) return (force == 3) ? 4 : 5; // inline
        return (force == 3) ? 2 : 3; // attacking
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

    int countPossibleMoves() {
        int c = 0;
        for (byte cell = 0; cell < state.length; cell++) {
            byte p = 0;
            for (byte dir = 0; dir < 6; dir++) {
                for (byte allyN = 1; allyN <= 3; allyN++) {
                    if (isValidMove(cell, dir, allyN)!=-1) {
                        c++;
                        p++;
                    }
                }
            }
            stateP[cell] = p;
        }
        return c;
    }

//    List<BitBoard1D> getAllPossibleNextState(byte side) {
//        List<BitBoard1D> res = new ArrayList<>();
//        for (byte cell = 0; cell < state.length; cell++) {
//            if (state[cell] != side) continue;
//            for (byte dir = 0; dir < 6; dir++) {
//                for (byte n = 1; n <= 3; n++) {
//                    if (isValidMove(cell, dir, n)) {
//                        BitBoard1D copy = new BitBoard1D(this);
//                        copy.move(cell, dir, n);
//                        res.add(copy);
//                    }
//                }
//            }
//        }
//        return res;
//    }

    List<byte[]> getAllPossibleMoves(byte side) {
        List<byte[]> res = new ArrayList<>();
        for (byte cell = 0; cell < state.length; cell++) {
            if (state[cell] != side) continue;
            for (byte dir = 0; dir < 6; dir++) {
                for (byte n = 1; n <= 3; n++) {
                    int order = isValidMove(cell, dir, n);
                    if (order != -1) {
                        res.add(new byte[]{cell, dir, n, (byte)order});
                    }
                }
            }
        }
        res.sort(Comparator.comparingInt(move -> move[3]));
        return res;
    }

    static int c = 0;

    BitBoard1D getBestMove() {
        int max = Integer.MIN_VALUE;
        int depth = 4;
        c = 0;
        BitBoard1D best = null;
        for (byte[] move : getAllPossibleMoves((byte) 1)) {
            BitBoard1D copy = new BitBoard1D(this);
            copy.move(move[0], move[1], move[2]);
            int utility = min(copy, Integer.MIN_VALUE, Integer.MAX_VALUE, depth - 1);
            if (best == null || utility > max) {
                max = utility;
                best = copy;
            }
        }
        System.out.println("node: " + c);
        System.out.println("best: " + max);
        return best;
    }

    static byte[] central_weight = {
            1, 1, 1, 1, 1,
            1, 2, 2, 2, 2, 1,
            1, 2, 3, 3, 3, 2, 1,
            1, 2, 3, 4, 4, 3, 2, 1,
            1, 2, 3, 4, 5, 4, 3, 2, 1,
            1, 2, 3, 4, 4, 3, 2, 1,
            1, 2, 3, 3, 3, 2, 1,
            1, 2, 2, 2, 2, 1,
            1, 1, 1, 1, 1,
    };

    int heuristic() {
        int score = 0;
        for (int i = 0; i < state.length; i++) {
            if (state[i] == 0) continue;
            if (state[i] == 1) {
                score += central_weight[i];
            } else {
                score -= central_weight[i];
            }
        }
        return score + (white - black) * 50;
    }

    int min(BitBoard1D b, int alpha, int beta, int depth) {
        if (depth == 0 || b.white == 6 || b.black == 6) {
            c++;
            return b.heuristic();
        }
        int value = Integer.MAX_VALUE;
        for (byte[] move : b.getAllPossibleMoves((byte) 10)) {
            BitBoard1D copy = new BitBoard1D(b);
            copy.move(move[0], move[1], move[2]);
            int utility = max(copy, alpha, beta, depth - 1);
            if (utility < value) value = utility;
            if (utility <= alpha) return utility;
            if (utility < beta) beta = utility;
        }
        return value;
    }

    int max(BitBoard1D b, int alpha, int beta, int depth) {
        if (depth == 0 || b.white == 6 || b.black == 6) {
            c++;
            return b.heuristic();
        }
        int value = Integer.MIN_VALUE;
        for (byte[] move : b.getAllPossibleMoves((byte) 1)) {
            BitBoard1D copy = new BitBoard1D(b);
            copy.move(move[0], move[1], move[2]);
            int utility = min(copy, alpha, beta, depth - 1);
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
            for (int i = 1; (line = br.readLine()) != null && i <= 2; i++) {
                if (i == 1) {
                    if (line.toLowerCase().charAt(0) == 'w') side = 1;
                } else {
                    String[] states = line.split(",");
                    for (String state : states) {
                        readState(state.trim());
                    }
                }
            }
            Set<String> board = new TreeSet<>();
            Set<String> move = new TreeSet<>();
            for (byte cell = 0; cell < state.length; cell++) {
                if (state[cell] != side) continue;
                for (byte dir = 0; dir < 6; dir++) {
                    for (byte N = 1; N <= 3; N++) {
                        if (isValidMove(cell, dir, N) != -1) {
                            String s = toStandardNotation[cell] + " " + directionToString[dir] + " " + N;
                            move.add(s);
                            BitBoard1D copy = new BitBoard1D(this);
                            copy.move(cell, dir, N);
                            board.add(copy.stateToString());
                        }
                    }
                }
            }
            fw = new FileWriter(path + "/Test" + n + ".board");
            bw = new BufferedWriter(fw);
            for (String s : board) {
                bw.write(s + "\n");
            }
            bw.close();
            fw.close();
            fw = new FileWriter(path + "/Test" + n + ".move");
            bw = new BufferedWriter(fw);
            for (String s : move) {
                bw.write(s + "\n");
            }
            bw.close();
            fw.close();
//            br = new BufferedReader(new FileReader("src/test_input/Test" + n + ".board"));
//            int count = 0;
//            while ((line = br.readLine()) != null) {
//                if (board.contains(line)) System.out.println("Test " + n + " Failed: " + line);
//                count++;
//            }
//            if (board.size() == count) System.out.println("Test " + n + " Passed!");
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
            if (isValidMove(id, d, n) != -1) {
                byte moved = state[id];
                move(id, d, n);
                show();
                if (moved == 10) {
                    long start = System.currentTimeMillis();
                    BitBoard1D next = getBestMove();
                    long end = System.currentTimeMillis();
                    System.out.println("Used: " + (double) (end - start) / 1000);
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
