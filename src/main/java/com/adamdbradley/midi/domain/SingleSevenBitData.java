package com.adamdbradley.midi.domain;

import javax.annotation.Nonnull;

import lombok.EqualsAndHashCode;
import lombok.NonNull;

/**
 * Primitive support for a variety of non-marker MIDI values.
 */
@EqualsAndHashCode
public abstract class SingleSevenBitData {

    private final boolean basisOne;

    @NonNull @Nonnull private final String name;
    private final byte data;

    protected SingleSevenBitData(final boolean basisOne, final String name, final int data) {
        this(basisOne, name, data, 0x0000007F);
    }

    protected SingleSevenBitData(final boolean basisOne,
            @Nonnull final String name,
            final int data, final int allowedBits) {
        this.basisOne = basisOne;
        this.name = name;
        if ((allowedBits & 0xFFFFFF80) != 0) {
            throw new IllegalStateException("Trying to use a mask w/ more than 7 bits");
        }
        if ((data & (~allowedBits)) != 0) {
            System.err.println("allowed: " + Integer.toHexString(allowedBits));
            System.err.println("~allowed: " + Integer.toHexString(~allowedBits));
            System.err.println("data: " + Integer.toHexString(data));
            System.err.println("zero: " + Integer.toHexString(data & (~allowedBits)));
            throw new IllegalArgumentException("Value must be in "
                    + Integer.toHexString(allowedBits) + ": " + data);
        }
        this.data = (byte) data;
    }

    /**
     * Basis-0 value
     * @return
     */
    public final byte getData() {
        return data;
    }

    /**
     * May be basis-1 (if it's that type of message).
     * @return
     */
    public final int getNumber() {
        return ((int) getData()) + (basisOne ? 1 : 0);
    }

    public final String toString() {
        return name + "[" + Integer.toHexString(data) + "]";
    }

}
