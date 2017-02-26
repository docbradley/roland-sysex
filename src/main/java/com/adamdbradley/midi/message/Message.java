package com.adamdbradley.midi.message;

import java.util.Arrays;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.SysexMessage;

import com.adamdbradley.midi.MidiUtils;
import com.adamdbradley.midi.domain.Channel;
import com.adamdbradley.midi.domain.SingleSevenBitData;

import lombok.RequiredArgsConstructor;

/**
 * Immutable wrapper object for {@link MidiMessage}.
 * @param <T> For cases where subtyping is meaningful (mostly sysex)
 */
@RequiredArgsConstructor
public abstract class Message<T extends MidiMessage> {

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

    public static Message<?> parse(final MidiMessage input) {
        if (input instanceof ShortMessage) {
            final ShortMessage message = (ShortMessage) input;
            switch (((ShortMessage) input).getCommand()) {
            case ShortMessage.NOTE_OFF:
                return new NoteOffMessage(message);
            case ShortMessage.NOTE_ON:
                return new NoteOnMessage(message);
            case ShortMessage.POLY_PRESSURE:
                return new PolyphonicKeyPressureMessage(message);
            case ShortMessage.CONTROL_CHANGE:
                return new ControlChangeMessage(message);
            case ShortMessage.PROGRAM_CHANGE:
                return new ProgramChangeMessage(message);
            case ShortMessage.CHANNEL_PRESSURE:
                return new ChannelPressureMessage(message);
            case ShortMessage.PITCH_BEND:
                return new PitchBendChangeMessage(message);
            default:
                throw new IllegalStateException("Unknown command " + message.getCommand());
            }
        } else if (input instanceof SysexMessage) {
            throw new IllegalArgumentException("Can't parse sysex");
        } else {
            throw new IllegalArgumentException("Unknown type: " + input.getClass().getSimpleName());
        }
    }

    protected static ShortMessage buildMessage(final int command,
            final Channel channel,
            final SingleSevenBitData data0, final SingleSevenBitData data1) {
        try {
            return new ShortMessage(command, channel.getData(), data0.getData(), data1.getData());
        } catch (InvalidMidiDataException e) {
            throw new IllegalArgumentException(e);
        }
    }

    protected static ShortMessage buildMessage(final int command,
            final Channel channel,
            final SingleSevenBitData data0) {
        return buildMessage(command, channel, data0, new SingleSevenBitData(false, "NOOP", 0) {});
    }

}
