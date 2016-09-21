package com.adamdbradley.sysex;

public class Channel extends SingleSevenBitData {

    private Channel(final int data) {
        super(data, 0x0000000F);
    }

    private static final Channel[] channels;
    static {
        channels = new Channel[16];
        for (int i=0; i<16; i++) {
            channels[i] = new Channel(i);
        }
    };

    /**
     * @param channel 1..16
     * @return
     */
    public Channel of(int channel) {
        if (channel < 1 || channel > 16) {
            throw new IllegalArgumentException("Illegal MIDI channel " + channel);
        }
        return channels[channel - 1];
    }
}
