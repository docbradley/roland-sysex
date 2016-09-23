package com.adamdbradley.sysex.roland.a80;

import javax.annotation.Nonnull;

import com.adamdbradley.sysex.CurveParameterValue;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Builder
@EqualsAndHashCode
@Getter
@RequiredArgsConstructor
@ToString
public class VelocityCurve {

    @Getter
    @RequiredArgsConstructor
    public enum CurveType {
        Flat((byte) 0),
        ShallowSoft((byte) 1),
        DeepSoft((byte) 2),
        ShallowHard((byte) 3),
        DeepHard((byte) 4),
        Sinusoid((byte) 5),
        Reverse((byte) 6);
        private final byte value;
    }

    @NonNull @Nonnull private final CurveType curve;
    @NonNull @Nonnull private final CurveParameterValue scaling;
    @NonNull @Nonnull private final CurveParameterValue offset;
    @NonNull @Nonnull private final CurveParameterValue holdoff;

    // Provides defaults for Builder
    public static class VelocityCurveBuilder {
        private CurveType curve = CurveType.Flat;
        private CurveParameterValue scaling = CurveParameterValue.of(64);
        private CurveParameterValue offset = CurveParameterValue.of(0);
        private CurveParameterValue holdoff = CurveParameterValue.of(0);
    }

    byte[] build() {
        return new byte[] {
                curve.value,
                scaling.getData(),
                offset.getData(),
                holdoff.getData()
        };
    }

}
