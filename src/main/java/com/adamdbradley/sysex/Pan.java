package com.adamdbradley.sysex;

/**
 * Identifies a MIDI Continuous Control pan value.
 * Converts between nominal (-63..63) and wire (1..64..127) formats.
 */
public class Pan extends ContinuousControlValue {

    private Pan(final int data) {
        super(data);
    }

    private static final Pan[] pan;
    static {
        pan = new Pan[128];
        for (int i=0; i<128; i++) {
            pan[i] = new Pan(i);
        }
    };

    /**
     * @param value 0..127
     * @return
     */
    public static Pan of(final int value) {
        return pan[value + 64];
    }

}
