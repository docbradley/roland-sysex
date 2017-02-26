package com.adamdbradley.midi.processor;

import java.util.List;
import java.util.Random;

import javax.sound.midi.MidiMessage;

import org.junit.Test;

import com.adamdbradley.midi.domain.Channel;
import com.adamdbradley.midi.domain.ContinuousControlValue;
import com.adamdbradley.midi.domain.Note;
import com.adamdbradley.midi.domain.ProgramChange;
import com.adamdbradley.midi.message.NoteOffMessage;
import com.adamdbradley.midi.message.NoteOnMessage;
import com.adamdbradley.midi.message.ProgramChangeMessage;
import com.google.common.collect.ImmutableList;

import static org.junit.Assert.*;

public class TestProgram {

    private static final Random rng = new Random();

    final ImmutableList<DeviceDescriptor> fakeMidiDevices = ImmutableList.of(
            DeviceDescriptor.builder().name("1").input(true).output(true).build(),
            DeviceDescriptor.builder().name("2").input(true).output(true).build(),
            DeviceDescriptor.builder().name("3").input(true).output(true).build(),
            DeviceDescriptor.builder().name("4").input(true).output(true).build());

    @Test
    public void smokeTestMapping() throws Exception {
        // Send every input on device[0] to both device[2] and device[3]. Squelch everything else.
        final Program program = new Program(ImmutableList
                .of(
                        Rule.builder()
                                .recognizer((mm) -> mm.getDevice() == fakeMidiDevices.get(0))
                                .mapper((mm) -> ImmutableList
                                        .of(
                                                new ProgramMessage(fakeMidiDevices.get(2), mm.getMessage()),
                                                new ProgramMessage(fakeMidiDevices.get(3), mm.getMessage())
                                        ))
                                .build()
                ));

        assertTrue(program.process(randomNoteMessage(fakeMidiDevices.get(1))).isEmpty());
        assertTrue(program.process(randomNoteMessage(fakeMidiDevices.get(2))).isEmpty());
        assertTrue(program.process(randomNoteMessage(fakeMidiDevices.get(3))).isEmpty());

        final ProgramMessage msg = randomNoteMessage(fakeMidiDevices.get(0));

        final List<ProgramMessage> mapped = program.process(msg);
        assertEquals(2, mapped.size());
        assertSame(fakeMidiDevices.get(2), mapped.get(0).getDevice());
        assertArrayEquals(msg.getMessage().getMessage(), mapped.get(0).getMessage().getMessage());
        assertSame(fakeMidiDevices.get(3), mapped.get(1).getDevice());
        assertArrayEquals(msg.getMessage().getMessage(), mapped.get(1).getMessage().getMessage());
    }


    private static ProgramMessage randomNoteMessage(final DeviceDescriptor device) {
        return new ProgramMessage(device, randomMessage());
    }

    private static MidiMessage randomMessage() {
        switch (rng.nextInt(3)) {
            case 0:
                return new NoteOnMessage(Channel.of(rng.nextInt(16) + 1),
                        Note.of(rng.nextInt(128)), ContinuousControlValue.of(rng.nextInt(128))).getMessage();
            case 1:
                return new NoteOffMessage(Channel.of(rng.nextInt(16) + 1),
                        Note.of(rng.nextInt(128)), ContinuousControlValue.of(rng.nextInt(128))).getMessage();
            case 2:
                return new ProgramChangeMessage(Channel.of(rng.nextInt(16) + 1),
                        ProgramChange.of(rng.nextInt(128) + 1)).getMessage();
            default:
                throw new IllegalStateException();
        }
    }

}
