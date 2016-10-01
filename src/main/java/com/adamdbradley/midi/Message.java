package com.adamdbradley.midi;

import javax.sound.midi.MidiMessage;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

/**
 * Immutable wrapper object for {@link MidiMessage}.
 * @param <T> subclass of MidiMessage to specialize for
 */
@EqualsAndHashCode
@RequiredArgsConstructor
public abstract class Message<T extends MidiMessage> {

    private final T message;

    /**
     * Since {@link MidiMessage} isn't immutable, always vend a copy so
     * we can protect the one we own/wrap.
     */
    public final MidiMessage getMessage() {
        return (MidiMessage) message.clone();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + ":"
                + MidiUtils.toString(message);
    }

}
