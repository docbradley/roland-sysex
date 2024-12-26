package com.adamdbradley.midi.sysex.roland.a90;

import com.adamdbradley.midi.domain.SingleSevenBitData;

/**
 * Identifies a cent adjustment value (+/-50).
 * Translates between nominal (+/-50) and wire (14..64..114) values.
 */
public class CentBiasControlValue extends SingleSevenBitData {

    public static final CentBiasControlValue ZERO = of(0);

    private CentBiasControlValue(final int data) {
        super(false, "CentBias" + (data - 64), data, 0x0000007F);
    }

    private static final CentBiasControlValue centBiasControls[];
    static {
        centBiasControls = new CentBiasControlValue[101];
        for (int i=0; i<101; i++) {
            centBiasControls[i] = new CentBiasControlValue(i + 14);
        }
    }

    /**
     * 
     * @param centValue -50..0..50
     * @return
     */
    public static CentBiasControlValue of(int centValue) {
        if (centValue < -50 || centValue > 50) {
            throw new IllegalArgumentException("Can't centBias beyond +/- 50");
        }
        return centBiasControls[centValue + 50];
    }
}
