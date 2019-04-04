package sylvartore;

public class Zobrist {

    private int hash;
    private final int[][] bitStrings;

    public Zobrist() {
        bitStrings = new int[61][3];
        for (int i = 0; i < 61; i++) {
            for (int j = 0; j < 3; j++) {
                bitStrings[i][j] = (int) (((long) (Math.random() * Long.MAX_VALUE)));
            }
        }
        hash = 0;
    }

    public Zobrist(Zobrist from) {
        this.bitStrings = from.bitStrings;
        this.hash = from.hash;
    }

    public void reset() {
        hash = 0;
    }

    public int hash(byte[] state) {
        int hash = 0;
        for (int i = 0; i < 61; i++) {
            if (state[i] == 0) continue;
            hash = hash ^ bitStrings[i][state[i] == 1 ? 0 : 1];
        }
        return hash;
    }

    public int xor(int position, int piece) {
        hash = hash ^ bitStrings[position][piece + 1];
        return hash;
    }

    @Override
    public int hashCode() {
        return hash;
    }

}
