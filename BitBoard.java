package sylvartore;

public class BitBoard {
    public byte[][] standardInitialLayout() {
        return new byte[][]{
                {11, 11, 11, 11, 11, 11},
                {11, 1, 1, 1, 1, 1, 11},
                {11, 1, 1, 1, 1, 1, 1, 11},
                {11, 0, 0, 1, 1, 1, 0, 0, 11},
                {11, 0, 0, 0, 0, 0, 0, 0, 0, 11},
                {11, 0, 0, 0, 0, 0, 0, 0, 0, 0, 11},
                {11, 0, 0, 0, 0, 0, 0, 0, 0, 11},
                {11, 0, 0, 10, 10, 10, 0, 0, 11},
                {11, 10, 10, 10, 10, 10, 10, 11},
                {11, 10, 10, 10, 10, 10, 11},
                {11, 11, 11, 11, 11, 11},
        };
    }

    public static String[][] toStandardNotation = new String[][]{{},
            {"", "I5", "I6", "I7", "I8", "I9"},
            {"", "H4", "H5", "H6", "H7", "H8", "H9"},
            {"", "G3", "G4", "G5", "G6", "G7", "G8", "G9"},
            {"", "F2", "F3", "F4", "F5", "F6", "F7", "F8", "F9"},
            {"", "E1", "E2", "E3", "E4", "E5", "E6", "E7", "E8", "E9"},
            {"", "D1", "D2", "D3", "D4", "D5", "D6", "D7", "D8"},
            {"", "C1", "C2", "C3", "C4", "C5", "C6", "C7"},
            {"", "B1", "B2", "B3", "B4", "B5", "B6"},
            {"", "A1", "A2", "A3", "A4", "A5"},
    };

    public static byte[][] dimensionReduction = new byte[][]{{},
            {-1, 0, 1, 2, 3, 4},
            {-1, 5, 6, 7, 8, 9, 10},
            {-1, 11, 12, 13, 14, 15, 16, 17},
            {-1, 18, 19, 20, 21, 22, 23, 24, 25},
            {-1, 26, 27, 28, 29, 30, 31, 32, 33, 34},
            {-1, 35, 36, 37, 38, 39, 40, 41, 42},
            {-1, 43, 44, 45, 46, 47, 48, 49},
            {-1, 50, 51, 52, 53, 54, 55},
            {-1, 56, 57, 58, 59, 60},
    };

    public static byte[][] dimensionIncrement = new byte[][]{
            {1, 1}, {1, 2}, {1, 3}, {1, 4}, {1, 5},
            {2, 1}, {2, 2}, {2, 3}, {2, 4}, {2, 5}, {2, 6},
            {3, 1}, {3, 2}, {3, 3}, {3, 4}, {3, 5}, {3, 6}, {3, 7},
            {4, 1}, {4, 2}, {4, 3}, {4, 4}, {4, 5}, {4, 6}, {4, 7}, {4, 8},
            {5, 1}, {5, 2}, {5, 3}, {5, 4}, {5, 5}, {5, 6}, {5, 7}, {5, 8}, {5, 9},
            {6, 1}, {6, 2}, {6, 3}, {6, 4}, {6, 5}, {6, 6}, {6, 7}, {6, 8},
            {7, 1}, {7, 2}, {7, 3}, {7, 4}, {7, 5}, {7, 6}, {7, 7},
            {8, 1}, {8, 2}, {8, 3}, {8, 4}, {8, 5}, {8, 6},
            {9, 1}, {9, 2}, {9, 3}, {9, 4}, {9, 5}
    };

