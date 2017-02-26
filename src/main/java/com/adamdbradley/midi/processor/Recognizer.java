package com.adamdbradley.midi.processor;

import java.io.Serializable;
import java.util.function.Predicate;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;

import com.adamdbradley.midi.message.Message;

/**
 * Marker interface for the ability to recognize a {@link Message}
 * (a tuple of a {@link MidiDevice} and a {@link MidiMessage}).
 */
@FunctionalInterface
public interface Recognizer extends Predicate<Message<?>>, Serializable {
}
