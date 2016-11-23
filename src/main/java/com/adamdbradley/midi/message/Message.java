package com.adamdbradley.midi.message;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.SysexMessage;

import com.adamdbradley.midi.MidiUtils;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

/**
 * Immutable wrapper object for {@link MidiMessage}.
 * @param <T> subclass of MidiMessage to specialize for
 */
@EqualsAndHashCode
@RequiredArgsConstructor
public abstract class Message<T extends MidiMessage> {

    private final T message;

    /**
     * Since {@link MidiMessage} isn't immutable, always vend a copy so
     * we can protect the one we own/wrap.
     */
    public final MidiMessage getMessage() {
        return (MidiMessage) message.clone();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + ":"
                + MidiUtils.toString(message);
    }

    public static Message<?> parse(final MidiMessage input) {
        if (input instanceof ShortMessage) {
            final ShortMessage message = (ShortMessage) input;
            switch (((ShortMessage) input).getCommand()) {
            case 0b1000:
                return new NoteOffMessage(message);
            case 0b1001:
                return new NoteOnMessage(message);
            case 0b1010:
                return new PolyphonicKeyPressureMessage(message);
            case 0b1011:
                return new ControlChangeMessage(message);
            case 0b1100:
                return new ProgramChangeMessage(message);
            case 0b1101:
                return new ChannelPressureMessage(message);
            case 0b1110:
                return new PitchBendChangeMessage(message);
            case 0b1111:
                throw new IllegalArgumentException("Can't parse system common message type " + message.getCommand());
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