    public static byte[][] transitionMatrix = new byte[][]{
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

    byte state[][];

    public BitBoard() {
        state = standardInitialLayout();
    }

    public boolean isValidMove(byte row, byte col, byte d, byte n) {
        return isValidMove(row, col, d, n, state);
    }

    public boolean isValidMove(byte row, byte col, byte d, byte n, byte[][] state) {
        if (state[row][col] == 11 || state[row][col] == 0) return false;
        byte targetCell = transitionMatrix[dimensionReduction[row][col]][d];
        if (targetCell == -1) return false;
        if (n == 1) {
            if (state[dimensionIncrement[targetCell][0]][dimensionIncrement[targetCell][1]] == 0) return true;
            return canInline(row, col, d);
        } else {
            if (state[dimensionIncrement[targetCell][0]][dimensionIncrement[targetCell][1]] != 0) return false;
            byte ssd = d;
            if (++ssd == 6) ssd = 0;
            if (canSideStep(row, col, d, n, ssd)) return true;
            if (++ssd == 6) ssd = 0;
            return canSideStep(row, col, d, n, ssd);
        }
    }

    public boolean canInline(byte row, byte col, byte d) {
        byte force = 0;
        byte targetCell = transitionMatrix[dimensionReduction[row][col]][d];
        while (++force <= 3 && targetCell != -1 && state[dimensionIncrement[targetCell][0]][dimensionIncrement[targetCell][1]] == state[row][col]) {
            targetCell = transitionMatrix[targetCell][d];
        }
        if (force > 3 || targetCell == -1) return false;
        int[] counterForce = getF(dimensionIncrement[targetCell][0], dimensionIncrement[targetCell][1], d);
        return force > counterForce[2] && (state[counterForce[0]][counterForce[1]] != 11
                || state[counterForce[0]][counterForce[1]] != state[row][col]);
    }

    public int[] getF(byte row, byte col, byte d) {
        if (state[row][col] == 11 || state[row][col] == 0) return new int[]{row, col, 0};
        byte force = 1;
        byte targetCell = transitionMatrix[dimensionReduction[row][col]][d];
        while (++force <= 3 && targetCell != -1 &&
                state[dimensionIncrement[targetCell][0]][dimensionIncrement[targetCell][1]] == state[row][col]) {
            targetCell = transitionMatrix[targetCell][d];
        }
        if (state[dimensionIncrement[targetCell][0]][dimensionIncrement[targetCell][1]] != 0)
            targetCell = transitionMatrix[targetCell][d];
        if (force == 4) force = 0;
        return new int[]{dimensionIncrement[targetCell][0], dimensionIncrement[targetCell][1], force};
    }

    public boolean canSideStep(byte row, byte col, byte d, byte n, byte ssd) {
        byte ally1Cell = transitionMatrix[dimensionReduction[row][col]][ssd];
        if (ally1Cell == -1 || state[row][col] != state[dimensionIncrement[ally1Cell][0]][dimensionIncrement[ally1Cell][1]])
            return false;
        byte ally1SsdCell = transitionMatrix[ally1Cell][d];
        if (state[dimensionIncrement[ally1SsdCell][0]][dimensionIncrement[ally1SsdCell][1]] != 0) return false;
        if (n == 2) return true;
        byte ally2Cell = transitionMatrix[ally1Cell][ssd];
        if (ally2Cell == -1 || state[row][col] != state[dimensionIncrement[ally2Cell][0]][dimensionIncrement[ally2Cell][1]])
            return false;
        byte ally2SsdCell = transitionMatrix[ally2Cell][d];
        return state[dimensionIncrement[ally2SsdCell][0]][dimensionIncrement[ally2SsdCell][1]] == 0;
    }

    public void move(byte row, byte col, byte d, byte n) {
        move(row, col, d, n, state);
    }

    public void move(byte row, byte col, byte d, byte n, byte[][] state) {
        byte targetCell = transitionMatrix[dimensionReduction[row][col]][d];
        if (n == 1) {
            if (state[dimensionIncrement[targetCell][0]][dimensionIncrement[targetCell][1]] == 0)
                basicMove();
            else inlineMove(row, col, d);
        } else {
            byte ssd = d;
            if (++ssd == 6) ssd = 0;
            if (canSideStep(row, col, d, n, ssd)) return;
            if (++ssd == 6) ssd = 0;
            canSideStep(row, col, d, n, ssd);
        }
    }

    public void basicMove() {

    }

    public void inlineMove(int row, int col, int d) {

    }


    public byte[] standardInitialLayout1D() {
        return new byte[]{
                01, 01, 01, 01, 01,
                01, 01, 01, 01, 01, 01,
                00, 00, 01, 01, 01, 00, 00,
                00, 00, 00, 00, 00, 00, 00, 00,
                00, 00, 00, 00, 00, 00, 00, 00, 00,
                00, 00, 00, 00, 00, 00, 00, 00,
                00, 00, 10, 10, 10, 00, 00,
                10, 10, 10, 10, 10, 10,
                10, 10, 10, 10, 10,
        };
    }

    public static String[] toStandardNotation1D = new String[]{
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

    public byte[][] deepCopy() {
        byte[][] copy = new byte[state.length][];
        for (int i = 0; i < copy.length; i++) {
            copy[i] = new byte[state[i].length];
            System.arraycopy(state[i], 0, copy[i], 0, state[i].length);
        }
        return copy;
    }


}
