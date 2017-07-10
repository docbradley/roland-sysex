package com.adamdbradley.midi.processor;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

import javax.sound.midi.MidiDevice;

import com.adamdbradley.midi.DeviceFinder;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Builder
@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
@EqualsAndHashCode
@Getter
@ToString
public final class DeviceDescriptor implements Serializable {

    private static final long serialVersionUID = 1;

    private final String name;
    private final boolean input;
    private final boolean output;

    private static ConcurrentHashMap<String, DeviceDescriptor> instances = new ConcurrentHashMap<>();

    public static DeviceDescriptor of(final String name, final boolean input, final boolean output) {
        return new DeviceDescriptor(name, input, output).intern();
    }

    public static DeviceDescriptor of(final MidiDevice device) {
        return new DeviceDescriptor(device.getDeviceInfo().getName(),
                device.getMaxTransmitters() != 0,
                device.getMaxReceivers() != 0).intern();
    }

    private DeviceDescriptor intern() {
        final String key = toString();
        if (!instances.containsKey(key)) {
            synchronized(instances) {
                if (!instances.containsKey(key)) {
                    instances.put(key, this);
                }
            }
        }
        return instances.get(key);
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
