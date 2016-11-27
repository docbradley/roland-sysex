package com.adamdbradley.midi.message;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

import org.junit.Test;

import com.adamdbradley.midi.domain.Channel;
import com.adamdbradley.midi.domain.ContinuousControlValue;
import com.adamdbradley.midi.domain.Note;

import lombok.EqualsAndHashCode;

import static org.junit.Assert.*;

public class Symmetry {

    @Test
    public void noteOn() throws Exception {
        final Constructor<NoteOnMessage> ctor = NoteOnMessage.class
                .getConstructor(MidiDevice.class, Channel.class, Note.class, ContinuousControlValue.class);
        testSymmetryNoteMessages(ctor);
    }

    @Test
    public void noteOff() throws Exception {
        final Constructor<NoteOffMessage> ctor = NoteOffMessage.class
                .getConstructor(MidiDevice.class, Channel.class, Note.class, ContinuousControlValue.class);
        testSymmetryNoteMessages(ctor);
    }

    @Test
    public void polyphonicAftertouch() throws Exception {
        final Constructor<PolyphonicKeyPressureMessage> ctor = PolyphonicKeyPressureMessage.class
                .getConstructor(MidiDevice.class, Channel.class, Note.class, ContinuousControlValue.class);
        testSymmetryNoteMessages(ctor);
    }

    private void testSymmetryNoteMessages(final Constructor<? extends NoteMessage> ctor) 
            throws Exception {
        for (Channel channel: Arrays.asList(Channel.of(1), Channel.of(2), Channel.of(16))) {
            for (Note note: Arrays.asList(Note.of(0), Note.of(63), Note.of(127))) {
                for (ContinuousControlValue value: Arrays.asList(ContinuousControlValue.of(0),
                        ContinuousControlValue.of(63), ContinuousControlValue.of(127))) {
                    final MidiDevice device = new FakeMidiDevice();
                    final NoteMessage built = ctor.newInstance(device, channel, note, value);
                    final NoteMessage parsed = (NoteMessage) Message
                            .parse(device, built.getMessage());
                    assertEquals(parsed, built);
                }
            }
        }
    }

    @EqualsAndHashCode
    private static final class FakeMidiDevice implements MidiDevice {

        private final UUID id = UUID.randomUUID();

        @Override
        public void close() { throw new RuntimeException(); }

        @Override
        public Info getDeviceInfo() { throw new RuntimeException(); }

        @Override
        public int getMaxReceivers() { throw new RuntimeException(); }

        @Override
        public int getMaxTransmitters() { throw new RuntimeException(); }

        @Override
        public long getMicrosecondPosition() { throw new RuntimeException(); }

        @Override
        public Receiver getReceiver() { throw new RuntimeException(); }

        @Override
        public List<Receiver> getReceivers() { throw new RuntimeException(); }

        @Override
        public Transmitter getTransmitter() { throw new RuntimeException(); }

        @Override
        public List<Transmitter> getTransmitters() { throw new RuntimeException(); }

        @Override
        public boolean isOpen() { throw new RuntimeException(); }

        @Override
        public void open() { throw new RuntimeException(); }

    }

}
