package com.adamdbradley.midi.domain;

/**
 * Identifies a program change value.
 * Converts between nominal (1-128) and wire (0x00-0x7F).
 */
public class ProgramChange extends SingleSevenBitData {

    private ProgramChange(final int data) {
        super("PC" + (data + 1), data);
    }

    private static final ProgramChange[] pc;
    static {
        pc = new ProgramChange[128];
        for (int i=0; i<128; i++) {
            pc[i] = new ProgramChange(i);
        }
    };

    /**
     * 
     * @param value 1..128
     * @return
     */
    public static ProgramChange of(final int value) {
        return pc[value - 1];
    }

}
