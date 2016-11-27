package com.adamdbradley.midi.message;

import java.util.Arrays;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.SysexMessage;

import com.adamdbradley.midi.MidiUtils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Immutable wrapper object for {@link MidiMessage}.
 * @param <T> subclass of MidiMessage to specialize for
 */
@RequiredArgsConstructor
public abstract class Message<T extends MidiMessage> {

    @Getter
    private final MidiDevice device;

    private final T message;

    /**
     * Since {@link MidiMessage} isn't immutable, always vend a copy so
     * we can protect the one we own/wrap.
     */
    @SuppressWarnings("unchecked")
    public final T getMessage() {
        return (T) message.clone();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + ":"
                + MidiUtils.toString(message);
    }

    @Override
    public int hashCode() {
        return this.message.getMessage().hashCode();
    }

    @Override
    public boolean equals(final Object that) {
        if (!(that instanceof Message)) {
            return false;
        } else {
            return Arrays.equals(message.getMessage(),
                    ((Message<?>) that).message.getMessage());
        }
    }

    public static Message<?> parse(final MidiDevice device, final MidiMessage input) {
        if (input instanceof ShortMessage) {
            final ShortMessage message = (ShortMessage) input;
            switch (((ShortMessage) input).getCommand()) {
            case ShortMessage.NOTE_OFF:
                return new NoteOffMessage(device, message);
            case ShortMessage.NOTE_ON:
                return new NoteOnMessage(device, message);
            case ShortMessage.POLY_PRESSURE:
                return new PolyphonicKeyPressureMessage(device, message);
            case ShortMessage.CONTROL_CHANGE:
                return new ControlChangeMessage(device, message);
            case ShortMessage.PROGRAM_CHANGE:
                return new ProgramChangeMessage(device, message);
            case ShortMessage.CHANNEL_PRESSURE:
                return new ChannelPressureMessage(device, message);
            case ShortMessage.PITCH_BEND:
                return new PitchBendChangeMessage(device, message);
            default:
                throw new IllegalStateException("Unknown command " + message.getCommand());
            }
        } else if (input instanceof SysexMessage) {
            throw new IllegalArgumentException("Can't parse sysex");
        } else {
            throw new IllegalArgumentException("Unknown type: " + input.getClass().getSimpleName());
        }
    }

}
