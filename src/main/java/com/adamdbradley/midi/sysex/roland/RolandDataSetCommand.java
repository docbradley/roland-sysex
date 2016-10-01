package com.adamdbradley.midi.sysex.roland;

/**
 * Immutable wrapper for the address and payload portions of a
 * Roland Sysex message.
 */
public abstract class RolandDataSetCommand implements RolandSysexCommand {

    final byte[] payload;

    /**
     * 
     * @param model
     * @param address
     * @param data Starting with the first byte after address, ending before the end-of-sysex byte
     */
    protected RolandDataSetCommand(final InstrumentModel model,
            final int address,
            final byte[] data) {
        payload = build(model, address, data);
    }

    /**
     * 
     * @param model
     * @param address
     * @param data Excluding the checksum
     * @return Still excluding the checksum
     */
    private static byte[] build(final InstrumentModel model, final int address,
            final byte[] data) {
        if (data.length > 256) {
            throw new IllegalArgumentException("Can't build message over 256 bytes long");
        }
        final byte[] payload = new byte[model.addressLength() + data.length];
        final byte[] addressBytes = buildAddressBytes(model, address);
        System.arraycopy(addressBytes, 0, payload, 0, model.addressLength());
        System.arraycopy(data, 0, payload, model.addressLength(), data.length);
        return payload;
    }

    private static byte[] buildAddressBytes(InstrumentModel model, int address) {
        switch(model.addressLength()) {
        case 3:
            if ((address & 0xFF808080) != 0) {
                throw new IllegalArgumentException("Can't fit address "
                        + Integer.toHexString(address)
                        + " into three bytes");
            }
            return new byte[] { (byte) ((address >> 16) & 0x7F),
                    (byte) ((address >> 8) & 0x7F),
                    (byte) (address & 0x7F) };

        case 4:
            if ((address & 0xC0808080) != 0) { // Only allow MSB to be [0,3]
                throw new IllegalArgumentException("Can't fit address "
                        + Integer.toHexString(address)
                        + " into three bytes");
            }
            return new byte[] { (byte) ((address >> 24) & 0x7F),
                    (byte) ((address >> 16) & 0x7F),
                    (byte) ((address >> 8) & 0x7F),
                    (byte) (address & 0x7F) };

        default:
            throw new IllegalStateException("Unsupported addressLength from " + model);
        }
    }

    @Override
    public CommandId id() {
        return CommandId.DataSet;
    }

    @Override
    public byte[] bodyWithoutChecksum() {
        return payload;
    }

    /**
     * @param model
     * @param deviceId
     * @param payload From start of address through checksum
     * @return
     */
    public static RolandSysexCommand parse(final InstrumentModel model,
            final byte deviceId,
            final byte[] payload) {
        final byte[] addressBytes = new byte[4];
        final byte[] dataPayload;
        switch (model.addressLength()) {
        case 3:
            dataPayload = new byte[payload.length - 3];
            System.arraycopy(payload, 1, addressBytes, 1, 3);
            break;
        case 4:
            dataPayload = new byte[payload.length - 4];
            System.arraycopy(payload, 0, addressBytes, 0, 4);
        default:
            throw new IllegalArgumentException("Length not understood");
        }
        final int address = (addressBytes[0] * 0x010000)
                + (addressBytes[1] * 0x00010000)
                + (addressBytes[2] * 0x00000100)
                + (addressBytes[3]);

        // TODO: break out A-80/A-90 part messages to subtypes

        return new RolandDataSetCommand(model, address, dataPayload) {};
    }

}
