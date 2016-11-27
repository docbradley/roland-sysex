package com.adamdbradley.midi.domain.controller;

import com.adamdbradley.midi.Update;

public class Materializer {

    public static Update materialize(final Program program,
            final ProgramTarget machine) {
        return machine.materialize(program);
    }

}
