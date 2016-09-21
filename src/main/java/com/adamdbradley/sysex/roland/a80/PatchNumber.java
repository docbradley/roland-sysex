package com.adamdbradley.sysex.roland.a80;

import com.adamdbradley.sysex.SingleSevenBitData;

public class PatchNumber extends SingleSevenBitData {

    PatchNumber(final int data) {
        super(data, 0x0000003F);
    }

}
