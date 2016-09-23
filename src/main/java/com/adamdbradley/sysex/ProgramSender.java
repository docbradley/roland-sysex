package com.adamdbradley.sysex;

import java.util.function.Consumer;

import javax.sound.midi.Receiver;

import com.adamdbradley.sysex.roland.PatchModel;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProgramSender implements Consumer<Receiver>{

    private final PatchModel patchModel;

    @Override
    public void accept(final Receiver receiver) {
        patchModel
                .getDataSetMessages()
                .stream()
                .forEachOrdered(msg -> {
                    receiver.send(msg.getMessage(), 0L);
                    try {
                        Thread.sleep(20);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }

}
