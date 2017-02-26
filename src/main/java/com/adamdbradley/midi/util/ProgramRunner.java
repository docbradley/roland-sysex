package com.adamdbradley.midi.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;

import com.adamdbradley.midi.DeviceFinder;
import com.adamdbradley.midi.message.ContextualMessage;
import com.adamdbradley.midi.message.Message;
import com.adamdbradley.midi.processor.Program;
import com.adamdbradley.midi.processor.RealizedProgram;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProgramRunner implements Callable<Void> {

    private final Program program;

    private volatile RealizedProgram realized;
    private volatile Map<MidiDevice.Info, Receiver> outs;

    public Void call() throws InterruptedException {
        realized = new RealizedProgram(program);

        final List<MidiDevice> devices = new DeviceFinder().find()
                .filter((md) -> {
                    try {
                        md.open();
                    } catch (MidiUnavailableException e) {
                        return false;
                    }
                    return true;
                })
                .collect(Collectors.toList());

        outs = devices.stream()
                .filter(d -> d.getMaxReceivers() > 0)
                .collect(Collectors.toMap(d -> d.getDeviceInfo(),
                        d -> {
                            try {
                                return d.getReceiver();
                            } catch (MidiUnavailableException e) {
                                throw new RuntimeException(e);
                            }
                        }));

        devices.stream()
                .filter(d -> d.getMaxTransmitters() > 0)
                .forEach(d -> {
                    try {
                        d.getTransmitter().setReceiver(new ProgramReceiver(d));
                    } catch (MidiUnavailableException e) {
                        throw new RuntimeException(e);
                    }
                });

        while (true) {
            // An InterruptedException will blow out the top eventually
            try {
                Thread.sleep(Duration.ofDays(1).toMillis());
            } catch (InterruptedException e) {
                devices.stream().forEach(MidiDevice::close);
                return null;
            }
        }
    }

    @RequiredArgsConstructor
    private class ProgramReceiver implements Receiver {
        private final MidiDevice device;

        @Override
        public void send(final MidiMessage message, final long ignore) {
            final ContextualMessage input = new ContextualMessage(device, Message.parse(message));
            realized.process(input)
                    .forEach(cm -> {
                        outs.get(cm.getDevice().getDeviceInfo())
                                .send(message, 0L);
                    });
        }

        @Override
        public void close() {
        }
    }

    public static void main(final String[] argv) throws Exception {
        final Program program;

        if (argv.length < 1) {
            throw new IllegalArgumentException("Missing <filename> argument");
        }
        final String filename = argv[0];
        try (FileInputStream fis = new FileInputStream(new File(filename));
                ObjectInputStream ois = new ObjectInputStream(fis)) {
            program = (Program) ois.readObject();
        }

        final ProgramRunner runner = new ProgramRunner(program);

        runner.call();
        System.out.println("Exiting");
    }

}
