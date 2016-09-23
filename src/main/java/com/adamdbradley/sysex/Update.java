package com.adamdbradley.sysex;

import java.util.List;

/**
 * Encapsulates any object capable of specifying a transmittable update as
 * a series of one or more {@link Message}s.
 */
public interface Update {

    public List<Message<?>> getDataSetMessages();

}
