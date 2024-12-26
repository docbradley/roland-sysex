package com.adamdbradley.midi.domain;

/**
 * Identifies a MIDI Continuous Control modulation value (0-127).
 * Nominal and wire values are identical.
 */
public class Modulation extends ContinuousControlValue {

    private static final Modulation[] modulationValues;
    static {
        modulationValues = new Modulation[128];
        for (int i=0; i<128; i++) {
            modulationValues[i] = new Modulation(i);
        }
    };

    public static final Modulation ZERO = of(0);

    private Modulation(final int data) {
        super("Mod" + data, data);
    }

    /**
     * @param value 0..127
     * @return
     */
    public static Modulation of(final int value) {
        return modulationValues[value];
    }

}
