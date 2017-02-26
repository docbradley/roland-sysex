package com.adamdbradley.midi;

import org.junit.Test;

import com.google.common.collect.ImmutableList;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Transmitter;

public class TestDeviceFinder {

    @Test
    public void nameMatchFilter() {
        final Predicate<MidiDevice> p = DeviceFinder.nameMatchFilter("ABC123");
        assertTrue(p.test(fakeDevice("ABC123")));
        assertFalse(p.test(fakeDevice("AABC123")));
        assertFalse(p.test(fakeDevice("BC123")));
        assertFalse(p.test(fakeDevice("ABC1234")));
        assertFalse(p.test(fakeDevice("ABC12")));
    }

    @Test
    public void regexFilter() {
        final Predicate<MidiDevice> p = DeviceFinder.regexFilter("^[abc]+$");
        assertTrue(p.test(fakeDevice("a")));
        assertTrue(p.test(fakeDevice("cccccccccccccccccccccccccccccccc")));
        assertTrue(p.test(fakeDevice("bacbacbacbacbacbacbacbacbacbacbacbacbacbac")));
        assertFalse(p.test(fakeDevice("")));
        assertFalse(p.test(fakeDevice("abcdabc")));
        assertFalse(p.test(fakeDevice("1")));
    }

    @Test
    public void inputFilter() {
        assertTrue(DeviceFinder.MUST_HAVE_INPUT.test(fakeDevice(PortType.BOTH)));
        assertTrue(DeviceFinder.MUST_HAVE_INPUT.test(fakeDevice(PortType.IN)));
        assertFalse(DeviceFinder.MUST_HAVE_INPUT.test(fakeDevice(PortType.OUT)));
    }

    @Test
    public void outputFilter() {
        assertTrue(DeviceFinder.MUST_HAVE_OUTPUT.test(fakeDevice(PortType.BOTH)));
        assertTrue(DeviceFinder.MUST_HAVE_OUTPUT.test(fakeDevice(PortType.OUT)));
        assertFalse(DeviceFinder.MUST_HAVE_OUTPUT.test(fakeDevice(PortType.IN)));
    }

    @Test
    public void trueFilter() {
        for (int i=0; i<100; i++) {
            assertTrue(DeviceFinder.TRUE.test(fakeDevice()));
        }
    }

    @Test
    public void falseFilter() {
        for (int i=0; i<100; i++) {
            assertFalse(DeviceFinder.FALSE.test(fakeDevice()));
        }
    }

    @Test
    public void singlePredicate() {
        final String uuid = UUID.randomUUID().toString();
        final DeviceFinder finder = new DeviceFinder(() -> { return ImmutableList
                .<MidiDevice>of(
                        fakeDevice(),
                        fakeDevice(uuid),
                        fakeDevice(),
                        fakeDevice()
                        )
                .stream(); });
        final List<MidiDevice> devices = finder
                .find(DeviceFinder.nameMatchFilter(uuid))
                .collect(Collectors.toList());
        assertEquals(1, devices.size());
        assertEquals(uuid, devices.get(0).getDeviceInfo().getName());
    }

    @Test
    public void listPredicates() {
        final String uuid = UUID.randomUUID().toString();
        final DeviceFinder finder = new DeviceFinder(() -> { return ImmutableList
                .<MidiDevice>of(
                        fakeDevice(),
                        fakeDevice(uuid, PortType.IN),
                        fakeDevice(uuid, PortType.OUT),
                        fakeDevice()
                        )
                .stream(); });
        final List<MidiDevice> devices = finder
                .find(Arrays.asList(
                        DeviceFinder.MUST_HAVE_INPUT,
                        DeviceFinder.nameMatchFilter(uuid)
                ))
                .collect(Collectors.toList());
        assertEquals(1, devices.size());
        assertEquals(uuid, devices.get(0).getDeviceInfo().getName());
        assertEquals(devices.get(0).getMaxReceivers(), 0);
        assertThat(devices.get(0).getMaxTransmitters(), greaterThan((int) 1));
    }

    @Test
    public void varargsPredicates() {
        final String uuid = UUID.randomUUID().toString();
        final DeviceFinder finder = new DeviceFinder(() -> { return ImmutableList
                .<MidiDevice>of(
                        fakeDevice(),
                        fakeDevice(uuid, PortType.IN),
                        fakeDevice(uuid, PortType.OUT),
                        fakeDevice()
                        )
                .stream(); });
        final List<MidiDevice> devices = finder
                .find(
                        DeviceFinder.MUST_HAVE_INPUT,
                        DeviceFinder.nameMatchFilter(uuid)
                )
                .collect(Collectors.toList());
        assertEquals(1, devices.size());
        assertEquals(uuid, devices.get(0).getDeviceInfo().getName());
        assertEquals(devices.get(0).getMaxReceivers(), 0);
        assertThat(devices.get(0).getMaxTransmitters(), greaterThan((int) 1));
    }

    enum PortType {
        OUT,
        IN,
        BOTH
    }

    private static final Random rng = new Random();

    private MidiDevice fakeDevice(final String name) {
        return fakeDevice(name, PortType.values()[rng.nextInt(PortType.values().length)]);
    }

    private MidiDevice fakeDevice(final PortType type) {
        return fakeDevice(UUID.randomUUID().toString(), type);
    }

    private MidiDevice fakeDevice() {
        return fakeDevice(UUID.randomUUID().toString(),
                PortType.values()[rng.nextInt(PortType.values().length)]);
    }

    private MidiDevice fakeDevice(final String name, final PortType type) {
        return new MidiDevice() {
            @Override
            public Info getDeviceInfo() {
                return new Info(name, "The Magic Vendor", UUID.randomUUID().toString(), "v3.14") {};
            }
            @Override
            public int getMaxTransmitters() {
                switch (type) {
                case OUT:
                    return 0;
                default:
                    return Integer.MAX_VALUE;
                }
            }
            @Override
            public int getMaxReceivers() {
                switch (type) {
                case IN:
                    return 0;
                default:
                    return Integer.MAX_VALUE;
                }
            }
            @Override
            public void open() throws MidiUnavailableException {
                throw new IllegalStateException();
            }
            @Override
            public boolean isOpen() {
                return false;
            }
            @Override
            public List<Transmitter> getTransmitters() {
                throw new IllegalStateException();
            }
            @Override
            public Transmitter getTransmitter() throws MidiUnavailableException {
                throw new IllegalStateException();
            }
            @Override
            public List<Receiver> getReceivers() {
                throw new IllegalStateException();
            }
            @Override
            public Receiver getReceiver() throws MidiUnavailableException {
                throw new IllegalStateException();
            }
            @Override
            public long getMicrosecondPosition() {
                throw new IllegalStateException();
            }
            @Override
            public void close() {
                throw new IllegalStateException();
            }
        };
    }

}
