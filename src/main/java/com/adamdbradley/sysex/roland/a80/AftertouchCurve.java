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
public class AftertouchCurve {

    @Getter
    @RequiredArgsConstructor
    public enum CurveType {
        Flat((byte) 0),
        ShallowSoft((byte) 1),
        DeepSoft((byte) 2),
        ShallowHard((byte) 3),
        DeepHard((byte) 4),
        Sinusoid((byte) 5);
        private final byte value;
    }

    @Getter
    @RequiredArgsConstructor
    public enum AftertouchType {
        Poly((byte) 0),
        Channel((byte) 1),
        Off((byte) 2);
        private final byte value;
    }

    @NonNull @Nonnull private final CurveType curveType;
    @NonNull @Nonnull private final CurveParameterValue scaling;
    @NonNull @Nonnull private final CurveParameterValue holdoff;
    @NonNull @Nonnull private final AftertouchType type;

    // Provides defaults for Builder
    public static class AftertouchCurveBuilder {
        private CurveType curveType = CurveType.Flat;
        private CurveParameterValue scaling = CurveParameterValue.of(64);
        private CurveParameterValue holdoff = CurveParameterValue.of(0);
        private AftertouchType type = AftertouchType.Poly;
    }

    byte[] build() {
        return new byte[] {
                curveType.value,
                scaling.getData(),
                holdoff.getData(),
                type.value
        };
    }

}
