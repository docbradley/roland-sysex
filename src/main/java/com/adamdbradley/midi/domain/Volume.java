package com.adamdbradley.midi.domain;

/**
 * Identifies a MIDI Continuous Control volume value (0-127).
 * Nominal and wire values are identical.
 */
public class Volume extends ContinuousControlValue {

    private static final Volume[] volume;
    static {
        volume = new Volume[128];
        for (int i=0; i<128; i++) {
            volume[i] = new Volume(i);
        }
    };

    public static final Volume ZERO = of(0);
    public static final Volume MID = of(64);
    public static final Volume NOMINAL = of(100);
    public static final Volume MAX = of(127);

    private Volume(final int data) {
        super(data);
    }

    /**
     * 
     * @param value 0..127
     * @return
     */
    public static Volume of(final int value) {
        return volume[value];
    }

}
