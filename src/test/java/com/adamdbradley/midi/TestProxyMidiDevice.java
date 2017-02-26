package com.adamdbradley.midi;

import org.junit.Test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.sound.midi.MidiDevice;

public class TestProxyMidiDevice {

    @Test
    public void fromDevice() throws Exception {
        final MidiDevice device = new DeviceFinder()
                .find(DeviceFinder.TRUE)
                .findFirst()
                .get();

        final ProxyMidiDevice reconstituted = writeAndRead(new ProxyMidiDevice(device));

        assertEquals(device.getDeviceInfo(), reconstituted.getDeviceInfo());
    }

    @Test
    public void fromIdentifiers() throws Exception {
        final MidiDevice device = new DeviceFinder()
                .find(DeviceFinder.TRUE)
                .findFirst()
                .get();

        final ProxyMidiDevice original = new ProxyMidiDevice(device.getDeviceInfo().getName(),
                device.getMaxTransmitters() > 0,
                device.getMaxReceivers() > 0);

        final ProxyMidiDevice reconstituted = writeAndRead(original);

        assertEquals(device.getDeviceInfo(), reconstituted.getDeviceInfo());
    }

    @SuppressWarnings("unchecked")
    private static <T> T writeAndRead(final T object) throws IOException, ClassNotFoundException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(object);
        final InputStream bais = new ByteArrayInputStream(baos.toByteArray());
        final ObjectInputStream ois = new ObjectInputStream(bais);
        return (T) ois.readObject();
    }

}
