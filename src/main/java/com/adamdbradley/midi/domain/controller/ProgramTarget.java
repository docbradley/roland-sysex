package com.adamdbradley.midi.domain.controller;

import com.adamdbradley.midi.Update;

public interface ProgramTarget {

    <T extends Update> T materialize(Program p);

    <T extends Update> T materialize(Chain c);

    <T extends Patch> T materialize(Patch p);

}
