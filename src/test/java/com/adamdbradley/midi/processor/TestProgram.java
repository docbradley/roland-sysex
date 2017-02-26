package com.adamdbradley.midi.processor;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.adamdbradley.midi.ProxyMidiDevice;
import com.adamdbradley.midi.domain.Channel;
import com.adamdbradley.midi.domain.ContinuousControlValue;
import com.adamdbradley.midi.domain.Note;
import com.adamdbradley.midi.domain.ProgramChange;
import com.adamdbradley.midi.message.Message;
import com.adamdbradley.midi.message.NoteOffMessage;
import com.adamdbradley.midi.message.NoteOnMessage;
import com.adamdbradley.midi.message.ProgramChangeMessage;
import com.google.common.collect.ImmutableList;

import static org.junit.Assert.*;

public class TestProgram {

    private static final Random rng = new Random();

    final ImmutableList<MidiDevice> fakeMidiDevices = ImmutableList.of(
            new FakeMidiDevice(),
            new FakeMidiDevice(),
            new FakeMidiDevice(),
            new FakeMidiDevice());

    @Before
    public void setupFakeDeviceFinderSupplier() {
        ProxyMidiDevice.supplier = () -> fakeMidiDevices.stream();
    }

    @After
    public void clearDeviceFinderSupplier() {
        ProxyMidiDevice.supplier = null;
    }

    @Test
    public void smokeTestMapping() throws Exception {
        // Send every input on device[0] to both device[2] and device[3]. Squelch everything else.
        final Program program = new Program(ImmutableList
                .of(
                        Rule.builder()
                                .recognizer((mm) -> mm.getDevice() == fakeMidiDevices.get(0))
                                .mapper((mm) -> ImmutableList
                                        .of(
                                                mm.reroute(fakeMidiDevices.get(2)),
                                                mm.reroute(fakeMidiDevices.get(3))
                                        ))
                                .build()
                ));

        assertTrue(program.process(randomNoteMessage(fakeMidiDevices.get(1))).isEmpty());
        assertTrue(program.process(randomNoteMessage(fakeMidiDevices.get(2))).isEmpty());
        assertTrue(program.process(randomNoteMessage(fakeMidiDevices.get(3))).isEmpty());

        final Message<?> msg = randomNoteMessage(fakeMidiDevices.get(0));

        final List<Message<?>> mapped = program.process(msg);
        assertEquals(2, mapped.size());
        assertSame(fakeMidiDevices.get(2), mapped.get(0).getDevice());
        assertArrayEquals(msg.getMessage().getMessage(), mapped.get(0).getMessage().getMessage());
        assertSame(fakeMidiDevices.get(3), mapped.get(1).getDevice());
        assertArrayEquals(msg.getMessage().getMessage(), mapped.get(1).getMessage().getMessage());
    }


    private static Message<?> randomNoteMessage(final MidiDevice device) {
        switch (rng.nextInt(3)) {
            case 0:
                return new NoteOnMessage(device, Channel.of(rng.nextInt(16) + 1),
                        Note.of(rng.nextInt(128)), ContinuousControlValue.of(rng.nextInt(128)));
            case 1:
                return new NoteOffMessage(device, Channel.of(rng.nextInt(16) + 1),
                        Note.of(rng.nextInt(128)), ContinuousControlValue.of(rng.nextInt(128)));
            case 2:
                return new ProgramChangeMessage(device, Channel.of(rng.nextInt(16) + 1),
                        ProgramChange.of(rng.nextInt(128) + 1));
            default:
                throw new IllegalStateException();
        }
    }


    private static final AtomicInteger sequence = new AtomicInteger(0);

    private class FakeMidiDevice implements MidiDevice {

        private final MidiDevice.Info info = new Info("" + sequence.getAndIncrement() + "/" + UUID.randomUUID(),
                "adamdbradley.com",
                "A pretend MIDI device",
                "0.0.0.0") {};

        @Override
        public String toString() {
            return getDeviceInfo().toString();
        }

        @Override
        public Info getDeviceInfo() {
            return info;
        }

        @Override
        public void close() {
        }

        @Override
        public int getMaxReceivers() {
            return 1;
        }

        @Override
        public int getMaxTransmitters() {
            return 1;
        }

        @Override
        public long getMicrosecondPosition() {
            return 0;
        }

        @Override
        public Receiver getReceiver() throws MidiUnavailableException {
            return null;
        }

        @Override
        public List<Receiver> getReceivers() {
            return null;
        }

        @Override
        public Transmitter getTransmitter() throws MidiUnavailableException {
            return null;
        }

        @Override
        public List<Transmitter> getTransmitters() {
            return null;
        }

        @Override
        public boolean isOpen() {
            return false;
        }

        @Override
        public void open() throws MidiUnavailableException {
        }
    };

}
