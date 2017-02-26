package com.adamdbradley.midi.processor;

import java.util.List;
import java.util.stream.Collectors;

import javax.sound.midi.MidiDevice;

import com.adamdbradley.midi.message.ContextualMessage;

import lombok.RequiredArgsConstructor;

/**
 * Wraps a {@link Program} with the ability to interface with messages
 * from and to real {@link MidiDevice}s.
 */
@RequiredArgsConstructor
public final class RealizedProgram {

    private final Program program;

    public List<ContextualMessage> process(final ContextualMessage input) {
        return program
                .process(ProgramMessage.fromMessage(input))
                .stream()
                .map(ProgramMessage::toContextualMessage)
                .collect(Collectors.toList());
    }

}
