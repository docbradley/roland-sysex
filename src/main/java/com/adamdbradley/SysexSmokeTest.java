package com.adamdbradley;

import java.util.Optional;
import java.util.concurrent.Callable;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Receiver;

import com.adamdbradley.midi.MidiUtils;
import com.adamdbradley.midi.domain.Channel;
import com.adamdbradley.midi.domain.ContinuousControllerId;
import com.adamdbradley.midi.domain.ProgramChange;
import com.adamdbradley.midi.domain.ProgramSender;
import com.adamdbradley.midi.domain.Volume;
import com.adamdbradley.midi.sysex.roland.a80.PatchModel;
import com.adamdbradley.midi.sysex.roland.a80.PatchNumber;
import com.adamdbradley.midi.sysex.roland.a80.ZoneModel;
import com.google.common.collect.ImmutableList;

/**
 * A smoke test.
 */
public class SysexSmokeTest implements Callable<Void> {

    public static void main(String[] args) throws Exception {
        new SysexSmokeTest().call();
    }

    @Override
    public Void call() throws Exception {
        final PatchModel patchModel = 
                PatchModel.builder()
                        .patchNumber(PatchNumber.of(1))
                        .patchName("ADB test zzz")
                        .zones(ImmutableList.of(
                                ZoneModel.builder()
                                        .channel(Channel.of(1))
                                        .volume(Volume.of(127))
                                        .pedalControls(ImmutableList.of(
                                                Optional.of(ContinuousControllerId.of(64)), // Sustain
                                                Optional.of(ContinuousControllerId.of(66)), // Sostenuto (only sustain currently-sounding notes)
                                                Optional.of(ContinuousControllerId.of(11)), // Expression (post-volume)
                                                Optional.of(ContinuousControllerId.of(5)) // Portamento Time
                                                ))
                                        .sliderControls(ImmutableList.of(
                                                Optional.of(ContinuousControllerId.of(7)), // Volume
                                                Optional.empty(),
                                                Optional.empty(),
                                                Optional.empty()
                                                ))
                                        .switchControls(ImmutableList.of(
                                                Optional.of(ContinuousControllerId.of(65)), // Portamento
                                                Optional.empty(),
                                                Optional.empty(),
                                                Optional.empty()
                                                ))
                                        .build(),
                                ZoneModel.builder()
                                        .channel(Channel.of(2))
                                        .programChange(ProgramChange.of(1))
                                        .build(),
                                ZoneModel.builder()
                                        .channel(Channel.of(3))
                                        .programChange(ProgramChange.of(2))
                                        .build(),
                                ZoneModel.builder()
                                        .channel(Channel.of(4))
                                        .programChange(ProgramChange.of(3))
                                        .build()
                                        ))
                        .build();

        patchModel.getMessages().stream()
                .forEach(msg -> {
                    System.err.println(MidiUtils.toString(msg.getMessage()));
                });

        final ProgramSender sender = new ProgramSender(patchModel);

        for (MidiDevice.Info info: MidiSystem.getMidiDeviceInfo()) {
            System.err.println(info.getName() + "//" + info.getDescription());
            if (!info.getName().contains("MXPXT" /* "MIDI4x4" */)) {
                continue;
            }
            System.err.println("Trying " + info);
            try (MidiDevice device = MidiSystem.getMidiDevice(info)) {
                if (device.getMaxReceivers() != 0) {
                    if (!device.isOpen()) {
                        device.open();
                    }
                    try (Receiver receiver = device.getReceiver()) {
                        System.err.println("Sending...");
                        sender.accept(receiver);
                    }
                }
            }
        }

        return null;
    }

}
