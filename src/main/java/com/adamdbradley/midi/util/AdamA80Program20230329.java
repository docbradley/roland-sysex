package com.adamdbradley.midi.util;

import java.io.File;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import java.util.Optional;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;

import com.adamdbradley.midi.DeviceFinder;
import com.adamdbradley.midi.MidiUtils;
import com.adamdbradley.midi.domain.Channel;
import com.adamdbradley.midi.domain.ContinuousControllerId;
import com.adamdbradley.midi.domain.Note;
import com.adamdbradley.midi.domain.ProgramChange;
import com.adamdbradley.midi.domain.ProgramSender;
import com.adamdbradley.midi.domain.Volume;
import com.adamdbradley.midi.sysex.roland.a80.PatchModel;
import com.adamdbradley.midi.sysex.roland.a80.PatchNumber;
import com.adamdbradley.midi.sysex.roland.a80.ZoneModel;
import com.adamdbradley.midi.sysex.roland.a80.PatchModel.PatchModelBuilder;
import com.adamdbradley.midi.sysex.roland.a80.ZoneModel.ZoneModelBuilder;
import com.google.common.collect.ImmutableList;

/**
 * Useful.
 */
public class AdamA80Program20230329 {

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            MidiUtils.enumerateDevices()
                    .forEach(md -> System.err.println(md.getDeviceInfo()));
        } else if (args[0].equals("file")) {
            final ProgramSender program = buildProgram(Arrays.asList(args).subList(1, args.length));
            final Sequence s = new Sequence(Sequence.PPQ, 4);
            final Track t = s.createTrack();
            program.accept(new Receiver() {
                @Override
                public void send(final MidiMessage message, final long tick) {
                    t.add(new MidiEvent(message, tick));
                }
                @Override
                public void close() {
                }
            });
            final File target = new File("./" + AdamA80Program20230329.class.getSimpleName() + ".mid");
            MidiSystem.write(s, 0, target);
        } else {
            final ProgramSender program = buildProgram(Arrays.asList(args).subList(1, args.length));
            try (MidiDevice device = new DeviceFinder().find(
                    DeviceFinder.MUST_HAVE_OUTPUT,
                    DeviceFinder.regexFilter(args[0])).findAny().get()) {
                if (!device.isOpen()) {
                    device.open();
                }
                try (Receiver receiver = device.getReceiver()) {
                    System.err.println("Sending to " + device.getDeviceInfo().getName() + "...");
                    program.accept(receiver);
                    System.err.println("...Done");
                }
            }
        }
    }

    public static ProgramSender buildProgram(final List<String> args) {
        if (args.isEmpty()) {
            final ImmutableList.Builder<PatchModel> builder = ImmutableList.builder();
            for (int i=1; i<=64; i++) {
                builder.add(buildPatch(i));
            }
            final List<PatchModel> patchModels = builder.build();
            return new ProgramSender(patchModels);
        } else {
            final int i = Integer.parseInt(args.get(0));
            return new ProgramSender(ImmutableList.of(buildPatch(i)));
        }
    }

    /**
     * 
     * @param i 1..64
     * @return
     */
    private static PatchModel buildPatch(final int i) {
        switch(i) {
        case 1:
            return defaultPatch(1)
                    .patchName("Summit Base")
                    .midiOutputUnmuted(BitSet.valueOf(new long[] { 0xF }))
                    .zones(ImmutableList.of(
                            ZoneModel.builder()
                                    .channel(Channel.of(1))
                                    .startKey(Note.of(0)).endKey(Note.of(127))
                                    .unmuted(true)
                                    .volume(Volume.of(100))
                                    .programChange(ProgramChange.of(1))
                                    .pedalControls(ImmutableList.of(
                                            Optional.of(ContinuousControllerId.StandardControllers.Balanace.get()),
                                            Optional.of(ContinuousControllerId.StandardControllers.ChorusLevel.get()),
                                            Optional.of(ContinuousControllerId.StandardControllers.LegatoPedal.get()),
                                            Optional.of(ContinuousControllerId.StandardControllers.HoldPedal.get())
                                            ))
                                    .sliderControls(ImmutableList.of(
                                            Optional.of(ContinuousControllerId.StandardControllers.Volume.get()),
                                            Optional.of(ContinuousControllerId.StandardControllers.Expression.get()),
                                            Optional.of(ContinuousControllerId.StandardControllers.BreathController.get()),
                                            Optional.of(ContinuousControllerId.StandardControllers.PortamentoTime.get())
                                            ))
                                    .switchControls(ImmutableList.of(
                                            Optional.of(ContinuousControllerId.StandardControllers.SoftPedal.get()),
                                            Optional.of(ContinuousControllerId.StandardControllers.SustenutoPedal.get()),
                                            Optional.of(ContinuousControllerId.StandardControllers.Hold2Pedal.get()),
                                            Optional.of(ContinuousControllerId.StandardControllers.Portamento.get()),
                                            Optional.empty(), Optional.empty(), Optional.empty()
                                            ))
                                    .build(),
                            ZoneModel.builder()
                                    .channel(Channel.of(2))
                                    .startKey(Note.of(0)).endKey(Note.of(0))
                                    .unmuted(true)
                                    .volume(Volume.of(100))
                                    .programChange(ProgramChange.of(1))
                                    .pedalControls(ImmutableList.of(
                                            Optional.of(ContinuousControllerId.StandardControllers.Balanace.get()),
                                            Optional.of(ContinuousControllerId.StandardControllers.ChorusLevel.get()),
                                            Optional.of(ContinuousControllerId.StandardControllers.LegatoPedal.get()),
                                            Optional.of(ContinuousControllerId.StandardControllers.HoldPedal.get())
                                            ))
                                    .sliderControls(ImmutableList.of(
                                            Optional.of(ContinuousControllerId.StandardControllers.Volume.get()),
                                            Optional.of(ContinuousControllerId.StandardControllers.Expression.get()),
                                            Optional.of(ContinuousControllerId.StandardControllers.BreathController.get()),
                                            Optional.of(ContinuousControllerId.StandardControllers.PortamentoTime.get())
                                            ))
                                    .switchControls(ImmutableList.of(
                                            Optional.of(ContinuousControllerId.StandardControllers.SoftPedal.get()),
                                            Optional.of(ContinuousControllerId.StandardControllers.SustenutoPedal.get()),
                                            Optional.of(ContinuousControllerId.StandardControllers.Hold2Pedal.get()),
                                            Optional.of(ContinuousControllerId.StandardControllers.Portamento.get()),
                                            Optional.empty(), Optional.empty(), Optional.empty()
                                            ))
                                    .build(),
                            ZoneModel.builder()
                                    .channel(Channel.of(3))
                                    .startKey(Note.of(0)).endKey(Note.of(0))
                                    .unmuted(true)
                                    .volume(Volume.of(100))
                                    .programChange(ProgramChange.of(1))
                                    .pedalControls(ImmutableList.of(
                                            Optional.of(ContinuousControllerId.StandardControllers.Balanace.get()),
                                            Optional.of(ContinuousControllerId.StandardControllers.ChorusLevel.get()),
                                            Optional.of(ContinuousControllerId.StandardControllers.LegatoPedal.get()),
                                            Optional.of(ContinuousControllerId.StandardControllers.HoldPedal.get())
                                            ))
                                    .sliderControls(ImmutableList.of(
                                            Optional.of(ContinuousControllerId.StandardControllers.Volume.get()),
                                            Optional.of(ContinuousControllerId.StandardControllers.Expression.get()),
                                            Optional.of(ContinuousControllerId.StandardControllers.BreathController.get()),
                                            Optional.of(ContinuousControllerId.StandardControllers.PortamentoTime.get())
                                            ))
                                    .switchControls(ImmutableList.of(
                                            Optional.of(ContinuousControllerId.StandardControllers.SoftPedal.get()),
                                            Optional.of(ContinuousControllerId.StandardControllers.SustenutoPedal.get()),
                                            Optional.of(ContinuousControllerId.StandardControllers.Hold2Pedal.get()),
                                            Optional.of(ContinuousControllerId.StandardControllers.Portamento.get()),
                                            Optional.empty(), Optional.empty(), Optional.empty()
                                            ))
                                    .build(),
                            ZoneModel.builder()
                                    .channel(Channel.of(4))
                                    .startKey(Note.of(0)).endKey(Note.of(0))
                                    .unmuted(true)
                                    .volume(Volume.of(100))
                                    .programChange(ProgramChange.of(1))
                                    .pedalControls(ImmutableList.of(
                                            Optional.of(ContinuousControllerId.StandardControllers.Balanace.get()),
                                            Optional.of(ContinuousControllerId.StandardControllers.ChorusLevel.get()),
                                            Optional.of(ContinuousControllerId.StandardControllers.LegatoPedal.get()),
                                            Optional.of(ContinuousControllerId.StandardControllers.HoldPedal.get())
                                            ))
                                    .sliderControls(ImmutableList.of(
                                            Optional.of(ContinuousControllerId.StandardControllers.Volume.get()),
                                            Optional.of(ContinuousControllerId.StandardControllers.Expression.get()),
                                            Optional.of(ContinuousControllerId.StandardControllers.BreathController.get()),
                                            Optional.of(ContinuousControllerId.StandardControllers.PortamentoTime.get())
                                            ))
                                    .switchControls(ImmutableList.of(
                                            Optional.of(ContinuousControllerId.StandardControllers.SoftPedal.get()),
                                            Optional.of(ContinuousControllerId.StandardControllers.SustenutoPedal.get()),
                                            Optional.of(ContinuousControllerId.StandardControllers.Hold2Pedal.get()),
                                            Optional.of(ContinuousControllerId.StandardControllers.Portamento.get()),
                                            Optional.empty(), Optional.empty(), Optional.empty()
                                            ))
                                    .build()
                            ))
                    .build();
        case 2: case 3: case 4: case 5: case 6: case 7: case 8:
        case 9: case 10: case 11: case 12: case 13: case 14: case 15: case 16:
            return defaultPatch(i)
                    .build();
        case 17:
            return basePatch(17, "MIDI 1:01-04", 1)
                    .midiOutputUnmuted(BitSet.valueOf(new long[] { 0x1 }))
                    .build();
        case 18:
            return basePatch(18, "MIDI 1:05-08", 5)
                    .midiOutputUnmuted(BitSet.valueOf(new long[] { 0x1 }))
                    .build();
        case 19:
            return basePatch(19, "MIDI 1:09-12", 9)
                    .midiOutputUnmuted(BitSet.valueOf(new long[] { 0x1 }))
                    .build();
        case 20:
            return basePatch(20, "MIDI 1:13-16", 13)
                    .midiOutputUnmuted(BitSet.valueOf(new long[] { 0x1 }))
                    .build();
        case 21:
            return basePatch(21, "MIDI 2:01-04", 1)
                    .midiOutputUnmuted(BitSet.valueOf(new long[] { 0x2 }))
                    .build();
        case 22:
            return basePatch(22, "MIDI 2:05-08", 5)
                    .midiOutputUnmuted(BitSet.valueOf(new long[] { 0x2 }))
                    .build();
        case 23:
            return basePatch(23, "MIDI 2:09-12", 9)
                    .midiOutputUnmuted(BitSet.valueOf(new long[] { 0x2 }))
                    .build();
        case 24:
            return basePatch(24, "MIDI 2:13-16", 13)
                    .midiOutputUnmuted(BitSet.valueOf(new long[] { 0x2 }))
                    .build();
        case 25:
            return basePatch(25, "MIDI 3:01-04", 1)
                    .midiOutputUnmuted(BitSet.valueOf(new long[] { 0x4 }))
                    .build();
        case 26:
            return basePatch(26, "MIDI 3:05-08", 5)
                    .midiOutputUnmuted(BitSet.valueOf(new long[] { 0x4 }))
                    .build();
        case 27:
            return basePatch(27, "MIDI 3:09-12", 9)
                    .midiOutputUnmuted(BitSet.valueOf(new long[] { 0x4 }))
                    .build();
        case 28:
            return basePatch(28, "MIDI 3:13-16", 13)
                    .midiOutputUnmuted(BitSet.valueOf(new long[] { 0x4 }))
                    .build();
        case 29:
            return basePatch(29, "MIDI 4:01-04", 1)
                    .midiOutputUnmuted(BitSet.valueOf(new long[] { 0x8 }))
                    .build();
        case 30:
            return basePatch(30, "MIDI 4:05-08", 5)
                    .midiOutputUnmuted(BitSet.valueOf(new long[] { 0x8 }))
                    .build();
        case 31:
            return basePatch(31, "MIDI 4:09-12", 9)
                    .midiOutputUnmuted(BitSet.valueOf(new long[] { 0x8 }))
                    .build();
        case 32:
            return basePatch(32, "MIDI 4:13-16", 13)
                    .midiOutputUnmuted(BitSet.valueOf(new long[] { 0x8 }))
                    .build();
        default:
            return defaultPatch(i)
                    .build();
        }
    }

    private static PatchModelBuilder basePatch(final int patchNumber, final String name, final int startChannel) {
        return PatchModel.builder()
            .patchNumber(PatchNumber.of(patchNumber))
            .patchName(name)
            .zones(ImmutableList.of(baseZone(startChannel).build(),
                    baseZone(startChannel + 1).build(),
                    baseZone(startChannel + 2).build(),
                    baseZone(startChannel + 3).build()));
    }

    private static ZoneModelBuilder baseZone(final int channel) {
        return ZoneModel.builder()
                .channel(Channel.of(channel))
                .programChangePresentation(ZoneModel.ProgramChangePresentation.Decimal128)
                .programChange(ProgramChange.of(1))
                .startKey(Note.of(0)).endKey(Note.of(127))
                .pedalControls(defaultPedalControls())
                .sliderControls(fourOptionalsPosition((channel - 1) % 4,
                        ContinuousControllerId.StandardControllers.Volume.get()))
                .switchControls(fourOptionalsPosition((channel - 1) % 4,
                        ContinuousControllerId.StandardControllers.HoldPedal.get()))
                .unmuted(true)
                .volume(Volume.of(100));
    }

    private static <T> List<Optional<T>> fourOptionalsPosition(final int position,
            final T value) {
        if (position < 0 || position > 3) {
            throw new IllegalArgumentException();
        }
        return ImmutableList.of(
                (position == 0) ? Optional.of(value) : Optional.empty(),
                (position == 1) ? Optional.of(value) : Optional.empty(),
                (position == 2) ? Optional.of(value) : Optional.empty(),
                (position == 3) ? Optional.of(value) : Optional.empty()
                );
    }

    private static List<Optional<ContinuousControllerId>> defaultPedalControls() {
        return ImmutableList.of(
                Optional.of(ContinuousControllerId.of(64)), // Sustain
                Optional.of(ContinuousControllerId.of(65)), // Portamento
                Optional.of(ContinuousControllerId.of(11)), // Expression (post-volume)
                Optional.of(ContinuousControllerId.of(5)) // Portamento Time
                );
    }

    private static PatchModel.PatchModelBuilder defaultPatch(final int patchNumber) {
        return PatchModel.builder()
                .patchNumber(PatchNumber.of(patchNumber))
                .patchName("Default    (ADB)")
                .midiOutputUnmuted(BitSet.valueOf(new long[] { 0x1 }))
                .zones(ImmutableList.of(
                        ZoneModel.builder()
                                .channel(Channel.of(1))
                                .volume(Volume.of(127))
                                .pedalControls(defaultPedalControls())
                                .sliderControls(ImmutableList.of(
                                        Optional.of(ContinuousControllerId.StandardControllers.Volume.get()),
                                        Optional.empty(), Optional.empty(), Optional.empty()
                                        ))
                                .switchControls(ImmutableList.of(
                                        Optional.of(ContinuousControllerId.StandardControllers.HoldPedal.get()),
                                        Optional.empty(), Optional.empty(), Optional.empty()
                                        ))
                                .build(),
                        ZoneModel.builder()
                                .startKey(Note.of(0)).endKey(Note.of(0))
                                .unmuted(false)
                                .build(),
                        ZoneModel.builder()
                                .startKey(Note.of(0)).endKey(Note.of(0))
                                .unmuted(false)
                                .build(),
                        ZoneModel.builder()
                                .startKey(Note.of(0)).endKey(Note.of(0))
                                .unmuted(false)
                                .build()
                        ));
    }

}
