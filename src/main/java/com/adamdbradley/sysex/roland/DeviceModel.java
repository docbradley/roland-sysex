package com.adamdbradley.sysex.roland;

public enum DeviceModel {

    /**
     * The original.  The monster.
     */
    A_80 (3, new byte[] { (byte) 0x27 }, false),

    /**
     * Includes the simple A-90, the A-90 with VE-RD1, and the A-90EX.
     */
    A_90(4, new byte[] { (byte) 0x7D }, true),

    /**
     * Use {@link #Roland_JV_5080} messages;
     * unsupported attributes are simply ignored.
     */
    JV_3080(4, new byte[] { (byte) 0x00, (byte) 0x10 }, true),

    JV_5080(4, new byte[] { (byte) 0x00, (byte) 0x10 }, true);

    private final int addressLength;
    private final byte[] modelId;
    private final boolean checksumBody;

    public int addressLength() {
        return addressLength;
    }

    public byte[] modelIdAsBytes() {
        return modelId;
    }

    public boolean checksumBody() {
        return checksumBody;
    }

    private DeviceModel(final int addressLength, final byte[] modelId,
            final boolean checksumBody) {
        this.addressLength = addressLength;
        this.modelId = modelId;
        this.checksumBody = checksumBody;
    }

}
