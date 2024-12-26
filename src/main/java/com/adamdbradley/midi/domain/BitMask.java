package com.adamdbradley.midi.domain;

import java.util.BitSet;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class BitMask {

    private final BitSet bitSet;

    public static final BitMask EMPTY = new BitMask(0x0L);
    public static final BitMask ONE_ON = new BitMask(0x01L);
    public static final BitMask FOUR_ON = new BitMask(0x0FL);

    public BitMask(final long value) {
        this(BitSet.valueOf(new long[] { value }));
    }

    public static BitMask of(final long value) {
        return new BitMask(value);
    }

    public Stream<Boolean> stream(int count) {
        return IntStream.range(0, count)
                .mapToObj(bitSet::get);
    }

    public Stream<Byte> streamAsBooleanBytes(int count) {
        return stream(count)
                .map(on -> (byte) (on ? 1 : 0));
    }
}
