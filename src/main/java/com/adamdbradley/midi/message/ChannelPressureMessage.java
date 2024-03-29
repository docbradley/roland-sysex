package com.adamdbradley.midi.message;

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

    private static final int COMMAND = ShortMessage.CHANNEL_PRESSURE;

    @Getter
    private final ContinuousControlValue pressure;

    public ChannelPressureMessage(final Channel channel,
            final ContinuousControlValue pressure) {
        this(buildMessage(COMMAND, channel, pressure));
    }

    protected ChannelPressureMessage(final ShortMessage message) {
        super(message);
        if (message.getCommand() != COMMAND) {
            throw new IllegalArgumentException();
        }
        pressure = ContinuousControlValue.of(message.getData1());
    }

}
