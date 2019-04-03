package sylvartore;

public class Zobrist {

    private int hash;
    private final int[][] bitStrings;

    /**
     * Initialize values for the given number of pieces and the given number of positions
     */
    public Zobrist() {
        bitStrings = new int[61][3];
        for (int i = 0; i < 61; i++) {
            for (int j = 0; j < 3; j++) {
                bitStrings[i][j] = (int) (((long) (Math.random() * Long.MAX_VALUE)));
            }
        }
        hash = 0;
    }

    /**
     * Initialize values from an existing instance of
     *
     * @param from
     */
    public Zobrist(Zobrist from) {
        this.bitStrings = from.bitStrings;
        this.hash = from.hash;
    }

    public void reset() {
        hash = 0;
    }

    public int hash(byte[] state) {
        for (int i = 0; i < 61; i++) {
            if (state[i] == 0) continue;
            xor(i, state[i] == 1 ? 0 : 1);
        }
        return hash;
    }

    /**
     * Compute the resulting hash after the given move.<br/>
     * XOR the bit string of the piece at the given position with the current hash value.
     *
     * @param piece
     * @param position
     * @return the Zobrist hash value
     */
    public int xor(int position, int piece) {
        hash = hash ^ bitStrings[position][piece + 1];
        return hash;
    }

    @Override
    public int hashCode() {
        return hash;
    }

}
