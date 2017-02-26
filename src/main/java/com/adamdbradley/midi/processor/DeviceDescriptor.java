package com.adamdbradley.midi.processor;

import java.io.Serializable;

import javax.sound.midi.MidiDevice;

import com.adamdbradley.midi.DeviceFinder;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Builder
@RequiredArgsConstructor
@EqualsAndHashCode
@Getter
@ToString
public final class DeviceDescriptor implements Serializable {

    private static final long serialVersionUID = 1;

    private final String name;
    private final boolean input;
    private final boolean output;

    // TODO: Cache
    public static DeviceDescriptor of(final MidiDevice device) {
        return new DeviceDescriptor(device);
    }

    public DeviceDescriptor(MidiDevice device) {
        this(device.getDeviceInfo().getName(),
                device.getMaxTransmitters() != 0,
                device.getMaxReceivers() != 0);
    }

    public MidiDevice find() {
        return new DeviceFinder()
                .find(
                        DeviceFinder.nameMatchFilter(getName()),
                        isInput() ? DeviceFinder.MUST_HAVE_INPUT : DeviceFinder.TRUE,
                        isOutput() ? DeviceFinder.MUST_HAVE_OUTPUT : DeviceFinder.TRUE
                ).findFirst()
                .get();
    }

}
