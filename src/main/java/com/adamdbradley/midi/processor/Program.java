package com.adamdbradley.midi.processor;

import java.io.Serializable;
import java.util.List;

import com.adamdbradley.midi.message.Message;
import com.google.common.collect.ImmutableList;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Program implements Serializable {

    private static final long serialVersionUID = 1L;

    private final List<Rule> rules;

    public List<Message<?>> process(final Message<?> message) {
        final ImmutableList.Builder<Message<?>> builder = ImmutableList.builder();
        for (Rule rule: rules) {
            if (rule.getRecognizer().test(message)) {
                builder.addAll(rule.getMapper().apply(message));
            }
        }
        return builder.build();
    }

}
