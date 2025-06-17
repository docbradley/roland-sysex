package com.adamdbradley.midi;

import java.io.File;
import java.util.stream.Stream;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;

import com.adamdbradley.midi.domain.Channel;
import com.adamdbradley.midi.domain.ContinuousControlValue;
import com.adamdbradley.midi.domain.Note;
import com.adamdbradley.midi.message.NoteOffMessage;
import com.adamdbradley.midi.message.NoteOnMessage;

public class MyJesusILoveTheeSequenceWriter {

    public static void main(String[] args) throws Exception {
        final Sequence s = new Sequence(Sequence.PPQ, 4);

//        // piano weave
//        final Track pianoTrack = s.createTrack();
//        Stream.<MidiEvent>of(
//                new MidiEvent(new NoteOnMessage(Channel.of(1), Note.of("A4"), ContinuousControlValue.of(63)).getMessage(), 0),
//                new MidiEvent(new NoteOffMessage(Channel.of(1), Note.of("A4"), ContinuousControlValue.of(63)).getMessage(), 2),
//                new MidiEvent(new NoteOnMessage(Channel.of(1), Note.of("E5"), ContinuousControlValue.of(63)).getMessage(), 2),
//                new MidiEvent(new NoteOffMessage(Channel.of(1), Note.of("E5"), ContinuousControlValue.of(63)).getMessage(), 4),
//                new MidiEvent(new NoteOnMessage(Channel.of(1), Note.of("D5"), ContinuousControlValue.of(63)).getMessage(), 4),
//                new MidiEvent(new NoteOffMessage(Channel.of(1), Note.of("D5"), ContinuousControlValue.of(63)).getMessage(), 6),
//                new MidiEvent(new NoteOnMessage(Channel.of(1), Note.of("E5"), ContinuousControlValue.of(63)).getMessage(), 6),
//                new MidiEvent(new NoteOffMessage(Channel.of(1), Note.of("E5"), ContinuousControlValue.of(63)).getMessage(), 8),
//                new MidiEvent(new NoteOnMessage(Channel.of(1), Note.of("C#5"), ContinuousControlValue.of(63)).getMessage(), 8),
//                new MidiEvent(new NoteOffMessage(Channel.of(1), Note.of("C#5"), ContinuousControlValue.of(63)).getMessage(), 10),
//                new MidiEvent(new NoteOnMessage(Channel.of(1), Note.of("E5"), ContinuousControlValue.of(63)).getMessage(), 10),
//                new MidiEvent(new NoteOffMessage(Channel.of(1), Note.of("E5"), ContinuousControlValue.of(63)).getMessage(), 12),
//                new MidiEvent(new NoteOnMessage(Channel.of(1), Note.of("B4"), ContinuousControlValue.of(63)).getMessage(), 12),
//                new MidiEvent(new NoteOffMessage(Channel.of(1), Note.of("B4"), ContinuousControlValue.of(63)).getMessage(), 14),
//                new MidiEvent(new NoteOnMessage(Channel.of(1), Note.of("E5"), ContinuousControlValue.of(63)).getMessage(), 14),
//                new MidiEvent(new NoteOffMessage(Channel.of(1), Note.of("E5"), ContinuousControlValue.of(63)).getMessage(), 16)
//                )
//        .forEach(pianoTrack::add);

        // loop
        final Track drumTrack = s.createTrack();
        Stream.<MidiEvent>of(
                new MidiEvent(new NoteOnMessage(Channel.of(1), Note.of("C1"), ContinuousControlValue.of(100)).getMessage(), 0),
                new MidiEvent(new NoteOffMessage(Channel.of(1), Note.of("C1"), ContinuousControlValue.of(100)).getMessage(), 1),
                new MidiEvent(new NoteOnMessage(Channel.of(1), Note.of("C1"), ContinuousControlValue.of(100)).getMessage(), 3),
                new MidiEvent(new NoteOffMessage(Channel.of(1), Note.of("C1"), ContinuousControlValue.of(100)).getMessage(), 4),
                new MidiEvent(new NoteOnMessage(Channel.of(1), Note.of("C#1"), ContinuousControlValue.of(100)).getMessage(), 4),
                new MidiEvent(new NoteOffMessage(Channel.of(1), Note.of("C#1"), ContinuousControlValue.of(100)).getMessage(), 5),
                new MidiEvent(new NoteOnMessage(Channel.of(1), Note.of("C1"), ContinuousControlValue.of(100)).getMessage(), 10),
                new MidiEvent(new NoteOffMessage(Channel.of(1), Note.of("C1"), ContinuousControlValue.of(100)).getMessage(), 11),
                new MidiEvent(new NoteOnMessage(Channel.of(1), Note.of("C#1"), ContinuousControlValue.of(100)).getMessage(), 12),
                new MidiEvent(new NoteOnMessage(Channel.of(1), Note.of("D1"), ContinuousControlValue.of(100)).getMessage(), 12),
                new MidiEvent(new NoteOffMessage(Channel.of(1), Note.of("C#1"), ContinuousControlValue.of(100)).getMessage(), 16),
                new MidiEvent(new NoteOffMessage(Channel.of(1), Note.of("D1"), ContinuousControlValue.of(100)).getMessage(), 16)
                )
        .forEach(drumTrack::add);

        MidiSystem.write(s, 0, new File("./mjilt-percloop-01.mid"));
    }

}
