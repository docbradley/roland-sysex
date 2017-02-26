package com.adamdbradley.midi.processor;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;

import com.adamdbradley.midi.message.ContextualMessage;
import com.adamdbradley.midi.message.Message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Like a {@link Message}, but divorced from concrete {@link MidiDevice}s.
 */
@Getter
@RequiredArgsConstructor
public final class ProgramMessage {

    private final DeviceDescriptor device;
    private final MidiMessage message;

    public static ProgramMessage fromMessage(final ContextualMessage message) {
        return new ProgramMessage(DeviceDescriptor.of(message.getDevice()), message.getMessage().getMessage());
    }

    public ContextualMessage toContextualMessage() {
        return new ContextualMessage(device.find(), Message.parse(message));
    }
}
