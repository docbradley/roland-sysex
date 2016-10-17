package com.adamdbradley.midi.sysex.roland.a90;

import javax.annotation.Nonnull;

import com.adamdbradley.midi.domain.ContinuousControlValue;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
@ToString
public class VelocityCurve {

    @RequiredArgsConstructor
    public enum CurveType {
        Unknown0(0),
        Unknown1(1),
        Unknown2(2),
        Unknown3(3),
        Unknown4(4),
        Unknown5(5),
        Unknown6(6);
        final int data;
        private byte getData() {
            return (byte) data;
        }
    }

    @NonNull @Nonnull private final CurveType curve;
    @NonNull @Nonnull private final ContinuousControlValue sense; // 1-127
    @NonNull @Nonnull private final ContinuousControlValue max; // 1-127

    public byte[] build() {
        return new byte[] {
                curve.getData(),
                sense.getData(),
                max.getData()
        };
    }

}
