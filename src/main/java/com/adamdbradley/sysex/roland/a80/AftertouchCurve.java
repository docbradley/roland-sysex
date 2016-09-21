package com.adamdbradley.sysex.roland.a80;

import com.adamdbradley.sysex.CurveParameterValue;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
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
        Key((byte) 1),
        Off((byte) 3);
        private final byte value;
    }

    private final CurveType curveType;
    private final CurveParameterValue scaling;
    private final CurveParameterValue holdoff;
    private final AftertouchType type;

    byte[] build() {
        return new byte[] {
                curveType.value,
                scaling.getData(),
                holdoff.getData(),
                type.value
        };
    }

}
