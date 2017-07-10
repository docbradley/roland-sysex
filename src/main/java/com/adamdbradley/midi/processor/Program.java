package com.adamdbradley.midi.processor;

import java.io.Serializable;
import java.util.Collection;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import com.adamdbradley.midi.message.Message;

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

}
