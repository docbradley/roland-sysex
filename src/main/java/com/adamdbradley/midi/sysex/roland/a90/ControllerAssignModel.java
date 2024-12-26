package com.adamdbradley.midi.sysex.roland.a90;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import com.adamdbradley.midi.domain.BitMask;
import com.adamdbradley.midi.domain.ContinuousControlValue;
import com.adamdbradley.midi.domain.ContinuousControllerId;
import com.adamdbradley.midi.domain.ContinuousControllerId.StandardControllers;
import com.adamdbradley.midi.message.Message;
import com.adamdbradley.midi.sysex.roland.InstrumentModel;
import com.adamdbradley.midi.sysex.roland.RolandDataSetCommand;
import com.adamdbradley.midi.sysex.roland.RolandSysexCommand;
import com.adamdbradley.midi.sysex.roland.RolandSysexMessage;
import com.google.common.collect.ImmutableList;

/**
 * Represents a single "Performance" for the A-90 controller.
 */
@Builder
@EqualsAndHashCode(callSuper = false)
@Getter
@RequiredArgsConstructor
@ToString
public class ControllerAssignModel
extends A90Foundations
implements com.adamdbradley.midi.sysex.roland.UpdatingModel {

    @Builder.Default @Nonnull private final Assign breathSlider = Assign.builder().cc(StandardControllers.BreathController.get());
    @Builder.Default @Nonnull private final Assign aftertouchSlider = Assign.builder().channelAftertouch();
    @Builder.Default @Nonnull private final Assign expressionSlider = Assign.builder().cc(StandardControllers.Expression.get());
    @Builder.Default @Nonnull private final Assign portamentoTimeSlider = Assign.builder().cc(StandardControllers.PortamentoTime.get());
    @Builder.Default @Nonnull private final Assign footController1 = Assign.builder().cc(StandardControllers.ReverbLevel.get());
    @Builder.Default @Nonnull private final Assign footController2 = Assign.builder().cc(StandardControllers.Expression.get());
    @Builder.Default @Nonnull private final Assign footSwitch1 = Assign.builder().cc(StandardControllers.SoftPedal.get());
    @Builder.Default @Nonnull private final Assign footSwitch2 = Assign.builder().cc(StandardControllers.SustenutoPedal.get());
    @Builder.Default @Nonnull private final Assign aftertouch = Assign.builder().channelAftertouch();
    @Builder.Default @Nonnull private final Assign wheel1 = Assign.builder().cc(StandardControllers.ModulationWheel.get());
    @Builder.Default @Nonnull private final Assign wheel2 = Assign.builder().pitchBend();
    @Builder.Default @Nonnull private final Assign bendLever = Assign.builder().pitchBend();
    @Builder.Default @Nonnull private final Assign modulationLever = Assign.builder().cc(StandardControllers.ModulationWheel.get());
    @Builder.Default @Nonnull private final Assign breath = Assign.builder().cc(StandardControllers.BreathController.get());
    @Builder.Default @Nonnull private final ModeAssign monoSwitch = ModeAssign.builder().monoPoly();
    @Builder.Default @Nonnull private final ModeAssign portamentoSwitch = ModeAssign.builder().cc(StandardControllers.Portamento.get());
    @Builder.Default @Nonnull private final AuxAssign aux1extA = AuxAssign.builder().off();
    @Builder.Default @Nonnull private final AuxAssign aux1extB = AuxAssign.builder().off();
    @Builder.Default @Nonnull private final AuxAssign aux1extC = AuxAssign.builder().off();
    @Builder.Default @Nonnull private final AuxAssign aux1extD = AuxAssign.builder().off();
    @Builder.Default @Nonnull private final AuxAssign aux1intA = AuxAssign.builder().off();
    @Builder.Default @Nonnull private final AuxAssign aux1intB = AuxAssign.builder().off();
    @Builder.Default @Nonnull private final AuxAssign aux1intC = AuxAssign.builder().off();
    @Builder.Default @Nonnull private final AuxAssign aux1intD = AuxAssign.builder().off();
    @Builder.Default @Nonnull private final AuxAssign aux2extA = AuxAssign.builder().off();
    @Builder.Default @Nonnull private final AuxAssign aux2extB = AuxAssign.builder().off();
    @Builder.Default @Nonnull private final AuxAssign aux2extC = AuxAssign.builder().off();
    @Builder.Default @Nonnull private final AuxAssign aux2extD = AuxAssign.builder().off();
    @Builder.Default @Nonnull private final AuxAssign aux2intA = AuxAssign.builder().off();
    @Builder.Default @Nonnull private final AuxAssign aux2intB = AuxAssign.builder().off();
    @Builder.Default @Nonnull private final AuxAssign aux2intC = AuxAssign.builder().off();
    @Builder.Default @Nonnull private final AuxAssign aux2intD = AuxAssign.builder().off();
    @Builder.Default @Nonnull private final BitMask extZonesCCResetWithPerfChange = BitMask.FOUR_ON;
    @Builder.Default @Nonnull private final BitMask intZonesCCResetWithPerfChange = BitMask.FOUR_ON;

    @Builder
    @EqualsAndHashCode
    @Getter
    @RequiredArgsConstructor
    @ToString
    public static class Assign {
        @Nonnull private final AssignType assignType;
        // TODO: should be a one-of-three-types union
        @Builder.Default @Nonnull private final Optional<ContinuousControllerId> controllerMessageType = Optional.empty();
        @Builder.Default @Nonnull private final Optional<ChannelMessageType> channelMessageType = Optional.empty();
        @Builder.Default @Nonnull private final Optional<PolyphonicAftertouchTrigger> polyphonicAftertouchTrigger = Optional.empty();
        @Builder.Default @Nonnull private final Optional<OtherMessageType> otherMessageType = Optional.empty();

        public static class AssignBuilder {
            public Assign off() {
                return this.assignType(AssignType.OFF)
                        .build();
            }
            public Assign cc(final ContinuousControllerId id) {
                return this.assignType(AssignType.CC)
                        .controllerMessageType(Optional.of(id))
                        .build();
            }
            public Assign channelAftertouch() {
                return this.assignType(AssignType.CHANNEL_MESSAGE)
                        .channelMessageType(Optional.of(ChannelMessageType.CHANNEL_AFTERTOUCH))
                        .build();
            }
            public Assign polyAftertouch(final PolyphonicAftertouchTrigger trigger) {
                return this.assignType(AssignType.CHANNEL_MESSAGE)
                        .channelMessageType(Optional.of(ChannelMessageType.POLYPHONIC_AFTERTOUCH))
                        .polyphonicAftertouchTrigger(Optional.of(trigger))
                        .build();
            }
            public Assign pitchBend() {
                return this.assignType(AssignType.CHANNEL_MESSAGE)
                        .channelMessageType(Optional.of(ChannelMessageType.PITCH_BEND))
                        .build();
            }
            public Assign tempo() {
                return this.assignType(AssignType.OTHERS)
                        .otherMessageType(Optional.of(OtherMessageType.TEMPO))
                        .build();
            }
            public Assign programUp() {
                return this.assignType(AssignType.OTHERS)
                        .otherMessageType(Optional.of(OtherMessageType.PROGRAM_UP))
                        .build();
            }
            public Assign programDown() {
                return this.assignType(AssignType.OTHERS)
                        .otherMessageType(Optional.of(OtherMessageType.PROGRAM_DOWN))
                        .build();
            }
        }

        public byte[] build() {
            return new byte[] {
                    (byte) assignType.ordinal(),
                    (byte) controllerMessageType.map(ContinuousControllerId::getData).orElse((byte) 0),
                    (byte) channelMessageType.map(Enum::ordinal).orElse(0).byteValue(),
                    (byte) polyphonicAftertouchTrigger.map(Enum::ordinal).orElse(0).byteValue(),
                    (byte) otherMessageType.map(Enum::ordinal).orElse(0).byteValue()
            };
        }
    }
    public enum AssignType {
        OFF,
        CC,
        CHANNEL_MESSAGE,
        OTHERS
    }
    public enum ChannelMessageType {
        CHANNEL_AFTERTOUCH,
        POLYPHONIC_AFTERTOUCH,
        PITCH_BEND
    }
    public enum OtherMessageType {
        TEMPO,
        PROGRAM_UP,
        PROGRAM_DOWN,
    }

    @Builder
    @EqualsAndHashCode
    @Getter
    @RequiredArgsConstructor
    @ToString
    public static class ModeAssign {
        @Nonnull private final ModeAssignType assignType;
        // TODO: should be a one-of-four-types union
        @Builder.Default @Nonnull private final Optional<ContinuousControllerId> controllerMessageType = Optional.empty();
        @Builder.Default @Nonnull private final Optional<ChannelMessageType> channelMessageType = Optional.empty();
        @Builder.Default @Nonnull private final Optional<ModeMessageType> modeMessageType = Optional.empty();
        @Builder.Default @Nonnull private final Optional<PolyphonicAftertouchTrigger> polyphonicAftertouchTrigger = Optional.empty();
        @Builder.Default @Nonnull private final Optional<Integer> autoFadeOutTime = Optional.empty();
        @Builder.Default @Nonnull private final Optional<ModeOtherMessageType> otherMessageType = Optional.empty();

        public static class ModeAssignBuilder {
            public ModeAssign off() {
                return this.assignType(ModeAssignType.OFF)
                        .build();
            }
            public ModeAssign cc(final ContinuousControllerId id) {
                return this.assignType(ModeAssignType.CC)
                        .controllerMessageType(Optional.of(id))
                        .build();
            }
            public ModeAssign channelAftertouch() {
                return this.assignType(ModeAssignType.CHANNEL_MESSAGE)
                        .channelMessageType(Optional.of(ChannelMessageType.CHANNEL_AFTERTOUCH))
                        .build();
            }
            public ModeAssign polyAftertouch(final PolyphonicAftertouchTrigger trigger) {
                return this.assignType(ModeAssignType.CHANNEL_MESSAGE)
                        .channelMessageType(Optional.of(ChannelMessageType.POLYPHONIC_AFTERTOUCH))
                        .polyphonicAftertouchTrigger(Optional.of(trigger))
                        .otherMessageType(Optional.empty())
                        .build();
            }
            public ModeAssign allSoundOff() {
                return this.assignType(ModeAssignType.MODE_MESSAGE)
                        .modeMessageType(Optional.of(ModeMessageType.ALL_SOUND_OFF))
                        .build();
            }
            public ModeAssign resetAllControllers() {
                return this.assignType(ModeAssignType.MODE_MESSAGE)
                        .modeMessageType(Optional.of(ModeMessageType.RESET_ALL_CONTROLLERS))
                        .build();
            }
            public ModeAssign localControl() {
                return this.assignType(ModeAssignType.MODE_MESSAGE)
                        .modeMessageType(Optional.of(ModeMessageType.LOCAL_CONTROL))
                        .build();
            }
            public ModeAssign allNoteOff() {
                return this.assignType(ModeAssignType.MODE_MESSAGE)
                        .modeMessageType(Optional.of(ModeMessageType.ALL_NOTE_OFF))
                        .build();
            }
            public ModeAssign omniOnOff() {
                return this.assignType(ModeAssignType.MODE_MESSAGE)
                        .modeMessageType(Optional.of(ModeMessageType.OMNI_ON_OFF))
                        .build();
            }
            public ModeAssign monoPoly() {
                return this.assignType(ModeAssignType.MODE_MESSAGE)
                        .modeMessageType(Optional.of(ModeMessageType.MONO_POLY))
                        .build();
            }
            public ModeAssign tempo() {
                return this.assignType(ModeAssignType.OTHERS)
                        .otherMessageType(Optional.of(ModeOtherMessageType.TEMPO))
                        .build();
            }
            public ModeAssign programUp() {
                return this.assignType(ModeAssignType.OTHERS)
                        .otherMessageType(Optional.of(ModeOtherMessageType.PROGRAM_UP))
                        .build();
            }
            public ModeAssign programDown() {
                return this.assignType(ModeAssignType.OTHERS)
                        .otherMessageType(Optional.of(ModeOtherMessageType.PROGRAM_DOWN))
                        .build();
            }
            public ModeAssign autoFadeout(int seconds) {
                return this.assignType(ModeAssignType.OTHERS)
                        .otherMessageType(Optional.of(ModeOtherMessageType.AUTO_FADE_OUT))
                        .autoFadeOutTime(Optional.of(seconds))
                        .build();
            }
        }

        public byte[] build() {
            return new byte[] {
                    (byte) assignType.ordinal(),
                    (byte) controllerMessageType.map(ContinuousControllerId::getData).orElse((byte) 0),
                    (byte) channelMessageType.map(Enum::ordinal).orElse(0).byteValue(),
                    (byte) modeMessageType.map(Enum::ordinal).orElse(0).byteValue(),
                    (byte) polyphonicAftertouchTrigger.map(Enum::ordinal).orElse(0).byteValue(),
                    (byte) otherMessageType.map(Enum::ordinal).orElse(0).byteValue(),
                    (byte) autoFadeOutTime.orElse(0).byteValue()
            };
        }
    }
    public enum ModeAssignType {
        OFF,
        CC,
        CHANNEL_MESSAGE,
        MODE_MESSAGE,
        OTHERS
    }
    public enum ModeMessageType {
        ALL_SOUND_OFF,
        RESET_ALL_CONTROLLERS,
        LOCAL_CONTROL,
        ALL_NOTE_OFF,
        OMNI_ON_OFF,
        MONO_POLY
    }
    public enum ModeOtherMessageType {
        TEMPO,
        PROGRAM_UP,
        PROGRAM_DOWN,
        AUTO_FADE_OUT
    }

    @Builder
    @EqualsAndHashCode
    @Getter
    @RequiredArgsConstructor
    @ToString
    public static class AuxAssign {
        @Nonnull private final AuxAssignType assignType;
        @Builder.Default @Nonnull private final Optional<ContinuousControllerId> controllerMessageType = Optional.empty();
        @Builder.Default @Nonnull private final Optional<ChannelMessageType> channelMessageType = Optional.empty();
        @Builder.Default @Nonnull private final Optional<PolyphonicAftertouchTrigger> polyphonicAftertouchTrigger = Optional.empty();
        @Builder.Default @Nonnull private final Optional<RPNType> rpnType = Optional.empty();
        @Builder.Default @Nonnull private final Optional<ContinuousControlValue> rpnMsb = Optional.empty();
        @Builder.Default @Nonnull private final Optional<ContinuousControlValue> rpnLsb = Optional.empty();
        @Builder.Default @Nonnull private final Optional<NRPNType> nrpnType = Optional.empty();
        @Builder.Default @Nonnull private final Optional<ContinuousControlValue> nrpnMsb = Optional.empty();
        @Builder.Default @Nonnull private final Optional<ContinuousControlValue> nrpnLsb = Optional.empty();
        @Builder.Default @Nonnull private final Optional<byte[]> sysexHeader = Optional.empty();

        public static class AuxAssignBuilder {
            public AuxAssign off() {
                return this.assignType(AuxAssignType.OFF)
                        .build();
            }
            public AuxAssign cc(final ContinuousControllerId id) {
                return this.assignType(AuxAssignType.CC)
                        .controllerMessageType(Optional.of(id))
                        .build();
            }
            public AuxAssign channelAftertouch() {
                return this.assignType(AuxAssignType.CHANNEL_MESSAGE)
                        .channelMessageType(Optional.of(ChannelMessageType.CHANNEL_AFTERTOUCH))
                        .build();
            }
            public AuxAssign polyAftertouch(final PolyphonicAftertouchTrigger polyphonicAftertouchTrigger) {
                return this.assignType(AuxAssignType.CHANNEL_MESSAGE)
                        .channelMessageType(Optional.of(ChannelMessageType.POLYPHONIC_AFTERTOUCH))
                        .polyphonicAftertouchTrigger(Optional.of(polyphonicAftertouchTrigger))
                        .build();
            }
            public AuxAssign pitchBend() {
                return this.assignType(AuxAssignType.CHANNEL_MESSAGE)
                        .channelMessageType(Optional.of(ChannelMessageType.PITCH_BEND))
                        .build();
            }
            public AuxAssign pitchBendSense(byte msb, byte lsb) {
                return this.assignType(AuxAssignType.RPN)
                        .rpnType(Optional.of(RPNType.PITCH_BEND_SENSE))
                        .rpnMsb(Optional.of(ContinuousControlValue.of(msb)))
                        .rpnLsb(Optional.of(ContinuousControlValue.of(lsb)))
                        .build();
            }
            public AuxAssign fineTune(byte msb, byte lsb) {
                return this.assignType(AuxAssignType.RPN)
                        .rpnType(Optional.of(RPNType.FINE_TUNE))
                        .rpnMsb(Optional.of(ContinuousControlValue.of(msb)))
                        .rpnLsb(Optional.of(ContinuousControlValue.of(lsb)))
                        .build();
            }
            public AuxAssign coarseTune(byte msb, byte lsb) {
                return this.assignType(AuxAssignType.RPN)
                        .rpnType(Optional.of(RPNType.COARSE_TUNE))
                        .rpnMsb(Optional.of(ContinuousControlValue.of(msb)))
                        .rpnLsb(Optional.of(ContinuousControlValue.of(lsb)))
                        .build();
            }
            public AuxAssign rpnFree(byte msb, byte lsb) {
                return this.assignType(AuxAssignType.RPN)
                        .rpnType(Optional.of(RPNType.FREE))
                        .rpnMsb(Optional.of(ContinuousControlValue.of(msb)))
                        .rpnLsb(Optional.of(ContinuousControlValue.of(lsb)))
                        .build();
            }
            public AuxAssign gsVibrateRate(byte msb, byte lsb) {
                return this.assignType(AuxAssignType.NRPN)
                        .nrpnType(Optional.of(NRPNType.GS_VIBRATE_RATE))
                        .nrpnMsb(Optional.of(ContinuousControlValue.of(msb)))
                        .nrpnLsb(Optional.of(ContinuousControlValue.of(lsb)))
                        .build();
            }
            public AuxAssign gsVibrateDepth(byte msb, byte lsb) {
                return this.assignType(AuxAssignType.NRPN)
                        .nrpnType(Optional.of(NRPNType.GS_VIBRATE_DEPTH))
                        .nrpnMsb(Optional.of(ContinuousControlValue.of(msb)))
                        .nrpnLsb(Optional.of(ContinuousControlValue.of(lsb)))
                        .build();
            }
            public AuxAssign gsVibrateDelay(byte msb, byte lsb) {
                return this.assignType(AuxAssignType.NRPN)
                        .nrpnType(Optional.of(NRPNType.GS_VIBRATE_DELAY))
                        .nrpnMsb(Optional.of(ContinuousControlValue.of(msb)))
                        .nrpnLsb(Optional.of(ContinuousControlValue.of(lsb)))
                        .build();
            }
            public AuxAssign gsTVFCutoffFreq(byte msb, byte lsb) {
                return this.assignType(AuxAssignType.NRPN)
                        .nrpnType(Optional.of(NRPNType.GS_TVF_CUTOFF_FREQ))
                        .nrpnMsb(Optional.of(ContinuousControlValue.of(msb)))
                        .nrpnLsb(Optional.of(ContinuousControlValue.of(lsb)))
                        .build();
            }
            public AuxAssign gsTVFResonance(byte msb, byte lsb) {
                return this.assignType(AuxAssignType.NRPN)
                        .nrpnType(Optional.of(NRPNType.GS_TVF_RESONANCE))
                        .nrpnMsb(Optional.of(ContinuousControlValue.of(msb)))
                        .nrpnLsb(Optional.of(ContinuousControlValue.of(lsb)))
                        .build();
            }
            public AuxAssign gsEnvAttackTime(byte msb, byte lsb) {
                return this.assignType(AuxAssignType.NRPN)
                        .nrpnType(Optional.of(NRPNType.GS_TVF_AND_TVA_ENVELOPE_ATTACK_TIME))
                        .nrpnMsb(Optional.of(ContinuousControlValue.of(msb)))
                        .nrpnLsb(Optional.of(ContinuousControlValue.of(lsb)))
                        .build();
            }
            public AuxAssign gsEnvDecayTime(byte msb, byte lsb) {
                return this.assignType(AuxAssignType.NRPN)
                        .nrpnType(Optional.of(NRPNType.GS_TVF_AND_TVA_ENVELOPE_DECAY_TIME))
                        .nrpnMsb(Optional.of(ContinuousControlValue.of(msb)))
                        .nrpnLsb(Optional.of(ContinuousControlValue.of(lsb)))
                        .build();
            }
            public AuxAssign gsEnvReleaseTime(byte msb, byte lsb) {
                return this.assignType(AuxAssignType.NRPN)
                        .nrpnType(Optional.of(NRPNType.GS_TVF_AND_TVA_ENVELOPE_RELEASE_TIME))
                        .nrpnMsb(Optional.of(ContinuousControlValue.of(msb)))
                        .nrpnLsb(Optional.of(ContinuousControlValue.of(lsb)))
                        .build();
            }
            public AuxAssign nrpnFree(byte msb, byte lsb) {
                return this.assignType(AuxAssignType.NRPN)
                        .nrpnType(Optional.of(NRPNType.FREE))
                        .nrpnMsb(Optional.of(ContinuousControlValue.of(msb)))
                        .nrpnLsb(Optional.of(ContinuousControlValue.of(lsb)))
                        .build();
            }
            public AuxAssign sysex(byte[] buffer) {
                if (buffer.length != 15) {
                    throw new IllegalArgumentException("Sysex must be precisely 15 bytes");
                }
                return this.assignType(AuxAssignType.SYSEX)
                        .sysexHeader(Optional.of(buffer))
                        .build();
            }
        }

        public byte[] build() {
            final ByteBuffer bb = ByteBuffer.allocate(26);
            bb.put(new byte[] {
                    (byte) assignType.ordinal(),
                    (byte) controllerMessageType.map(ContinuousControllerId::getData).orElse((byte) 0),
                    (byte) channelMessageType.map(Enum::ordinal).orElse(0).byteValue(),
                    (byte) polyphonicAftertouchTrigger.map(Enum::ordinal).orElse(0).byteValue(),
                    (byte) rpnType.map(Enum::ordinal).orElse(0).byteValue(),
                    (byte) rpnMsb.map(ContinuousControlValue::getData).orElse((byte) 0),
                    (byte) rpnLsb.map(ContinuousControlValue::getData).orElse((byte) 0),
                    (byte) nrpnType.map(Enum::ordinal).orElse(0).byteValue(),
                    (byte) nrpnMsb.map(ContinuousControlValue::getData).orElse((byte) 0),
                    (byte) nrpnLsb.map(ContinuousControlValue::getData).orElse((byte) 0)
            });
            bb.put(sysexHeader
                    .map(bytes -> ByteBuffer.allocate(16)
                            .put((byte) bytes.length)
                            .put(bytes)
                            .array())
                    .orElse(new byte[16]));
            return bb.array();
        }
    }
    public enum AuxAssignType {
        OFF,
        CC,
        CHANNEL_MESSAGE,
        RPN,
        NRPN,
        SYSEX
    }
    public enum PolyphonicAftertouchTrigger {
        HIGH,
        LOW,
        FIRST,
        LAST
    }
    public enum RPNType {
        PITCH_BEND_SENSE,
        FINE_TUNE,
        COARSE_TUNE,
        FREE
    }
    public enum NRPNType {
        GS_VIBRATE_RATE,
        GS_VIBRATE_DEPTH,
        GS_VIBRATE_DELAY,
        GS_TVF_CUTOFF_FREQ,
        GS_TVF_RESONANCE,
        GS_TVF_AND_TVA_ENVELOPE_ATTACK_TIME,
        GS_TVF_AND_TVA_ENVELOPE_DECAY_TIME,
        GS_TVF_AND_TVA_ENVELOPE_RELEASE_TIME,
        FREE
    }

    @Override
    public List<Message<?>> getMessages() {
        return buildCommands()
                .stream()
                .map(cmd -> new RolandSysexMessage((byte) 0,
                        InstrumentModel.A_90,
                        cmd))
                .collect(Collectors.toList());
    }

    private List<RolandSysexCommand> buildCommands() {
        final ImmutableList.Builder<RolandSysexCommand> builder = ImmutableList.builder();

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(56)) {
            baos.write(breathSlider.build());
            baos.write(aftertouchSlider.build());
            baos.write(expressionSlider.build());
            baos.write(portamentoTimeSlider.build());
            baos.write(footController1.build());
            baos.write(footController2.build());
            baos.write(footSwitch1.build());
            baos.write(footSwitch2.build());
            baos.write(aftertouch.build());
            baos.write(wheel1.build());
            baos.write(wheel2.build());
            baos.write(bendLever.build());
            baos.write(modulationLever.build());
            baos.write(breath.build());
            baos.write(monoSwitch.build());
            baos.write(portamentoSwitch.build());
            baos.write(aux1extA.build());
            baos.write(aux1extB.build());
            baos.write(aux1extC.build());
            baos.write(aux1extD.build());
            baos.write(aux1intA.build());
            baos.write(aux1intB.build());
            baos.write(aux1intC.build());
            baos.write(aux1intD.build());
            baos.write(aux2extA.build());
            baos.write(aux2extB.build());
            baos.write(aux2extC.build());
            baos.write(aux2extD.build());
            baos.write(aux2intA.build());
            baos.write(aux2intB.build());
            baos.write(aux2intC.build());
            baos.write(aux2intD.build());
            extZonesCCResetWithPerfChange.streamAsBooleanBytes(4).forEachOrdered(baos::write);
            intZonesCCResetWithPerfChange.streamAsBooleanBytes(4).forEachOrdered(baos::write);

            builder.add(new ControllersDataSetCommand(baos.toByteArray()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return builder.build();
    }

    private class ControllersDataSetCommand extends RolandDataSetCommand {
        private ControllersDataSetCommand(final byte[] bytes) {
            super(InstrumentModel.A_90,
                    0x00001000,
                    bytes);
            if (bytes.length != 0x037C) {
                throw new IllegalArgumentException("Wrong length, expected " + 0x037C + " but saw " + bytes.length);
            }
        }
    }

}
