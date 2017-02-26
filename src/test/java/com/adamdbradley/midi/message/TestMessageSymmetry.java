package com.adamdbradley.midi.message;

import java.lang.reflect.Constructor;
import java.util.Arrays;

import org.junit.Test;

import com.adamdbradley.midi.domain.Channel;
import com.adamdbradley.midi.domain.ContinuousControlValue;
import com.adamdbradley.midi.domain.Note;

import static org.junit.Assert.*;

public class TestMessageSymmetry {

    @Test
    public void noteOn() throws Exception {
        final Constructor<NoteOnMessage> ctor = NoteOnMessage.class
                .getConstructor(Channel.class, Note.class, ContinuousControlValue.class);
        testSymmetryNoteMessages(ctor);
    }

    @Test
    public void noteOff() throws Exception {
        final Constructor<NoteOffMessage> ctor = NoteOffMessage.class
                .getConstructor(Channel.class, Note.class, ContinuousControlValue.class);
        testSymmetryNoteMessages(ctor);
    }

    @Test
    public void polyphonicAftertouch() throws Exception {
        final Constructor<PolyphonicKeyPressureMessage> ctor = PolyphonicKeyPressureMessage.class
                .getConstructor(Channel.class, Note.class, ContinuousControlValue.class);
        testSymmetryNoteMessages(ctor);
    }

    private void testSymmetryNoteMessages(final Constructor<? extends NoteMessage> ctor) 
            throws Exception {
        for (Channel channel: Arrays.asList(Channel.of(1), Channel.of(2), Channel.of(16))) {
            for (Note note: Arrays.asList(Note.of(0), Note.of(63), Note.of(127))) {
                for (ContinuousControlValue value: Arrays.asList(ContinuousControlValue.of(0),
                        ContinuousControlValue.of(63), ContinuousControlValue.of(127))) {
                    final NoteMessage built = ctor.newInstance(channel, note, value);
                    final NoteMessage parsed = (NoteMessage) Message
                            .parse(built.getMessage());
                    assertEquals(parsed, built);
                }
            }
        }
    }

}
