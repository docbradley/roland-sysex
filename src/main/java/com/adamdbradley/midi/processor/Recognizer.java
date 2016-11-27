package com.adamdbradley.midi.processor;

import java.io.Serializable;
import java.util.function.Predicate;

import com.adamdbradley.midi.message.Message;

public interface Recognizer extends Predicate<Message<?>>, Serializable {
}
