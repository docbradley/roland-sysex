package com.adamdbradley.midi.util;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.Receiver;

import com.adamdbradley.midi.DeviceFinder;
import com.adamdbradley.midi.MidiUtils;
import com.adamdbradley.midi.domain.BitMask;
import com.adamdbradley.midi.domain.Channel;
import com.adamdbradley.midi.domain.ContinuousControlValue;
import com.adamdbradley.midi.domain.Modulation;
import com.adamdbradley.midi.domain.Note;
import com.adamdbradley.midi.domain.Pan;
import com.adamdbradley.midi.domain.ProgramChange;
import com.adamdbradley.midi.domain.ProgramSender;
import com.adamdbradley.midi.domain.Volume;
import com.adamdbradley.midi.sysex.roland.UpdatingModel;
import com.adamdbradley.midi.sysex.roland.a90.BiasControlValue;
import com.adamdbradley.midi.sysex.roland.a90.CentBiasControlValue;
import com.adamdbradley.midi.sysex.roland.a90.ControllerAssignModel;
import com.adamdbradley.midi.sysex.roland.a90.EQBiasControlValue;
import com.adamdbradley.midi.sysex.roland.a90.ExternalZone;
import com.adamdbradley.midi.sysex.roland.a90.InternalZone;
import com.adamdbradley.midi.sysex.roland.a90.PerformanceModel;
import com.adamdbradley.midi.sysex.roland.a90.SystemCommonModel;
import com.adamdbradley.midi.sysex.roland.a90.Transposition;
import com.adamdbradley.midi.sysex.roland.a90.VERD1FX;
import com.adamdbradley.midi.sysex.roland.a90.VERD1FX.ChorusOutput;
import com.adamdbradley.midi.sysex.roland.a90.VERD1FX.Frequency;
import com.adamdbradley.midi.sysex.roland.a90.VERD1FX.ReverbType;
import com.adamdbradley.midi.sysex.roland.a90.VelocityCurve;
import com.adamdbradley.midi.sysex.roland.a90.VolumeRange;
import com.adamdbradley.midi.sysex.roland.a90.VelocityCurve.CurveType;
import com.adamdbradley.midi.sysex.roland.a90.ZoneModel;
import com.adamdbradley.midi.sysex.roland.a90.SystemCommonModel.PanelMode;
import com.google.common.collect.ImmutableList;

/**
 * Useful.
 */
public class AdamA90Program20241216 {

