package com.adamdbradley.midi.processor;

import java.io.Serializable;

import com.adamdbradley.midi.domain.Channel;
import com.adamdbradley.midi.domain.ContinuousControllerId;
import com.adamdbradley.midi.domain.Note;
import com.adamdbradley.midi.domain.ProgramChange;
import com.adamdbradley.midi.message.ControlChangeMessage;
import com.adamdbradley.midi.message.NoteOffMessage;
import com.adamdbradley.midi.message.NoteOnMessage;
import com.adamdbradley.midi.message.ProgramChangeMessage;
import com.google.common.collect.ImmutableList;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 
 */
@RequiredArgsConstructor
@Getter
@Builder
public final class Rule implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Recognizer recognizer;
    private final Mapper mapper;

    public static Rule reactor(final DeviceDescriptor device,
            final Channel channel,
            final ContinuousControllerId ccId,
            final boolean onOff,
            final ImmutableList<ProgramMessage> response) {
        return Rule.builder()
                .recognizer(x -> x.toParsed(ControlChangeMessage.class)
                        .filter(ccm -> ccm.getChannel() == channel)
                        .filter(ccm -> ccm.getController() == ccId)
                        .filter(ccm -> (ccm.getValue().getData() & 0x40) == (onOff ? 0x40 : 0x00)) // FIXME: 0x40 or 0x80?
                        .isPresent())
                .mapper(x -> response)
                .build();
    }

    public static Rule reactor(final DeviceDescriptor device,
            final Channel channel,
            final ProgramChange program,
            final ImmutableList<ProgramMessage> response) {
        return Rule.builder()
                .recognizer(x -> x.toParsed(ProgramChangeMessage.class)
                        .filter(pcm -> pcm.getChannel() == channel)
                        .filter(pcm -> pcm.getProgram() == program)
                        .isPresent())
                .mapper(x -> response)
                .build();
    }

    public static Rule reactor(final DeviceDescriptor device,
            final Channel channel,
            final Note note,
            final boolean onOff,
            final ImmutableList<ProgramMessage> response) {
        if (onOff) {
            return Rule.builder()
                    .recognizer(x -> x.toParsed(NoteOnMessage.class)
                            .filter(nom -> nom.getChannel() == channel)
                            .filter(nom -> nom.getNote() == note)
                            .isPresent())
                    .mapper(x -> response)
                    .build();
        } else {
            return Rule.builder()
                    .recognizer(x -> x.toParsed(NoteOffMessage.class)
                            .filter(nom -> nom.getChannel() == channel)
                            .filter(nom -> nom.getNote() == note)
                            .isPresent())
                    .mapper(x -> response)
                    .build();
        }
    }

}
