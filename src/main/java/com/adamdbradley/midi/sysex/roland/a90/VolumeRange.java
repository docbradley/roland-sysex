package com.adamdbradley.midi.sysex.roland.a90;

import com.adamdbradley.midi.domain.Volume;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class VolumeRange {

    final Volume lowValue;
    final Volume highValue;

}
