package com.adamdbradley.midi.sysex.roland.a90;

import com.adamdbradley.midi.domain.SingleSevenBitData;

/**
 * Identifies a cent adjustment value.
 * Translates between nominal (0..100) and wire (14..114) values.
 */
public class CentControlValue extends SingleSevenBitData {

    private CentControlValue(final int data) {
        super(false, "Ctrl" + (data - 14) + "%", data, 0x0000007F);
    }

    private static final CentControlValue centControlValues[];
    static {
        centControlValues = new CentControlValue[101];
        for (int i=0; i<101; i++) {
            centControlValues[i] = new CentControlValue(i + 14);
        }
    }

    /**
     * 
     * @param centValue -50..0..50
     * @return
     */
    public static CentControlValue of(int centValue) {
        if (centValue < 0 || centValue > 100) {
            throw new IllegalArgumentException("Can't cent outside 0..100");
        }
        return centControlValues[centValue];
    }
}
