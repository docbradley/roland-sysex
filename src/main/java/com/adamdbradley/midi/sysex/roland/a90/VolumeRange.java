package com.adamdbradley.midi.sysex.roland.a90;

import com.adamdbradley.midi.domain.Volume;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
public class VolumeRange {

    public static final VolumeRange ZERO = of(Volume.ZERO, Volume.ZERO);
    public static final VolumeRange FULL = of(Volume.ZERO, Volume.MAX);

    public static VolumeRange of(final Volume min, final Volume max) {
        return builder()
                .lowValue(min)
                .highValue(max)
                .build();
    }

    final Volume lowValue;
    final Volume highValue;

}
