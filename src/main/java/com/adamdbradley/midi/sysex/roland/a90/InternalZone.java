package com.adamdbradley.midi.sysex.roland.a90;

import java.util.Optional;

import javax.annotation.Nonnull;

import com.adamdbradley.midi.domain.Channel;
import com.adamdbradley.midi.domain.ContinuousControlValue;
import com.adamdbradley.midi.domain.Modulation;
import com.adamdbradley.midi.domain.Note;
import com.adamdbradley.midi.domain.Pan;
import com.adamdbradley.midi.domain.ProgramChange;
import com.adamdbradley.midi.domain.Volume;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Builder
@EqualsAndHashCode(callSuper=true)
@Getter
@ToString
public class InternalZone extends Zone {

    @Builder
    private InternalZone(final String comment,
            boolean zoneSwitch, boolean localKeyboardSwitch,
            boolean midiOut1, boolean midiOut2, boolean midiOut3, boolean midiOut4,
            Channel midiChannel, Note keyLower, Note keyUpper, Transposition transpose,
            VelocityCurve velocityCurve,
            Optional<Volume> volume, Optional<Pan> pan, 
            Optional<Volume> reverbSendLevel, Optional<Volume> chorusSendLevel,
            Optional<ProgramChange> programChange, Optional<BankSelect> bankSelect,
            Optional<Volume> aux1, Optional<Volume> aux2,
            Optional<VolumeRange> breathSliderRange, Optional<VolumeRange> ATSliderRange,
            Optional<VolumeRange> exprSliderRange, Optional<VolumeRange> PTSliderRange,
            Optional<VolumeRange> FC1Range, Optional<VolumeRange> FC2Range,
            Optional<VolumeRange> FS1Values, Optional<VolumeRange> FS2Values,
            Optional<VolumeRange> monoSwitchValues, Optional<VolumeRange> PTSwitchValues,
            Optional<VolumeRange> aftertouchRange,
            Optional<VolumeRange> wheel1Range, Optional<VolumeRange> whell2Range,
            Optional<VolumeRange> bendLeverRange, Optional<VolumeRange> modLeverRange,
            Optional<VolumeRange> breathControllerRange,
            boolean globalTranspose, boolean totalVolumeSlider,
            boolean totalVolumePedal, boolean holdPedal,
            Optional<Modulation> modulation, Optional<ContinuousControlValue> aftertouch,
            Optional<Volume> expression, Optional<ContinuousControlValue> portamentoTime,
            CentControlValue attackTime, CentControlValue decayTime,
            CentControlValue releaseTime, CentControlValue brightValue,
            CentBiasControlValue fineTune) {
        super(comment, 
                zoneSwitch, localKeyboardSwitch, midiOut1, midiOut2, midiOut3, midiOut4,
                midiChannel, keyLower, keyUpper, transpose, velocityCurve, volume, pan,
                reverbSendLevel, chorusSendLevel, programChange, bankSelect, aux1, aux2,
                breathSliderRange, ATSliderRange, exprSliderRange, PTSliderRange,
                FC1Range, FC2Range, FS1Values, FS2Values, monoSwitchValues, PTSwitchValues,
                aftertouchRange, wheel1Range, whell2Range, bendLeverRange, modLeverRange,
                breathControllerRange, globalTranspose, totalVolumeSlider, totalVolumePedal,
                holdPedal, modulation, aftertouch, expression, portamentoTime);
        this.attackTime = attackTime;
        this.decayTime = decayTime;
        this.releaseTime = releaseTime;
        this.brightValue = brightValue;
        this.fineTune = fineTune;
    }

    @NonNull @Nonnull private final CentControlValue attackTime;
    @NonNull @Nonnull private final CentControlValue decayTime;
    @NonNull @Nonnull private final CentControlValue releaseTime;
    @NonNull @Nonnull private final CentControlValue brightValue;
    @NonNull @Nonnull private final CentBiasControlValue fineTune;

    private static class InternalZoneBuilder extends Zone.ZoneBuilder {
    }

    public byte[] build() {
        byte[] buffer = new byte[95];
        super.build(buffer, 5);
        buffer[0x52] = attackTime.getData();
        buffer[0x53] = decayTime.getData();
        buffer[0x54] = releaseTime.getData();
        buffer[0x55] = brightValue.getData();
        buffer[0x56] = fineTune.getData();
        return buffer;
    }

}
