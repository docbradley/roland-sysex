package com.adamdbradley.sysex;

public class ProgramChange extends SingleSevenBitData {

    private ProgramChange(final int data) {
        super(data);
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
     * @param value 0..127
     * @return
     */
    public static ProgramChange of(final int value) {
        return pc[value];
    }

}
