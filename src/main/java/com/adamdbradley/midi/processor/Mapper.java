package com.adamdbradley.midi.processor;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

import com.adamdbradley.midi.message.Message;
import com.google.common.collect.ImmutableList;

public interface Mapper extends Function<Message<?>, List<Message<?>>>, Serializable {

    public static Mapper IDENTITY = ImmutableList::of;

}
