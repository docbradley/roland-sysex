package com.adamdbradley.midi.processor;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

import com.google.common.collect.ImmutableList;

/**
 * Marker interface for the ability to transform one
 * {@link ProgramMessage} into a (potentially empty)
 * list of the same.  Can encapsulate routing,
 * transformation, muxing/demuxing, etc.
 * A {@link Mapper} MAY be stateful, but this should
 * be done advisedly since there is no panic mechanism
 * to signal a reset should its state become incorrect.
 */
@FunctionalInterface
public interface Mapper extends Function<ProgramMessage, List<ProgramMessage>>, Serializable {

    public static Mapper IDENTITY = ImmutableList::of;

    public static Mapper route(final DeviceDescriptor output) {
        return (m) -> ImmutableList.of(new ProgramMessage(output, m.getMessage()));
    }

}
