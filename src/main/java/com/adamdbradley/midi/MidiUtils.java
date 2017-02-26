package com.adamdbradley.midi;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import javax.annotation.Nonnull;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;

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
    public static String toString(@Nonnull final MidiMessage message) {
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

    public static Stream<MidiDevice.Info> enumerateDeviceInfo() {
        return Arrays.asList(MidiSystem.getMidiDeviceInfo())
                .stream();
    }

    public static Stream<MidiDevice> enumerateDevices() {
        return enumerateDeviceInfo()
                .map(MidiUtils::infoToDevice)
                .filter(p -> p.isPresent())
                .map(p -> p.get());
    }

    public static Optional<MidiDevice> infoToDevice(@Nonnull final MidiDevice.Info info) {
        try {
            return Optional.of(MidiSystem.getMidiDevice(info));
        } catch (MidiUnavailableException e) {
            return Optional.empty();
        }
    }

}
