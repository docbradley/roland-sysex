package com.adamdbradley.midi;

import java.util.List;

import com.adamdbradley.midi.message.Message;

/**
 * Encapsulates any object capable of specifying a transmissible update as
 * a series of one or more {@link Message}s.
 */
public interface Update {

    public List<Message<?>> getMessages();

}
