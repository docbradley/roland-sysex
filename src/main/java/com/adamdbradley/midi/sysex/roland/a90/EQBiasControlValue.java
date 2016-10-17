package com.adamdbradley.midi.sysex.roland.a90;

import com.adamdbradley.midi.domain.SingleSevenBitData;

/**
 * Identifies a DB adjustment value (+/- 15db).
 * Translates between nominal (+/-15) and wire (49..64..79) values.
 */
public class EQBiasControlValue extends SingleSevenBitData {

    private EQBiasControlValue(final int data) {
        super("DBBias" + (data - 15), data, 0x0000007F);
    }

    private static final EQBiasControlValue eqs[];
    static {
        eqs = new EQBiasControlValue[31];
        for (int i=0; i<31; i++) {
            eqs[i] = new EQBiasControlValue(i + 49);
        }
    }

    /**
     * @param centValue -15..0..15
     * @return
     */
    public static EQBiasControlValue of(int centValue) {
        if (centValue < -15 || centValue > 15) {
            throw new IllegalArgumentException("Can't DB Bias beyond +/- 15");
        }
        return eqs[centValue + 15];
    }

}
