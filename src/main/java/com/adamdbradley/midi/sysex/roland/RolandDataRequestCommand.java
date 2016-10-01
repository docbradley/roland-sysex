package com.adamdbradley.midi.sysex.roland;

/**
 * Immutable wrapper for a Roland Sysex request for a data dump.
 */
public class RolandDataRequestCommand implements RolandSysexCommand {

    final byte[] payload;

    /**
     * @param model
     * @param address
     * @param length
     */
    protected RolandDataRequestCommand(final InstrumentModel model,
            final int address,
            final int length) {
        payload = build(model, address, length);
    }

    /**
     * 
     * @param model
     * @param address
     * @param length
     * @return Still excluding the checksum
     */
    private static byte[] build(final InstrumentModel model, final int address,
            final int length) {
        if (length > 0x0FFFFFFF || length < 0) {
            throw new IllegalArgumentException("Too long");
        }
        final byte[] addressBytes = buildAddressBytes(model, address);
        final byte[] lengthBytes = buildAddressBytes(model, length);

        final byte[] payload = new byte[model.addressLength() * 2];
        System.arraycopy(addressBytes, 0, payload, 0, model.addressLength());
        System.arraycopy(lengthBytes, 0, payload, model.addressLength(), lengthBytes.length);
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

    public static RolandDataRequestCommand parse(final InstrumentModel model,
            final byte deviceId,
            final byte[] payload) {
        final byte[] addressBytes = new byte[4];
        final byte[] lengthBytes = new byte[4];
        switch (model.addressLength()) {
        case 3:
            System.arraycopy(payload, 1, addressBytes, 1, 3);
            System.arraycopy(payload, 5, lengthBytes, 1, 3);
            break;
        case 4:
            System.arraycopy(payload, 0, addressBytes, 0, 4);
            System.arraycopy(payload, 4, lengthBytes, 0, 4);
            break;
        default:
            throw new IllegalArgumentException("Length not understood");
        }
        final int address = (addressBytes[0] * 0x010000)
                + (addressBytes[1] * 0x00010000)
                + (addressBytes[2] * 0x00000100)
                + (addressBytes[3]);
        final int length = (lengthBytes[0] * 0x010000)
                + (lengthBytes[1] * 0x00010000)
                + (lengthBytes[2] * 0x00000100)
                + (lengthBytes[3]);

        return new RolandDataRequestCommand(model, address, length);
    }

}
