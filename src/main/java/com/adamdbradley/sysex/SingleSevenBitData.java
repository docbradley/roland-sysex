package com.adamdbradley.sysex;

import javax.annotation.Nonnull;

import lombok.EqualsAndHashCode;
import lombok.NonNull;

/**
 * Primitive support for a variety of non-marker MIDI values.
 */
@EqualsAndHashCode
public abstract class SingleSevenBitData {

    @NonNull @Nonnull private final String name;
    private final byte data;

    protected SingleSevenBitData(final String name, final int data) {
        this(name, data, 0x0000007F);
    }

    protected SingleSevenBitData(@Nonnull final String name,
            final int data, final int allowedBits) {
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

    public final byte getData() {
        return data;
    }

    public final String toString() {
        return name + "[" + Integer.toHexString(data) + "]";
    }

}
