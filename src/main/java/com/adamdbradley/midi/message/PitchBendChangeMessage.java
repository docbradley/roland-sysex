package com.adamdbradley.midi.message;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;

import com.adamdbradley.midi.domain.Channel;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Immutable wrapper object for {@link MidiMessage}.
 */
@EqualsAndHashCode(callSuper=true)
public class PitchBendChangeMessage extends ChannelMessage {

    private static final byte COMMAND = 0b1110;

    @Getter
    private final int value;

    public PitchBendChangeMessage(final MidiDevice device,
            final Channel channel, final int value) throws InvalidMidiDataException {
        this(device, new ShortMessage(COMMAND, channel.getData(),
                (value + 0x2000) & 0x007F, (((value + 0x2000) & 0x3F80)) >> 7));
    }

    protected PitchBendChangeMessage(final MidiDevice device, final ShortMessage message) {
        super(device, message);
        if (message.getCommand() != COMMAND) {
            throw new IllegalArgumentException();
        }
        value = (message.getData1() | (message.getData2() << 7))
                - 0x2000;
    }

}
