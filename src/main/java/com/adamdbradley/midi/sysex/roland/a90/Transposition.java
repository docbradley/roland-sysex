package com.adamdbradley.midi.sysex.roland.a90;

import com.adamdbradley.midi.domain.SingleSevenBitData;

/**
 * Identifies a transposition value.
 * Translates between nominal (+/-36) and wire (28-100) values.
 * TODO: separate concerns between model representation (this is common with
 * {@link com.adamdbradley.midi.sysex.roland.a80.Transposition}) and MIDI translation
 * (where this differs from A80's).
 */
public class Transposition extends SingleSevenBitData {

    public static final Transposition ZERO = of(0);

    private Transposition(final int data) {
        super(false, "T" + (data - 64), data, 0x0000007F);
    }

    private static final Transposition transpositions[];
    static {
        transpositions = new Transposition[73];
        for (int i=0; i<73; i++) {
            transpositions[i] = new Transposition(i + 28);
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
