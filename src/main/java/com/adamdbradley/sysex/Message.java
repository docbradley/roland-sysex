package com.adamdbradley.sysex;

import java.util.Arrays;

import javax.sound.midi.MidiMessage;

/**
 * Immutable message wrapper object.
 */
public abstract class Message<T extends MidiMessage> {

	private final T message;

    protected Message(final T message) {
        this.message = message;
    }

    public final T getMessage() {
        return message;
    }

    @Override
    public int hashCode() {
        return message.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        if (other == null) {
            return false;
        }
        if (!(other instanceof Message)) {
            return false;
        }
        return Arrays.equals(message.getMessage(),
                ((Message<?>) other).message.getMessage());
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + ":"
                + MidiUtils.toString(message);	
    }

}
