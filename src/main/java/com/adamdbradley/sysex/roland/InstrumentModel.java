package com.adamdbradley.sysex.roland;

import lombok.RequiredArgsConstructor;

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

    /**
     * Use {@link #Roland_JV_5080} messages;
     * unsupported attributes are simply ignored.
     */
    JV_3080(4, new byte[] { (byte) 0x00, (byte) 0x10 }),

    JV_5080(4, new byte[] { (byte) 0x00, (byte) 0x10 });

    private final int addressLength;
    private final byte[] modelId;

    public int addressLength() {
        return addressLength;
    }

    public byte[] modelIdAsBytes() {
        return modelId;
    }

}
