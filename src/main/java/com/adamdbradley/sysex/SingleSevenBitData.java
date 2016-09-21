package com.adamdbradley.sysex;

public abstract class SingleSevenBitData {

    private byte data;

    protected SingleSevenBitData(final int data) {
        this(data, 0x0000007F);
    }

    protected SingleSevenBitData(final int data, final int allowedBits) {
        if ((data & (~allowedBits)) != 0) {
            throw new IllegalArgumentException("Value must be 0-127: " + data);
        }
    }

    public byte getData() {
        return data;
    }

}
