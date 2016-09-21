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

    private final CurveType curve;
    private final CurveParameterValue scaling;
    private final CurveParameterValue offset;
    private final CurveParameterValue holdoff;

    byte[] build() {
        return new byte[] {
                curve.value,
                scaling.getData(),
                offset.getData(),
                holdoff.getData()
        };
    }

}
