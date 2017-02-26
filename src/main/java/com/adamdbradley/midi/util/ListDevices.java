package com.adamdbradley.midi.util;

import com.adamdbradley.midi.DeviceFinder;

public class ListDevices {

    public static void main(final String[] argv) throws Exception {
        new DeviceFinder().find(DeviceFinder.TRUE).forEach((md) -> {
            System.out.println(md.getDeviceInfo().getName()
                    + " Inputs:" + md.getMaxTransmitters()
                    + " Outputs:" + md.getMaxReceivers());
        });
    }

}
