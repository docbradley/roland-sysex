package com.adamdbradley.midi.domain.controller;

import java.util.List;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
@EqualsAndHashCode
@Getter
public class Program {

    // TODO: Global Setup
    private final List<Chain> chains;
    private final List<Patch> patches;

}
