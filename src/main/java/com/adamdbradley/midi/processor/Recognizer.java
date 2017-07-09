package com.adamdbradley.midi.processor;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;

import com.adamdbradley.midi.domain.Channel;
import com.adamdbradley.midi.message.ChannelMessage;
import com.adamdbradley.midi.message.Message;

/**
 * Marker interface for the ability to recognize a {@link Message}
 * (a tuple of a {@link MidiDevice} and a {@link MidiMessage}).
 */
@FunctionalInterface
public interface Recognizer extends Predicate<ProgramMessage>, Serializable {

    public static Recognizer EVERYTHING = (x) -> true;

    public static Recognizer NOTHING = (x) -> false;

    public static Recognizer from(final DeviceDescriptor output) {
        return (x) -> (x.getDevice().equals(output));
    }

    public static Recognizer from(final DeviceDescriptor output, final Predicate<Channel> channels) {
        return (x) -> {
            if (!x.getDevice().equals(output)) {
                return false;
            }
            return x.toParsed(ChannelMessage.class)
                    .map(ChannelMessage::getChannel)
                    .filter(channels)
                    .isPresent();
        };
    }

    public static Recognizer from(final DeviceDescriptor output, final Collection<Channel> channels) {
        return from(output, channels::contains);
    }

    public static Recognizer from(final DeviceDescriptor output, Channel ... channels) {
        return from(output, Arrays.asList(channels));
    }

    public static Recognizer from(final DeviceDescriptor output, final Channel channel) {
        return from(output, (x) -> x == channel);
    }

}
