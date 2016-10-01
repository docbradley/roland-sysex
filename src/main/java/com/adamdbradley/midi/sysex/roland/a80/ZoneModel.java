package com.adamdbradley.midi.sysex.roland.a80;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import com.adamdbradley.midi.domain.Channel;
import com.adamdbradley.midi.domain.ContinuousControllerId;
import com.adamdbradley.midi.domain.Modulation;
import com.adamdbradley.midi.domain.Note;
import com.adamdbradley.midi.domain.ProgramChange;
import com.adamdbradley.midi.domain.Volume;
import com.google.common.collect.ImmutableList;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Represents a single "zone" within a "patch" for the A-80 controller.
 */
@Getter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class ZoneModel {

    @Getter
    @RequiredArgsConstructor
    public enum ProgramChangePresentation {
        AB_Bank8_Patch8((byte) 0),
        IntCar_Bank8_Patch8((byte) 1),
        IntCar_BankH_Patch8((byte) 2),
        AB_Decimal16((byte) 3),
        AB_Decimal32((byte) 4),
        IntCar_Decimal64((byte) 5),
        Decimal128((byte) 6),
        DecimalZero99((byte) 7),
        Hex((byte) 8);
        private final byte value;
    }

    @Getter private final boolean unmuted;
    @NonNull @Nonnull private final Note startKey; // inclusive?
    @NonNull @Nonnull private final Note endKey; // inclusive?
    @NonNull @Nonnull private final Channel channel;
    @NonNull @Nonnull private final Transposition transpose; // Zero = dec 36
    @NonNull @Nonnull private final VelocityCurve velocityCurve;
    @NonNull @Nonnull private final AftertouchCurve aftertouchCurve;
    @NonNull @Nonnull private final ProgramChangePresentation programChangePresentation;
    @NonNull @Nonnull private final Volume volume;
    @NonNull @Nonnull private final Modulation modulation;
    private final boolean enablePitchBend;
    @NonNull @Nonnull private final ProgramChange programChange;
    @NonNull @Nonnull private final List<Optional<ContinuousControllerId>> sliderControls;
    @NonNull @Nonnull private final List<Optional<ContinuousControllerId>> switchControls;
    @NonNull @Nonnull private final List<Optional<ContinuousControllerId>> pedalControls;

    /**
     * Default {@link ZoneModel} is based on mostly sensible values, but
     * key range is 0-0 and all controllers are disabled.
     */
    public static class ZoneModelBuilder {
        private boolean unmuted = true;
        private Note startKey = Note.of(0);
        private Note endKey = Note.of(0);
        private Transposition transpose = Transposition.of(0);
        private VelocityCurve velocityCurve = VelocityCurve.builder().build();
        private AftertouchCurve aftertouchCurve = AftertouchCurve.builder().build();
        private ProgramChangePresentation programChangePresentation = ProgramChangePresentation.Decimal128;
        private Volume volume = Volume.of(100);
        private Modulation modulation = Modulation.of(0);
        private boolean enablePitchBend = true;
        private ProgramChange programChange = ProgramChange.of(0);
        private List<Optional<ContinuousControllerId>> sliderControls = ImmutableList.of(
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
        private List<Optional<ContinuousControllerId>> switchControls = ImmutableList.of(
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
        private List<Optional<ContinuousControllerId>> pedalControls = ImmutableList.of(
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
    }

    public byte[] build() {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(29)) {
            baos.write(this.startKey.getData());
            baos.write(this.endKey.getData());
            baos.write(this.channel.getData());
            baos.write(this.transpose.getData());
            baos.write(this.velocityCurve.build());   // 4B
            baos.write(this.aftertouchCurve.build()); // 4B
            baos.write(this.programChangePresentation.getValue());
            baos.write(this.volume.getData());
            baos.write(this.modulation.getData());
            baos.write(this.enablePitchBend ? (byte) 0x1 : (byte) 0x0);
            baos.write(this.programChange.getData());
            baos.write(mapOptionalCCToSysexByte(this.sliderControls.get(0)));
            baos.write(mapOptionalCCToSysexByte(this.sliderControls.get(1)));
            baos.write(mapOptionalCCToSysexByte(this.sliderControls.get(2)));
            baos.write(mapOptionalCCToSysexByte(this.sliderControls.get(3)));
            baos.write(mapOptionalCCToSysexByte(this.switchControls.get(0)));
            baos.write(mapOptionalCCToSysexByte(this.switchControls.get(1)));
            baos.write(mapOptionalCCToSysexByte(this.switchControls.get(2)));
            baos.write(mapOptionalCCToSysexByte(this.switchControls.get(3)));
            baos.write(mapOptionalCCToSysexByte(this.pedalControls.get(0)));
            baos.write(mapOptionalCCToSysexByte(this.pedalControls.get(1)));
            baos.write(mapOptionalCCToSysexByte(this.pedalControls.get(2)));
            baos.write(mapOptionalCCToSysexByte(this.pedalControls.get(3)));

            if (baos.size() != 29) {
                throw new IllegalStateException("Built to wrong size");
            }
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private byte mapOptionalCCToSysexByte(Optional<ContinuousControllerId> id) {
        if (id.isPresent()) {
            if (id.get().getData() < 120) {
                return (byte) (id.get().getData() + 1);
            } else if (id.get().getData() >= 124) {
                return (byte) (id.get().getData());
            } else {
                throw new RuntimeException("Can't map CC 120-123");
            }
        } else {
            return (byte) 0x0;
        }
    }

}
