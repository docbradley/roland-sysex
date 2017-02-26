package com.adamdbradley.midi.message;

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
public class NoteOnMessage extends NoteMessage {

    private static final int COMMAND = ShortMessage.NOTE_ON;

    public NoteOnMessage(final MidiDevice device, final Channel channel, final Note note,
            final ContinuousControlValue velocity) {
        super(device, COMMAND, channel, note, velocity);
    }

    protected NoteOnMessage(final MidiDevice device, final ShortMessage message) {
        super(device, message);
        if (message.getCommand() != COMMAND) {
            throw new IllegalArgumentException();
        }
    }

}
