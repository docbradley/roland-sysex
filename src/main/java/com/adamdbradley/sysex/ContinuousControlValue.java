package com.adamdbradley.sysex;

/**
 * Identifies a MIDI Continuous Control value (0-127).
 * Nominal and wire values are identical.
 */
public class ContinuousControlValue extends SingleSevenBitData {

    protected ContinuousControlValue(final String name, final int data) {
        super(name, data);
    }

    public ContinuousControlValue(final int data) {
        super("Val" + data, data);
    }

    private static final ContinuousControlValue controlValues[];
    static {
        controlValues = new ContinuousControlValue[128];
        for (int i=0; i<128; i++) {
            controlValues[i] = new ContinuousControlValue(i);
        }
    }

    /**
     * @param ccVal 0..127
     * @return
     */
    public static ContinuousControlValue of(final int ccVal) {
        return controlValues[ccVal];
    }

}
