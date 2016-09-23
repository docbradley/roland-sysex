package com.adamdbradley.sysex.roland;

import lombok.RequiredArgsConstructor;

/**
 * Identifies a particular Roland instrument model.
 * This is used to populate the ModelID field and control the length of the
 * address field in Roland's SysEx message format.
 */
@RequiredArgsConstructor
public enum InstrumentModel {

    /**
     * The original.  The monster.
     */
    A_80 (3, new byte[] { (byte) 0x27 }),

    /**
     * Includes the simple A-90, the A-90 with VE-RD1, and the A-90EX.
     */
    A_90(4, new byte[] { (byte) 0x7D }),

    A_88(0, null),
    A_49(0, null),
    A_50(0, null),
    A_33(0, null),
    A_30(0, null),
    A_300PRO(0, null),
    A_500PRO(0, null),
    A_800PRO(0, null),

    ;

    private final int addressLength;
    private final byte[] modelId;

    public int addressLength() {
        return addressLength;
    }

    public byte[] modelIdAsBytes() {
        return modelId;
    }

}
