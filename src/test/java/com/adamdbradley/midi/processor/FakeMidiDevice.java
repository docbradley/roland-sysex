package com.adamdbradley.midi.processor;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public final class FakeMidiDevice implements MidiDevice {

    private static final AtomicInteger sequence = new AtomicInteger(0);

    private final MidiDevice.Info info = new MidiDevice.Info("" + sequence.getAndIncrement() + "/" + UUID.randomUUID(),
            "adamdbradley.com",
            "A pretend MIDI device",
            "0.0.0.0") {};

    @Override
    public void close() { throw new RuntimeException(); }

    @Override
    public Info getDeviceInfo() { return info; }

    @Override
    public int getMaxReceivers() { throw new RuntimeException(); }

    @Override
    public int getMaxTransmitters() { throw new RuntimeException(); }

    @Override
    public long getMicrosecondPosition() { throw new RuntimeException(); }

    @Override
    public Receiver getReceiver() { throw new RuntimeException(); }

    @Override
    public List<Receiver> getReceivers() { throw new RuntimeException(); }

    @Override
    public Transmitter getTransmitter() { throw new RuntimeException(); }

    @Override
    public List<Transmitter> getTransmitters() { throw new RuntimeException(); }

    @Override
    public boolean isOpen() { throw new RuntimeException(); }

    @Override
    public void open() { throw new RuntimeException(); }

}