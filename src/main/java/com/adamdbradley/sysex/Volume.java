package com.adamdbradley.sysex;

public class Volume extends SingleSevenBitData {

    private Volume(final int data) {
        super(data);
    }

    private static final Volume[] volume;
    static {
        volume = new Volume[128];
        for (int i=0; i<128; i++) {
            volume[i] = new Volume(i);
        }
    };

    /**
     * 
     * @param value 0..127
     * @return
     */
    public static Volume of(final int value) {
        return volume[value];
    }

}
