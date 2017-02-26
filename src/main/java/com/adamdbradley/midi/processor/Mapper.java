package com.adamdbradley.midi.processor;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;

import com.adamdbradley.midi.message.Message;
import com.google.common.collect.ImmutableList;

/**
 * Marker interface for the ability to transform one {@link Message}
 * (a tuple of a {@link MidiDevice} and a {@link MidiMessage}) into
 * a list (potentially empty) of the same.
 */
@FunctionalInterface
public interface Mapper extends Function<Message<?>, List<Message<?>>>, Serializable {

    public static Mapper IDENTITY = ImmutableList::of;

}
