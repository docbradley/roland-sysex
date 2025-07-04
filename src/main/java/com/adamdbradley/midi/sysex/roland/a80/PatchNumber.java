package com.adamdbradley.midi.sysex.roland.a80;

import com.adamdbradley.midi.domain.SingleSevenBitData;

/**
 * Identifier for an A-80 "patch".
 * Translates between nominal (1-64, aka 11-88 oct-basis-1)
 * and wire (0-63) representations.
 */
public class PatchNumber extends SingleSevenBitData {

    private PatchNumber(final int data) {
        super(true, "A80P" + (data + 1), data, 0x0000003F);
    }

    private static final PatchNumber[] patchValues;
    static {
        patchValues = new PatchNumber[64];
        for (int i=0; i<64; i++) {
            patchValues[i] = new PatchNumber(i);
        }
    };

    /**
     * 
     * @param value 1..64
     * @return
     */
    public static PatchNumber of(final int value) {
        return patchValues[value - 1];
    }

}
