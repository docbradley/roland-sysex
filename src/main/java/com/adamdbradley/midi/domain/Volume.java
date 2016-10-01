package com.adamdbradley.midi.domain;

/**
 * Identifies a MIDI Continuous Control volume value (0-127).
 * Nominal and wire values are identical.
 */
public class Volume extends ContinuousControlValue {

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
