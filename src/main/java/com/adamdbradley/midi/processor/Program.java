package com.adamdbradley.midi.processor;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import com.adamdbradley.midi.domain.ProgramChange;
import com.adamdbradley.midi.message.Message;
import com.google.common.collect.ImmutableList;

import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * {@link Program} is a collection of {@link Rule}s and the ability to
 * (via {@link #process(Message)}) evaluate a {@link Message} against
 * them to produce zero or more new {@link Message}s.
 */
@RequiredArgsConstructor
@Builder
public final class Program implements Serializable {

    private static final long serialVersionUID = 1L;

    @Nonnull @NonNull
    private final Collection<Rule> rules;

    public Stream<ProgramMessage> process(final ProgramMessage message) {
        return rules.stream()
                .flatMap(r -> r.process(message));
    }

    public static Program switchingProgram(final Function<ProgramMessage, Optional<ProgramChange>> switchFinder,
            final Map<ProgramChange, Program> programs) {
        return new Program(ImmutableList.of(
                Rule.builder()
                        .recognizer(m -> true)
                        .mapper(new SwitchingMapper(switchFinder, programs))
                        .build()
                ));
    }

    @RequiredArgsConstructor
    private static final class SwitchingMapper implements Mapper {
        private static final long serialVersionUID = 1L;

        private final Function<ProgramMessage, Optional<ProgramChange>> switchFinder;
        private final Map<ProgramChange, Program> programs;

        @Nonnull private ProgramChange program = ProgramChange.of(1);

        @Override
        public List<ProgramMessage> apply(final ProgramMessage t) {
            final Optional<ProgramChange> isSwitch = switchFinder.apply(t);
            program = isSwitch.orElse(program);
            return programs.get(program).process(t).collect(Collectors.toList());
        }
    }

}
