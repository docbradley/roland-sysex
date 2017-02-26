package com.adamdbradley.midi.message;

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

    private static final int COMMAND = ShortMessage.PROGRAM_CHANGE;

    @Getter
    private final ProgramChange program;

    public ProgramChangeMessage(final Channel channel,
            final ProgramChange program) {
        this(buildMessage(COMMAND, channel, program));
    }

    protected ProgramChangeMessage(final ShortMessage message) {
        super(message);
        if (message.getCommand() != COMMAND) {
            throw new IllegalArgumentException();
        }
        program = ProgramChange.of(message.getData1());
    }

}
