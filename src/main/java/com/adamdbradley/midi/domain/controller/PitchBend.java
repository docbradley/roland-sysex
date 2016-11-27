package com.adamdbradley.midi.domain.controller;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
@EqualsAndHashCode
public class PitchBend {

    private final int pitchBend;

}
