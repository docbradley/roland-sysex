package com.adamdbradley.midi.message;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;

import com.adamdbradley.midi.domain.Channel;
import com.adamdbradley.midi.domain.ContinuousControlValue;
import com.adamdbradley.midi.domain.Note;

import lombok.EqualsAndHashCode;

/**
 * Immutable wrapper object for {@link MidiMessage}.
 */
@EqualsAndHashCode(callSuper=true)
public class PolyphonicKeyPressureMessage extends NoteMessage {

    private static final int COMMAND = ShortMessage.POLY_PRESSURE;

    public PolyphonicKeyPressureMessage(final Channel channel, final Note note,
            final ContinuousControlValue velocity) throws InvalidMidiDataException {
        super(COMMAND, channel, note, velocity);
    }

    protected PolyphonicKeyPressureMessage(final ShortMessage message) {
        super(message);
        if (message.getCommand() != COMMAND) {
            throw new IllegalArgumentException();
        }
    }

}
