package com.adamdbradley.midi.message;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;

import com.adamdbradley.midi.domain.Channel;
import com.adamdbradley.midi.domain.ContinuousControlValue;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Immutable wrapper object for {@link MidiMessage}.
 */
@EqualsAndHashCode(callSuper=true)
public class ChannelPressureMessage extends ChannelMessage {

    private static final byte COMMAND = 0b1101;

    @Getter
    private final ContinuousControlValue pressure;

    public ChannelPressureMessage(final MidiDevice device, final Channel channel,
            final ContinuousControlValue pressure) throws InvalidMidiDataException {
        this(device,
                new ShortMessage(COMMAND, channel.getData(), pressure.getData(), 0));
    }

    protected ChannelPressureMessage(final MidiDevice device, final ShortMessage message) {
        super(device, message);
        if (message.getCommand() != COMMAND) {
            throw new IllegalArgumentException();
        }
        pressure = ContinuousControlValue.of(message.getData1());
    }

}
