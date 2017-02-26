package com.adamdbradley.midi.domain;

/**
 * Identifier for a MIDI channel (1-16).
 * Translates between the nominal value and the wire value (0x0 - 0xF).
 */
public class Channel extends SingleSevenBitData {

    private Channel(final int data) {
        super(true, "Ch" + (data+1), data, 0x0000000F);
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
    public static Channel of(int channel) {
        if (channel < 1 || channel > 16) {
            throw new IllegalArgumentException("Illegal MIDI channel " + channel);
        }
        return channels[channel - 1];
    }

}
