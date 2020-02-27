//
// Created by Sylvartore on 3/5/2019.
//
package sylvartore;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.*;
import java.util.ArrayList;

public class Game {
    byte state[];
    int turnLeft;
    Game prev;
    int aiTime;
    double total;
    double total2;
    AI ai;
    AI counter;
    byte humanSide;
    boolean gameOver;
    UI ui;
    ArrayList<String> log;
    long aiFinished;

    public Game() {
        state = new byte[61];
        humanSide = 0;
        total = 0;
        total2 = 0;
        turnLeft = 80;
        aiTime = 10000;
        prev = null;
        log = new ArrayList<>();
        ai = new AI((byte) 1);
        counter = new AI((byte) -1);
        gameOver = false;
    }

    public void reset() {
        humanSide = 0;
        total = 0;
        total2 = 0;
        aiTime = 10000;
        turnLeft = 80;
        prev = null;
        gameOver = false;
        update();
        log.clear();
    }

    public Game(Game source) {
        prev = source.prev;
        humanSide = source.humanSide;
        total = source.total;
        total2 = source.total2;
        aiTime = source.aiTime;
        turnLeft = source.turnLeft;
        state = new byte[61];
        System.arraycopy(source.state, 0, state, 0, source.state.length);
        gameOver = source.gameOver;
        ai = source.ai;
        counter = source.counter;
        log = source.log;
    }

    public static byte isValidMove(byte cell, byte d, byte n, byte[] state) {
        if (state[cell] == 0) return (byte) -1;
        byte targetCell = TransitionMatrix[cell][d];
        if (targetCell == -1) return -1;
        if (n == 1) {
            if (state[targetCell] == 0) return 8;
            return canInline(cell, targetCell, d, state);
        } else {
            if (state[targetCell] != 0) return -1;
            byte ssd = d;
            if (++ssd == 6) ssd = 0;
            if (canSideStep(cell, d, n, ssd, state)) return n == 3 ? (byte) 3 : (byte) 7;
            if (++ssd == 6) ssd = 0;
            if (canSideStep(cell, d, n, ssd, state)) return n == 3 ? (byte) 3 : (byte) 7;
            return -1;
        }
    }

