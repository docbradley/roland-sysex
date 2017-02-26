package com.adamdbradley.midi;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.SysexMessage;

import static org.junit.Assert.*;

public class TestMidiUtils {

    @Test
    public void testToString() throws Exception {
        final Map<String, ? extends MidiMessage> expectations = ImmutableMap
                .<String, MidiMessage>builder()
                .put("9a 2b 1c", new ShortMessage((byte) 0x9a, (byte) 0x2b, (byte) 0x1c))
                .put("f0 02 03 04 05 06 07 08 f7", new SysexMessage(new byte[] {
                        (byte) 0xf0, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, (byte) 0xf7
                }, 9))
                .build();
        for (Map.Entry<String, ? extends MidiMessage> entry: expectations.entrySet()) {
            assertEquals(entry.getKey(), MidiUtils.toString(entry.getValue()));
        }
    }

    @Test
    public void enumerateDeviceInfo() throws Exception {
        final Stream<MidiDevice.Info> deviceInfos = MidiUtils.enumerateDeviceInfo();
        assertNotNull(deviceInfos);
        deviceInfos.forEach(i -> {
            assertNotNull(i);
            assertNotNull(i.getDescription());
        });
    }

    @Test
    public void enumerateDevices() throws Exception {
        final Stream<MidiDevice> devices = MidiUtils.enumerateDevices();
        assertNotNull(devices);
        devices.forEach(i -> {
            assertNotNull(i);
            assertFalse(i.isOpen());
        });
    }

    @Test
    public void infoToDevice() {
        final Stream<MidiDevice.Info> deviceInfos = MidiUtils.enumerateDeviceInfo();
        assertNotNull(deviceInfos);
        deviceInfos
                .map(MidiUtils::infoToDevice)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(dev -> {
            assertFalse(dev.isOpen());
        });
    }

}
