package com.adamdbradley.midi.domain.controller;

import java.util.List;
import java.util.Optional;

import com.adamdbradley.midi.domain.Channel;
import com.adamdbradley.midi.domain.ContinuousControllerId;
import com.adamdbradley.midi.domain.Modulation;
import com.adamdbradley.midi.domain.Note;
import com.adamdbradley.midi.domain.ProgramChange;
import com.adamdbradley.midi.domain.Volume;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
@EqualsAndHashCode
public class Zone {

    private final Note startKey;
    private final Note endKey;
    private final Channel channel;
    private final Transposition transpose;
    private final VelocityCurve velocityCurve;
    private final AftertouchCurve aftertouchCurve;
    private final ProgramChange programChange;
    private final Volume volume;
    private final Modulation modulation;
    private final PitchBend pitchBend;
    private final List<Optional<ContinuousControllerId>> sliders;
    private final List<Optional<ContinuousControllerId>> switches;
    private final List<Optional<ContinuousControllerId>> footControllers;

}
