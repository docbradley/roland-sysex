package com.adamdbradley.sysex;

public class Modulation extends SingleSevenBitData {

    private Modulation(final int data) {
        super(data);
    }

    private static final Modulation[] modulationValues;
    static {
        modulationValues = new Modulation[128];
        for (int i=0; i<128; i++) {
            modulationValues[i] = new Modulation(i);
        }
    };

    /**
     * @param value 0..127
     * @return
     */
    public static Modulation of(final int value) {
        return modulationValues[value];
    }

}
