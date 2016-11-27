package com.adamdbradley.midi.message;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiDevice;
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
public class NoteOffMessage extends NoteMessage {

    private static final int COMMAND = ShortMessage.NOTE_OFF;

    public NoteOffMessage(final MidiDevice device, final Channel channel, final Note note,
            final ContinuousControlValue velocity) throws InvalidMidiDataException {
        super(device, COMMAND, channel, note, velocity);
    }

    protected NoteOffMessage(final MidiDevice device, final ShortMessage message) {
        super(device, message);
        if (message.getCommand() != COMMAND) {
            throw new IllegalArgumentException();
        }
    }

}
