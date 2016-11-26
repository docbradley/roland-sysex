package com.adamdbradley.midi.message;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;

import com.adamdbradley.midi.domain.Channel;
import com.adamdbradley.midi.domain.ContinuousControlValue;
import com.adamdbradley.midi.domain.ContinuousControllerId;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Immutable wrapper object for {@link MidiMessage}.
 */
@EqualsAndHashCode(callSuper=true)
public class ControlChangeMessage extends ChannelMessage {

    private static final byte COMMAND = 0b1011;

    @Getter
    private final ContinuousControllerId controller;

    @Getter
    private final ContinuousControlValue value;

    public ControlChangeMessage(final Channel channel,
            final ContinuousControllerId controller,
            final ContinuousControlValue value) throws InvalidMidiDataException {
        this(new ShortMessage(COMMAND, channel.getData(), controller.getData(), value.getData()));
    }

    protected ControlChangeMessage(final ShortMessage message) {
        super(message);
        if (message.getCommand() != COMMAND) {
            throw new IllegalArgumentException();
        }
        controller = ContinuousControllerId.of(message.getData1());
        value = ContinuousControlValue.of(message.getData2());
    }

}
