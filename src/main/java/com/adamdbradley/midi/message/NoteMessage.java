package com.adamdbradley.midi.message;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;

import com.adamdbradley.midi.domain.Channel;
import com.adamdbradley.midi.domain.ContinuousControlValue;
import com.adamdbradley.midi.domain.Note;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Immutable wrapper object for {@link MidiMessage}.
 */
@EqualsAndHashCode(callSuper=true)
public abstract class NoteMessage extends ChannelMessage {

    @Getter
    private final Note note;

    @Getter
    private final ContinuousControlValue value;

    protected NoteMessage(final ShortMessage message) {
        super(message);
        note = Note.of(message.getData1());
        value = ContinuousControlValue.of(message.getData2());
    }

    public NoteMessage(final int command, final Channel channel,
            final Note note, final ContinuousControlValue velocity)
                    throws InvalidMidiDataException {
        this(new ShortMessage(command, channel.getData(),
                note.getData(), velocity.getData()));
    }

}