    public static void main(final String[] args) throws Exception {
        if (args.length == 0) {
            MidiUtils.enumerateDevices()
                    .forEach(md -> System.err.println(md.getDeviceInfo()));
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
        final ImmutableList.Builder<UpdatingModel> updates = ImmutableList.builder();

        updates.addAll(buildSystem());

        if (args.size() == 1) {
            final int i = Integer.parseInt(args.get(0));
            updates.add(buildPatch(i));
        }

        return new ProgramSender(updates.build());
    }

    private static ImmutableList<UpdatingModel> buildSystem() {
        return ImmutableList.of(
                SystemCommonModel.builder()
                .panelMode(PanelMode.PERFORMANCE)
                .performanceNumber(ProgramChange.of(1))
                .chainNumber(ProgramChange.of(1))
                .controlChannelSwitch(true)
                .controlChannel(Channel.of(16))
                .midiOutEnables(BitMask.FOUR_ON)
                .midiOutSequencerEnables(BitMask.FOUR_ON)
                .globalKeyTranspose(BiasControlValue.of(-12))
                .vExpEnable(true)
                .build(),
                ControllerAssignModel.builder()
                .build()
                );
    }

    /**
     * @param i 1.64 (internal; card cannot be addressed)
     * @return
     */
    private static PerformanceModel buildPatch(final int i) {
        final PerformanceModel.PerformanceModelBuilder builder = defaultPatch(i);
        switch(i) {
        case 1:
            return builder
                    .performanceName("SummitControl 24")
                    .internalZones(ImmutableList.of(
                            defaultInternalZone()
                            .midiChannel(Channel.of(1))
                            .programChange(Optional.of(ProgramChange.of(5)))
                            .build(),
                            defaultInternalZone()
                            .midiChannel(Channel.of(2))
                            .programChange(Optional.of(ProgramChange.of(46)))
                            .build(),
                            defaultInternalZone()
                            .midiChannel(Channel.of(3))
                            .programChange(Optional.of(ProgramChange.of(80)))
                            .build(),
                            defaultInternalZone()
                            .midiChannel(Channel.of(4))
                            .programChange(Optional.of(ProgramChange.of(85)))
                            .build()
                            ))
                    .externalZones(ImmutableList.of(
                            defaultExternalZone()
                            .midiChannel(Channel.of(1))
                            .build(),
                            defaultExternalZone()
                            .midiChannel(Channel.of(2))
                            .build(),
                            defaultExternalZone()
                            .midiChannel(Channel.of(3))
                            .build(),
                            defaultExternalZone()
                            .midiChannel(Channel.of(4))
                            .build()
                            ))
                    .effectorSetups(ImmutableList.of())
                    .build();
        default:
            throw new IllegalArgumentException();
        }
    }

    @SuppressWarnings("unchecked")
    private static <T extends ZoneModel.ZoneModelBuilder<?, ?>>T populateZone(final T builder) {
        return (T) builder
                .aftertouch(Optional.empty())
                .aftertouchRange(Optional.of(VolumeRange.FULL))
                .aftertouchSliderRange(Optional.of(VolumeRange.FULL))
                .aux1(Optional.empty())
                .aux2(Optional.empty())
                .bankSelect(Optional.empty())
                .bendLeverRange(Optional.empty())
                .breathControllerRange(Optional.of(VolumeRange.ZERO))
                .chorusSendLevel(Optional.of(Volume.ZERO))
                .comment("snap20241216")
                .expression(Optional.of(Volume.NOMINAL))
                .exprSliderRange(Optional.of(VolumeRange.FULL))
                .footController1Range(Optional.of(VolumeRange.FULL))
                .footController2Range(Optional.of(VolumeRange.FULL))
                .footSwitch1Values(Optional.of(VolumeRange.FULL))
                .footSwitch2Values(Optional.of(VolumeRange.FULL))
                .globalTranspose(false)
                .holdPedal(true)
                .keyLower(Note.of(0))
                .keyUpper(Note.of(127))
                .localKeyboardSwitch(true)
                .midiChannel(Channel.of(1))
                .midiOuts(BitMask.FOUR_ON)
                .modLeverRange(Optional.of(VolumeRange.FULL))
                .modulation(Optional.of(Modulation.ZERO))
                .monoSwitchValues(Optional.of(VolumeRange.FULL))
                .pan(Optional.of(Pan.CENTER))
                .portamentoTime(Optional.of(ContinuousControlValue.MID))
                .programChange(Optional.empty())
                .portamentoTimeSliderRange(Optional.of(VolumeRange.FULL))
                .portamentoSwitchValues(Optional.of(VolumeRange.FULL))
                .reverbSendLevel(Optional.of(Volume.ZERO))
                .totalVolumePedal(true)
                .totalVolumeSlider(true)
                .transpose(Transposition.ZERO)
                .velocityCurve(VelocityCurve.builder()
                        .curve(CurveType.Unknown0)
                        .max(ContinuousControlValue.MAX)
                        .sense(ContinuousControlValue.ZERO)
                        .build())
                .volume(Optional.of(Volume.of(100)))
                .wheel1Range(Optional.of(VolumeRange.FULL))
                .wheel2Range(Optional.of(VolumeRange.FULL))
                .zoneSwitch(true);
    }

    private static ExternalZone.ExternalZoneBuilder<?, ?> defaultExternalZone() {
        return (ExternalZone.ExternalZoneBuilder<?, ?>) populateZone(ExternalZone.builder());
    }

    private static InternalZone.InternalZoneBuilder<?, ?> defaultInternalZone() {
        return ((InternalZone.InternalZoneBuilder<?, ?>) populateZone(InternalZone.builder()))
                .attackTime(CentBiasControlValue.ZERO)
                .brightValue(CentBiasControlValue.ZERO)
                .decayTime(CentBiasControlValue.ZERO)
                .fineTune(CentBiasControlValue.ZERO)
                .releaseTime(CentBiasControlValue.ZERO);
    }

    private static PerformanceModel.PerformanceModelBuilder defaultPatch(final int patchNumber) {
        return PerformanceModel.builder()
                .patchNumber(patchNumber)
                .performanceName("Default    (ADB)")
                .effectorSetups(ImmutableList.of(// max of 4
                        ))
                .internalZones(ImmutableList.of(
                        defaultInternalZone()
                        .build(),
                        defaultInternalZone()
                        .build(),
                        defaultInternalZone()
                        .build(),
                        defaultInternalZone()
                        .build()
                        ))
                .externalZones(ImmutableList.of(
                        defaultExternalZone()
                        .build(),
                        defaultExternalZone()
                        .build(),
                        defaultExternalZone()
                        .build(),
                        defaultExternalZone()
                        .build()
                        ))
                .in2toInternal(true)
                .in2toOuts(BitMask.EMPTY)
                .remoteSwitchExts(BitMask.ONE_ON)
                .remoteSwitchInts(BitMask.ONE_ON)
                .songChange(Optional.empty())
                .tempoChange(Optional.empty())
                .verd1FX(VERD1FX.builder()
                        .chorusDepth(ContinuousControlValue.ZERO)
                        .chorusFeedback(Volume.ZERO)
                        .chorusLevel(Volume.ZERO)
                        .chorusOutput(ChorusOutput.Mix)
                        .chorusPreDelay(ContinuousControlValue.ZERO)
                        .chorusRate(ContinuousControlValue.ZERO)
                        .eqHighGain(EQBiasControlValue.of(0))
                        .eqLowGain(EQBiasControlValue.of(0))
                        .eqMidFrequency(Frequency.F800)
                        .eqMidGain(EQBiasControlValue.of(0))
                        .reverbFeedback(Volume.of(0))
                        .reverbHighFrequencyDamp(Frequency.BYPASS)
                        .reverbLevel(Volume.of(0))
                        .reverbTime(ContinuousControlValue.ZERO)
                        .reverbType(ReverbType.Hall1)
                        .build())
                ;
    }

}
