package com.adamdbradley.midi.sysex.roland.a90;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;

import javax.annotation.Nonnull;

import com.adamdbradley.midi.domain.Channel;
import com.adamdbradley.midi.domain.ContinuousControlValue;
import com.adamdbradley.midi.domain.Modulation;
import com.adamdbradley.midi.domain.Note;
import com.adamdbradley.midi.domain.Pan;
import com.adamdbradley.midi.domain.ProgramChange;
import com.adamdbradley.midi.domain.SingleSevenBitData;
import com.adamdbradley.midi.domain.Volume;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
@ToString
public abstract class Zone {

    private final String comment;

    private final boolean zoneSwitch;
    private final boolean localKeyboardSwitch;
    private final boolean midiOut1;
    private final boolean midiOut2;
    private final boolean midiOut3;
    private final boolean midiOut4;
    @NonNull @Nonnull private final Channel midiChannel;
    @NonNull @Nonnull private final Note keyLower;
    @NonNull @Nonnull private final Note keyUpper;
    @NonNull @Nonnull private final Transposition transpose;
    @NonNull @Nonnull private final VelocityCurve velocityCurve; // curve, sense, max
    @NonNull @Nonnull private final Optional<Volume> volume;
    @NonNull @Nonnull private final Optional<Pan> pan;
    @NonNull @Nonnull private final Optional<Volume> reverbSendLevel;
    @NonNull @Nonnull private final Optional<Volume> chorusSendLevel;
    @NonNull @Nonnull private final Optional<ProgramChange> programChange;
    @NonNull @Nonnull private final Optional<BankSelect> bankSelect; // 14 bits
    @NonNull @Nonnull private final Optional<Volume> aux1;
    @NonNull @Nonnull private final Optional<Volume> aux2;
    @NonNull @Nonnull private final Optional<VolumeRange> breathSliderRange;
    @NonNull @Nonnull private final Optional<VolumeRange> ATSliderRange;
    @NonNull @Nonnull private final Optional<VolumeRange> exprSliderRange;
    @NonNull @Nonnull private final Optional<VolumeRange> PTSliderRange;
    @NonNull @Nonnull private final Optional<VolumeRange> FC1Range;
    @NonNull @Nonnull private final Optional<VolumeRange> FC2Range;
    @NonNull @Nonnull private final Optional<VolumeRange> FS1Values;
    @NonNull @Nonnull private final Optional<VolumeRange> FS2Values;
    @NonNull @Nonnull private final Optional<VolumeRange> monoSwitchValues;
    @NonNull @Nonnull private final Optional<VolumeRange> PTSwitchValues;
    @NonNull @Nonnull private final Optional<VolumeRange> aftertouchRange;
    @NonNull @Nonnull private final Optional<VolumeRange> wheel1Range;
    @NonNull @Nonnull private final Optional<VolumeRange> wheel2Range;
    @NonNull @Nonnull private final Optional<VolumeRange> bendLeverRange;
    @NonNull @Nonnull private final Optional<VolumeRange> modLeverRange;
    @NonNull @Nonnull private final Optional<VolumeRange> breathControllerRange;
    private final boolean globalTranspose;
    private final boolean totalVolumeSlider;
    private final boolean totalVolumePedal;
    private final boolean holdPedal;
    @NonNull @Nonnull private final Optional<Modulation> modulation;
    @NonNull @Nonnull private final Optional<ContinuousControlValue> aftertouch;
    @NonNull @Nonnull private final Optional<Volume> expression;
    @NonNull @Nonnull private final Optional<ContinuousControlValue> portamentoTime;

    static class ZoneBuilder {
        
    }

    /**
     * @param buffer
     * @param gap to leave between byte offset 0x51 and the final eight bytes
     */
    protected void build(byte[] buffer, int gap) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(buffer.length)) {
            baos.write((byte) (zoneSwitch ? 1 : 0));
            baos.write((byte) (localKeyboardSwitch ? 1 : 0));
            baos.write((byte) (midiOut1 ? 1 : 0));
            baos.write((byte) (midiOut2 ? 1 : 0));
            baos.write((byte) (midiOut3 ? 1 : 0));
            baos.write((byte) (midiOut4 ? 1 : 0));
            baos.write(midiChannel.getData());
            baos.write(keyLower.getData());
            baos.write(keyUpper.getData());
            baos.write(transpose.getData());
            baos.write(velocityCurve.build());
            emitOptional(baos, volume);
            emitOptional(baos, pan);
            emitOptional(baos, reverbSendLevel);
            emitOptional(baos, chorusSendLevel);
            emitOptional(baos, programChange);
            if (bankSelect.isPresent()) {
                
            } else {
                
            }
            emitOptional(baos, aux1);
            emitOptional(baos, aux2);
            emitOptionalRange(baos, breathSliderRange);
            emitOptionalRange(baos, ATSliderRange);
            emitOptionalRange(baos, exprSliderRange);
            emitOptionalRange(baos, PTSliderRange);
            emitOptionalRange(baos, FC1Range);
            emitOptionalRange(baos, FC2Range);
            emitOptionalRange(baos, FS1Values);
            emitOptionalRange(baos, FS2Values);
            emitOptionalRange(baos, monoSwitchValues);
            emitOptionalRange(baos, PTSwitchValues);
            emitOptionalRange(baos, aftertouchRange);
            emitOptionalRange(baos, wheel1Range);
            emitOptionalRange(baos, wheel2Range);
            emitOptionalRange(baos, bendLeverRange);
            emitOptionalRange(baos, modLeverRange);
            emitOptionalRange(baos, breathControllerRange);
            baos.write((byte) (globalTranspose ? 1 : 0));
            baos.write((byte) (totalVolumeSlider ? 1 : 0));
            baos.write((byte) (totalVolumePedal ? 1 : 0));
            baos.write((byte) (holdPedal ? 1 : 0));

            for (int i=0; i<gap; i++) {
                baos.write((byte) 0);
            }

            emitOptional(baos, modulation);
            emitOptional(baos, aftertouch);
            emitOptional(baos, expression);
            emitOptional(baos, portamentoTime);

            final byte[] written = baos.toByteArray();
            System.arraycopy(written, 0, buffer, 0, written.length);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void emitOptional(final ByteArrayOutputStream baos,
            final Optional<? extends SingleSevenBitData> data) {
        if (data.isPresent()) {
            baos.write(data.get().getData());
            baos.write((byte) 1);
        } else {
            baos.write((byte) 0);
            baos.write((byte) 0);
        }
    }

    private void emitOptionalRange(final ByteArrayOutputStream baos,
            final Optional<VolumeRange> range) {
        if (range.isPresent()) {
            baos.write((byte) 1);
            baos.write(range.get().lowValue.getData());
            baos.write(range.get().highValue.getData());
        } else {
            baos.write((byte) 0);
            baos.write((byte) 0x0);
            baos.write((byte) 0x7F);
        }
    }

}
