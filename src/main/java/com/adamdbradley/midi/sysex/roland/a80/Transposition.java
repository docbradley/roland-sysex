package com.adamdbradley.midi.sysex.roland.a80;

import com.adamdbradley.midi.domain.SingleSevenBitData;

/**
 * Identifies a transposition value.
 * Translates between nominal (+/-36) and wire (0..72) values.
 */
public class Transposition extends SingleSevenBitData {

    private Transposition(final int data) {
        super("T" + (data - 36), data, 0x0000007F);
    }

    private static final Transposition transpositions[];
    static {
        transpositions = new Transposition[73];
        for (int i=0; i<73; i++) {
            transpositions[i] = new Transposition(i);
        }
    }

    /**
     * 
     * @param transposition -36..36
     * @return
     */
    public static Transposition of(int transposition) {
        if (transposition < -36 || transposition > 36) {
            throw new IllegalArgumentException("Can't transpose beyond +/- 36");
        }
        return transpositions[transposition + 36];
    }
}
