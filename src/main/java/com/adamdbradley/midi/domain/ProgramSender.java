package com.adamdbradley.midi.domain;

import java.util.Collection;
import java.util.function.Consumer;

import javax.sound.midi.Receiver;

import com.adamdbradley.midi.Update;
import com.adamdbradley.midi.sysex.roland.UpdatingModel;
import com.google.common.collect.ImmutableList;

import lombok.RequiredArgsConstructor;

/**
 * Wraps an {@link Update} with a strategy object that knows how to pace
 * delivery of the constituent messages.
 */
@RequiredArgsConstructor
public class ProgramSender implements Consumer<Receiver>{

    private final Collection<? extends UpdatingModel> patchModels;

    public ProgramSender(final UpdatingModel singlePatchModel) {
        this(ImmutableList.of(singlePatchModel));
    }

    @Override
    public void accept(final Receiver receiver) {
        patchModels
                .stream()
                .forEach(patchModel -> patchModel.getMessages()
                        .stream()
                        .forEachOrdered(msg -> {
                            receiver.send(msg.getMessage(), 0L);
                            try {
                                Thread.sleep(20);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }));
    }

}
