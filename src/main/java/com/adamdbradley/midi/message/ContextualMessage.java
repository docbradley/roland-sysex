package com.adamdbradley.midi.message;

import javax.sound.midi.MidiDevice;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ContextualMessage {

    private final MidiDevice device;
    private final Message<?> message;

}
