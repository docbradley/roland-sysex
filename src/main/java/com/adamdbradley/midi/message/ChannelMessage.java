package com.adamdbradley.midi.message;

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
public abstract class ChannelMessage extends Message<ShortMessage> {

    @Getter
    private final Channel channel;

    protected ChannelMessage(final MidiDevice device, final ShortMessage message) {
        super(device, message);
        channel = Channel.of(message.getChannel() + 1);
    }

}
