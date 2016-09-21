package com.adamdbradley.sysex.roland;

public abstract class RolandDataSetCommand implements Command {

    final byte[] payload;

    public RolandDataSetCommand(final DeviceModel model,
            final int address,
            final byte[] data) {
        payload = build(model, address, data);
    }

    private static byte[] build(final DeviceModel model, final int address,
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

    private static byte[] buildAddressBytes(DeviceModel model, int address) {
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

}
