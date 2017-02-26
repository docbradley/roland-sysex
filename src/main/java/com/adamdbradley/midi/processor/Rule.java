package com.adamdbradley.midi.processor;

import java.io.Serializable;

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

}
