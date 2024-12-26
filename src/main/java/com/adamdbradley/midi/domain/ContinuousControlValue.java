package com.adamdbradley.midi.domain;

/**
 * Identifies a MIDI Continuous Control value (0-127).
 * Nominal and wire values are identical.
 */
public class ContinuousControlValue extends SingleSevenBitData {

    private static final ContinuousControlValue controlValues[];
    static {
        controlValues = new ContinuousControlValue[128];
        for (int i=0; i<128; i++) {
            controlValues[i] = new ContinuousControlValue(i);
        }
    }

    public static final ContinuousControlValue ZERO = of(0);
    public static final ContinuousControlValue MID = of(64);
    public static final ContinuousControlValue NOMINAL = of(100);
    public static final ContinuousControlValue MAX = of(127);

    protected ContinuousControlValue(final String name, final int data) {
        super(false, name, data);
    }

    public ContinuousControlValue(final int data) {
        super(false, "Val" + data, data);
    }

    /**
     * @param ccVal 0..127
     * @return
     */
    public static ContinuousControlValue of(final int ccVal) {
        return controlValues[ccVal];
    }

}
