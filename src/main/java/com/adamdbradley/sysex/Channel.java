package com.adamdbradley.sysex;

public class Channel extends SingleSevenBitData {

    private Channel(final int data) {
        super(data, 0x0000001F); // 0x10 or higher = "disabled"
    }

    private static final Channel[] channels;
    static {
        channels = new Channel[17];
        for (int i=0; i<17; i++) {
            channels[i] = new Channel(i);
        }
    };

    /**
     * @param channel 1..16
     * @return
     */
    public static Channel of(int channel) {
        if (channel < 1 || channel > 16) {
            throw new IllegalArgumentException("Illegal MIDI channel " + channel);
        }
        return channels[channel - 1];
    }

    /**
     * Identifier for "use no channel" (i.e., disable feature)
     * @return
     */
    public static Channel none() {
        return channels[16];
    }
}
