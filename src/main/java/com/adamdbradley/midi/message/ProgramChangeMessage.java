package com.adamdbradley.midi.message;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;

import com.adamdbradley.midi.domain.Channel;
import com.adamdbradley.midi.domain.ProgramChange;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Immutable wrapper object for {@link MidiMessage}.
 */
@EqualsAndHashCode(callSuper=true)
public class ProgramChangeMessage extends ChannelMessage {

    private static final byte COMMAND = 0b1100;

    @Getter
    private final ProgramChange program;

    public ProgramChangeMessage(final MidiDevice device, final Channel channel,
            final ProgramChange program) throws InvalidMidiDataException {
        this(device, new ShortMessage(COMMAND, channel.getData(),
                program.getData(), 0));
    }

    protected ProgramChangeMessage(final MidiDevice device, final ShortMessage message) {
        super(device, message);
        if (message.getCommand() != COMMAND) {
            throw new IllegalArgumentException();
        }
        program = ProgramChange.of(message.getData1());
    }

}
