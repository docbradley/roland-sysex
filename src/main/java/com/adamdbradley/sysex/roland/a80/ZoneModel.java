package com.adamdbradley.sysex.roland.a80;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import com.adamdbradley.sysex.Channel;
import com.adamdbradley.sysex.ContinuousControllerId;
import com.adamdbradley.sysex.Modulation;
import com.adamdbradley.sysex.Note;
import com.adamdbradley.sysex.ProgramChange;
import com.adamdbradley.sysex.Volume;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * 29 bytes (0x1D)
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
        AB_Bank8_Number8((byte) 0),
        AB_Number32((byte) 1),
        InternalOrCard((byte) 2),
        Bank8_Number8((byte) 3),
        Number64((byte) 4),
        Number128((byte) 5),
        BankAH_Number8((byte) 6),
        Number99((byte) 7),
        AB_Number16((byte) 8),
        Hex((byte) 9);
        private final byte value;
    }

    final boolean unmuted;

    private final Note startKey; // inclusive?

    private final Note endKey; // inclusive?

    private final Channel channel;

    private final Transposition transpose; // Zero = dec 36

    private final VelocityCurve velocityCurve;

    private final AftertouchCurve aftertouchCurve;

    private final ProgramChangePresentation patchType;

    private final Volume volume;

    private final Modulation modulation;

    private final boolean enablePitchBend;

    private final ProgramChange patchChange;

    private final List<ContinuousControllerId> sliderControlNumbers;

    private final List<ContinuousControllerId> switchControlNumbers;

    private final List<ContinuousControllerId> pedalControlNumbers;

    public byte[] build() {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(29)) {
            baos.write(this.startKey.getData());
            baos.write(this.endKey.getData());
            baos.write(this.channel.getData());
            baos.write(this.transpose.getData());
            baos.write(this.velocityCurve.build());   // 4B
            baos.write(this.aftertouchCurve.build()); // 4B
            baos.write(this.patchType.getValue());
            baos.write(this.volume.getData());
            baos.write(this.modulation.getData());
            baos.write(this.enablePitchBend ? (byte) 0x0 : (byte) 0x1);
            baos.write(this.patchChange.getData());
            baos.write(this.sliderControlNumbers.get(0).getData());
            baos.write(this.sliderControlNumbers.get(1).getData());
            baos.write(this.sliderControlNumbers.get(2).getData());
            baos.write(this.sliderControlNumbers.get(3).getData());
            baos.write(this.switchControlNumbers.get(0).getData());
            baos.write(this.switchControlNumbers.get(1).getData());
            baos.write(this.switchControlNumbers.get(2).getData());
            baos.write(this.switchControlNumbers.get(3).getData());
            baos.write(this.pedalControlNumbers.get(0).getData());
            baos.write(this.pedalControlNumbers.get(1).getData());
            baos.write(this.pedalControlNumbers.get(2).getData());
            baos.write(this.pedalControlNumbers.get(3).getData());

            if (baos.size() != 29) {
                throw new IllegalStateException("Built to wrong size");
            }
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
