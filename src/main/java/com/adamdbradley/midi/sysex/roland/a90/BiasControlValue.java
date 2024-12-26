package com.adamdbradley.midi.sysex.roland.a90;

import com.adamdbradley.midi.domain.SingleSevenBitData;

/**
 * Identifies a cent adjustment value (+/-50).
 * Translates between nominal (+/-50) and wire (14..64..114) values.
 */
public class BiasControlValue extends SingleSevenBitData {

    public static final BiasControlValue ZERO = of(0);

    private BiasControlValue(final int data) {
        super(false, "Bias" + (data - 64), data, 0x0000007F);
    }

    private static final BiasControlValue biasControls[];
    static {
        biasControls = new BiasControlValue[101];
        for (int i=0; i<127; i++) {
            biasControls[i] = new BiasControlValue(i + 1);
        }
    }

    /**
     * 
     * @param biasedValue -63..0..63
     * @return
     */
    public static BiasControlValue of(int biasedValue) {
        if (biasedValue < -64 || biasedValue > 64) {
            throw new IllegalArgumentException("Can't bias beyond +/- 63");
        }
        return biasControls[biasedValue + 50];
    }
}