    public static int[] getForce(byte cell, byte d, byte[] state) {
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

    public static boolean canSideStep(byte cell, byte d, byte n, byte ssd, byte[] state) {
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

    public static byte canInline(byte cell, byte targetCell, byte d, byte[] state) {
        byte force = 0;
        while (++force <= 3 && targetCell != -1 && state[targetCell] == state[cell]) {
            targetCell = TransitionMatrix[targetCell][d];
        }
        if (force > 3 || targetCell == -1) return (byte) -1;
        int[] counterForce = getForce(targetCell, d, state);
        if (!(force > counterForce[1] && (counterForce[0] == -1 || state[counterForce[0]] != state[cell]))) return -1;
        if (counterForce[0] == -1) return (force == 3) ? 0 : (byte) 4; // capturing
        if (counterForce[1] == 0) return (force == 3) ? (byte) 2 : (byte) 6; // inline
        return (force == 3) ? (byte) 1 : (byte) 5; // attacking
    }

    public static void move(byte cell, byte d, byte n, byte[] state) {
        byte targetCell = TransitionMatrix[cell][d];
        if (n == 1) {
            if (state[targetCell] == 0) {
                state[targetCell] = state[cell];
                state[cell] = 0;
            } else inlineMove(cell, targetCell, d, state);
        } else {
            byte ssd = d;
            if (++ssd == 6) ssd = 0;
            if (sideStepMove(cell, d, n, ssd, targetCell, state)) return;
            if (++ssd == 6) ssd = 0;
            sideStepMove(cell, d, n, ssd, targetCell, state);
        }
    }

    public static void inlineMove(byte cell, byte prev, byte d, byte[] state) {
        while (prev != -1 && TransitionMatrix[prev][d] != -1 && state[prev] != 0) {
            prev = TransitionMatrix[prev][d];
        }
        if (state[prev] != 0 && TransitionMatrix[prev][d] != -1) prev = TransitionMatrix[prev][d];
        byte next = TransitionMatrix[prev][CounterDirection[d]];
        byte to = prev;
        while ((prev == to || state[prev] != 0)
                && (prev != cell)) {
            state[prev] = state[next];
            prev = next;
            next = TransitionMatrix[prev][CounterDirection[d]];
        }
        state[cell] = 0;
    }

    public static boolean sideStepMove(byte cell, byte d, byte n, byte ssd, byte targetCell, byte[] state) {
        byte ally1Cell = TransitionMatrix[cell][ssd];
        if (ally1Cell == -1 || state[cell] != state[ally1Cell]) return false;
        byte ally1SsdCell = TransitionMatrix[ally1Cell][d];
        if (ally1SsdCell == -1 || state[ally1SsdCell] != 0) return false;
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

    public static byte[] toUiMove(byte cell, byte d, byte n, byte[] state) {
        if (n == 1) {
            byte targetCell = TransitionMatrix[cell][d];
            if (state[targetCell] == 0) {
                return new byte[]{cell, d};
            } else {
                byte furtherCell = TransitionMatrix[targetCell][d];
                if (furtherCell != -1 && state[furtherCell] == state[cell]) {
                    return new byte[]{cell, furtherCell, d};
                }
                return new byte[]{cell, targetCell, d};
            }
        } else {
            byte ssd = d;
            if (++ssd == 6) ssd = 0;
            byte allyCell = TransitionMatrix[cell][ssd];
            byte allyTargetCell = TransitionMatrix[allyCell][d];
            if (state[allyCell] == state[cell] && allyTargetCell != -1 && state[allyTargetCell] == 0) {
                if (n == 2) return new byte[]{cell, allyCell, d};
                else {
                    byte ally2Cell = TransitionMatrix[allyCell][ssd];
                    byte ally2TargetCell = TransitionMatrix[ally2Cell][d];
                    if (state[ally2Cell] == state[cell] && ally2TargetCell != -1 && state[ally2TargetCell] == 0) {
                        return new byte[]{cell, ally2Cell, d};
                    }
                }
            }
            if (++ssd == 6) ssd = 0;
            allyCell = TransitionMatrix[cell][ssd];
            if (n == 2) return new byte[]{cell, allyCell, d};
            return new byte[]{cell, TransitionMatrix[allyCell][ssd], d};
        }
    }

    void init(UI ui) {
        this.ui = ui;
        state = getStandardInitialLayout();
        for (int i = 0; i < state.length; i++) {
            int[] coordinates = UI_GRID[i];
            ui.squares[coordinates[0] + 1][coordinates[1] + 1].id = i;
            ui.squares[coordinates[0] + 1][coordinates[1] + 1].setOnMouseClicked(
                    new HumanClickListener(i, this));
            ui.squares[coordinates[0] + 1][coordinates[1] + 1].slot.setVisible(true);
        }
    }

    public void undo() {
        if (prev != null) {
            System.out.println("Undo");
            humanSide = prev.humanSide;
            total = prev.total;
            total2 = prev.total2;
            aiTime = prev.aiTime;
            turnLeft = prev.turnLeft;
            state = new byte[61];
            System.arraycopy(prev.state, 0, state, 0, prev.state.length);
            prev = prev.prev;
            log.remove(log.size() - 1);
            log.remove(log.size() - 1);
            update();
        } else {
            System.out.println("Can't undo");
        }
    }


    public void update() {
        int w = 0, b = 0;
        for (Square[] row : ui.squares) {
            for (Square square : row) {
                if (square.id != -1) {
                    if (state[square.id] == 1) {
                        w++;
                        square.slot.setFill(Color.GREY);
                    } else if (state[square.id] == -1) {
                        b++;
                        square.slot.setFill(Color.rgb(43, 43, 43));
                    } else
                        square.slot.setFill(Color.rgb(112, 146, 190));
                    if (ui.bs != null) ui.bs.setText("Black Score: " + (14 - w) + "      ");
                    if (ui.ws != null) ui.ws.setText("White Score: " + (14 - b) + "      ");
                }
            }
        }
        if (w == 8 || b == 8) {
            gameOver = true;
            System.out.println("Game Over");
        }
    }

    public void aiMove(AI ai) {
        if (gameOver) return;
        long start = System.currentTimeMillis();
        byte[] best = ai.getBestMove(turnLeft, aiTime, state);
        if(best == null) {
            System.out.println("Not enough time for AI to get a move");
            System.exit(0);
        }
        long end = System.currentTimeMillis();
        double used = (double) (end - start) / 1000;
        if (ai == this.ai) total += used;
        if (humanSide == 0 && ai == this.counter) total2 += used;
        String s = (ai == this.ai) ? String.valueOf(total) : String.valueOf(total2);
        String t = String.valueOf(used);
        turnLeft--;
        String moved = Game.moveToString(best, state);
        System.out.println((ai.side == 1 ? "WHITE" : "BLACK") + " AI "
                + ai.name + " moved: " + moved + " Turn Left: " + turnLeft
                + " Used: " + (t.length() > 6 ? t.substring(0, 6) : t) + "s Total: " +
                (s.length() > 6 ? s.substring(0, 6) : s) + "s\n");
        log.add("Turn Left: " + turnLeft
                + "\t" + (humanSide == -1 ? "WHITE" : "BLACK")
                + ": " + moved
                + "\tTime: " + (t.length() > 6 ? t.substring(0, 6) : t) + "s"
                + "\tTotal Time: " + (s.length() > 6 ? s.substring(0, 6) : s) + "s\n");
        move(best[0], best[1], best[2], state);
    }

    public void aiFirstMove() {
        long start = System.currentTimeMillis();
        byte[] best = ai.getFirstMove(state);
        long end = System.currentTimeMillis();
        double used = (double) (end - start) / 1000;
        String s = String.valueOf(total);
        String t = String.valueOf(used);
        turnLeft--;
        System.out.println("Turn Left: " + turnLeft
                + " Used: " + (t.length() > 6 ? t.substring(0, 6) : t) + "s Total: " +
                (s.length() > 6 ? s.substring(0, 6) : s) + "s\n");
        log.add("Turn Left: " + turnLeft
                + "\t" + (humanSide == -1 ? "WHITE" : "BLACK")
                + ": " + Game.moveToString(best, state)
                + "\tTime: " + (t.length() > 6 ? t.substring(0, 6) : t) + "s"
                + "\tTotal Time: " + (s.length() > 6 ? s.substring(0, 6) : s) + "s");
        move(best[0], best[1], best[2], state);
    }

    static String moveToString(byte[] move, byte[] state) {
        byte[] uiMove = toUiMove(move[0], move[1], move[2], state);
        StringBuilder sb = new StringBuilder();
        sb.append(Game.ToStandardNotation[uiMove[0]]);
        sb.append(" ");
        if (uiMove.length == 2) {
            sb.append(Game.directionToString[uiMove[1]]);
        } else {
            sb.append(Game.ToStandardNotation[uiMove[1]]);
            sb.append(" ");
            sb.append(Game.directionToString[uiMove[2]]);
        }
        return sb.toString();
    }

    void outputLog(boolean toConsole) {
        if (toConsole) {
            for (String s : log) {
                System.out.println(s);
            }
            System.out.println();
        } else {
            FileWriter fw = null;
            BufferedWriter bw = null;
            try {
                fw = new FileWriter("./log.txt");
                bw = new BufferedWriter(fw);
                for (String s : log) {
                    bw.write(s + "\n");
                }
                bw.close();
                fw.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            } finally {
                try {
                    if (bw != null) bw.close();
                    if (fw != null) fw.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public static final byte[] CounterDirection = new byte[]{3, 4, 5, 0, 1, 2};

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

    public static final int[][] UI_GRID = new int[][]{
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

    public static byte[] getStandardInitialLayout() {
        return new byte[]{
                1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1,
                0, 0, 1, 1, 1, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, -1, -1, -1, 0, 0,
                -1, -1, -1, -1, -1, -1,
                -1, -1, -1, -1, -1,
        };
    }

    public static byte[] getGermanDaisyLayout() {
        return new byte[]{
                0, 0, 0, 0, 0,
                1, 1, 0, 0, -1, -1,
                1, 1, 1, 0, -1, -1, -1,
                0, 1, 1, 0, 0, -1, -1, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, -1, -1, 0, 0, 1, 1, 0,
                -1, -1, -1, 0, 1, 1, 1,
                -1, -1, 0, 0, 1, 1,
                0, 0, 0, 0, 0
        };
    }

    public static byte[] getBelgianDaisyLayout() {
        return new byte[]{
                1, 1, 0, -1, -1,
                1, 1, 1, -1, -1, -1,
                0, 1, 1, 0, -1, -1, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0,
                0, -1, -1, 0, 1, 1, 0,
                -1, -1, -1, 1, 1, 1,
                -1, -1, 0, 1, 1
        };
    }

    public static final String[] directionToString = new String[]{"Left", "LeftUp", "RightUp", "Right", "RightDown", "LeftDown"};
    public static final String[] ToStandardNotation = new String[]{
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

    public static void print(int[] state) {
        StringBuilder sb = new StringBuilder();
        int spaces = 4, n = 0, m = 5;
        boolean decre = true;
        char x = 'I', y = '9';
        for (int aState : state) {
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

//            char a = ' ';
//            switch (aState) {
//                case -1:
//                    a = '@';
//                    break;
//                case 0:
//                    a = '+';
//                    break;
//                case 1:
//                    a = 'O';
//                    break;
//            }
            sb.append(String.valueOf(aState)).append(' ');
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

}
