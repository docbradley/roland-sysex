package com.adamdbradley.sysex.roland.a80;

import com.adamdbradley.sysex.SingleSevenBitData;

/**
 * The A-80 itself's "Patches"
 */
public class PatchNumber extends SingleSevenBitData {

    private PatchNumber(final int data) {
        super(data, 0x0000003F);
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
