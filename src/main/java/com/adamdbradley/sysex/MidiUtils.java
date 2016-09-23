package com.adamdbradley.sysex;

import javax.sound.midi.MidiMessage;

import com.google.common.base.Strings;

/**
 * Debugging-oriented helpers for rendering Midi messages.
 */
public class MidiUtils {

    /**
     * Render a {@link MidiMessage} payload as a string of hex digit tuples
     * separated by spaces.
     * hex
     * @param message
     * @return
     */
    public static String toString(final MidiMessage message) {
        final StringBuilder sb = new StringBuilder();
        // J8 streams don't support byte arrays...
        for (int i=0; i<message.getLength(); i++) {
            sb.append(Strings
                    .padStart(
                            Integer.toHexString(0x00FF & message.getMessage()[i]),
                            2, '0'))
                    .append(' ');
        }
        return sb.toString().trim();
    }

}
