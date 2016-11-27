package com.adamdbradley.midi.domain.controller;

import java.util.BitSet;
import java.util.List;

import com.adamdbradley.midi.domain.ProgramChange;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
@EqualsAndHashCode
public class Patch {

    private final ProgramChange number;
    private final String name;
    private final BitSet outputMask;
    private final BitSet zoneMask;
    private final List<Zone> zones;
    private final List<AdditionalTransmission> additionals;

}
