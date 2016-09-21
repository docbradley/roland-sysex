package com.adamdbradley.sysex;

public class CurveParameterValue extends SingleSevenBitData {

    private CurveParameterValue(final int data) {
        super(data);
    }

    private static final CurveParameterValue[] paramValues;
    static {
        paramValues = new CurveParameterValue[128];
        for (int i=0; i<128; i++) {
            paramValues[i] = new CurveParameterValue(i);
        }
    };

    /**
     * @param value 0..127
     * @return
     */
    public static CurveParameterValue of(final int value) {
        return paramValues[value];
    }

}
