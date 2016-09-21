package com.adamdbradley.sysex;

public class ContinuousControllerId extends SingleSevenBitData {

    private ContinuousControllerId(final int data) {
        super(data);
    }

    private static final ContinuousControllerId controllerIds[];
    static {
        controllerIds = new ContinuousControllerId[128];
        for (int i=0; i<128; i++) {
            controllerIds[i] = new ContinuousControllerId(i);
        }
    }

    /**
     * @param ccid 0 for "none" or 1..127
     * @return
     */
    public static ContinuousControllerId of(final int ccid) {
        return controllerIds[ccid];
    }

}
