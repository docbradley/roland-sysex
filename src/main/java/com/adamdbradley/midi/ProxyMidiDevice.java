package com.adamdbradley.midi;

import java.io.Serializable;
import java.util.List;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

import lombok.RequiredArgsConstructor;

/**
 * Wrap {@link MidiDevice} to make it {@link Serializable}
 */
@RequiredArgsConstructor
public class ProxyMidiDevice implements MidiDevice, Serializable {

    private static final long serialVersionUID = 1L;

    private final String name;
    private final boolean input;
    private final boolean output;

    public ProxyMidiDevice(final MidiDevice device) {
        this(device.getDeviceInfo().getName(),
                device.getMaxTransmitters() != 0,
                device.getMaxReceivers() != 0);
    }

    private transient MidiDevice actual = null;

    private MidiDevice actual() {
        if (actual == null) {
            actual = new DeviceFinder()
                    .find(DeviceFinder.nameMatchFilter(name),
                            input ? DeviceFinder.MUST_HAVE_INPUT : DeviceFinder.TRUE,
                            output ? DeviceFinder.MUST_HAVE_OUTPUT : DeviceFinder.TRUE)
                    .findFirst()
                    .get();
        }
        return actual;
    }

    @Override
    public void close() {
        actual().close();
    }
    @Override
    public Info getDeviceInfo() {
        return actual().getDeviceInfo();
    }
    @Override
    public int getMaxReceivers() {
        return actual().getMaxReceivers();
    }
    @Override
    public int getMaxTransmitters() {
        return actual().getMaxTransmitters();
    }
    @Override
    public long getMicrosecondPosition() {
        return actual().getMicrosecondPosition();
    }
    @Override
    public Receiver getReceiver() throws MidiUnavailableException {
        return actual().getReceiver();
    }
    @Override
    public List<Receiver> getReceivers() {
        return actual().getReceivers();
    }
    @Override
    public Transmitter getTransmitter() throws MidiUnavailableException {
        return actual().getTransmitter();
    }
    @Override
    public List<Transmitter> getTransmitters() {
        return actual.getTransmitters();
    }
    @Override
    public boolean isOpen() {
        return actual.isOpen();
    }
    @Override
    public void open() throws MidiUnavailableException {
        actual.open();
    }

}
