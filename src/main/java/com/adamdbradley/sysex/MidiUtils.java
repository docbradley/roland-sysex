package com.adamdbradley.sysex;

import javax.sound.midi.MidiMessage;

public class MidiUtils {

    public static String toString(final MidiMessage message) {
        final StringBuilder sb = new StringBuilder();
        // J8 streams don't support byte arrays...
        for (int i=0; i<message.getLength(); i++) {
            sb.append(Integer.toHexString(0x00FF & message.getMessage()[i])).append(' ');
        }
        return sb.toString();
    }

}
