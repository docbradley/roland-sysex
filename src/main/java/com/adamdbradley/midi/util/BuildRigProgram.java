package com.adamdbradley.midi.util;

import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.adamdbradley.midi.domain.Channel;
import com.adamdbradley.midi.domain.ProgramChange;
import com.adamdbradley.midi.message.ProgramChangeMessage;
import com.adamdbradley.midi.processor.DeviceDescriptor;
import com.adamdbradley.midi.processor.Program;
import com.adamdbradley.midi.processor.ProgramMessage;
import com.adamdbradley.midi.processor.Recognizer;
import com.adamdbradley.midi.processor.Rule;
import com.google.common.collect.ImmutableList;

public class BuildRigProgram {

    public static void main(String[] args) throws Exception {
        final Path file = Paths.get(args[0]);
        final Program program = build();
        write(program, file);
    }

    private static void write(final Program program, final Path file) throws IOException {
        try (OutputStream fos = Files.newOutputStream(file, StandardOpenOption.CREATE_NEW)) {
            try (ObjectOutput oos = new ObjectOutputStream(fos)) {
                oos.writeObject(program);
            }
        }
    }

    private static Program build() {
        final DeviceDescriptor[] muxIn = new DeviceDescriptor[] {};
        final DeviceDescriptor[] muxOut = new DeviceDescriptor[] {};

        return Program.builder()
                .rules(ImmutableList.of(
                        Rule.builder() // Dummy rule: recognize and discard everything
                                .recognizer(Recognizer.EVERYTHING) // recognize everything
                                .mapper(x -> ImmutableList.of()) // discard everything
                                .build(),
                        Rule.reactor(muxIn[6],
                                Channel.of(16),
                                ProgramChange.of(1),
                                ImmutableList.of(
                                        ProgramMessage.of(
                                                muxOut[0],
                                                new ProgramChangeMessage(Channel.of(1), ProgramChange.of(1))
                                                )
                                        )),
                        Rule.builder() // Dummy rule: recognize and discard nothing
                                .recognizer(Recognizer.NOTHING)
                                .mapper(x -> ImmutableList.of(x))
                                .build()
                        ))
                .build();
    }

}
