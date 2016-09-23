package com.adamdbradley.sysex;

public abstract class SingleSevenBitData {

    private final byte data;

    protected SingleSevenBitData(final int data) {
        this(data, 0x0000007F);
    }

    protected SingleSevenBitData(final int data, final int allowedBits) {
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

    public byte getData() {
        return data;
    }

}
