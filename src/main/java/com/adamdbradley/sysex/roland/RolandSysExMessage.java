package com.adamdbradley.sysex.roland;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.SysexMessage;

import com.adamdbradley.sysex.Message;

public class RolandSysExMessage extends Message<SysexMessage> {

    public RolandSysExMessage(final byte deviceId,
            final DeviceModel model,
            final Command command) {
        super(build(deviceId, model, command));
    }

    private static SysexMessage build(final byte deviceId,
            final DeviceModel model,
            final Command command) {
        final byte[] modelId = model.modelIdAsBytes();
        final byte[] body = command.bodyWithoutChecksum();
        final byte[] payload = new byte[5 + modelId.length + body.length];
        payload[0] = (byte) 0xF0;
        payload[1] = (byte) 0x41;
        payload[2] = deviceId;
        System.arraycopy(modelId, 0, payload, 3, modelId.length);
        payload[3 + modelId.length] = command.id().asByte();
        System.arraycopy(body, 0, payload, 4 + modelId.length, body.length);

        // Roland has two checksum standards:
        // one that inspects the bytes of the body,
        // and one that only inspects the length of the body.
        byte sum = 0b0;
        if (model.checksumBody()) {
            for (int i = 0; i < body.length; i++) {
                sum += body[i];
            }
        } else {
            for (int i = 0; i < model.addressLength(); i++) {
                sum += body[i];
            }
            sum += body.length - model.addressLength();
        }

        payload[payload.length - 1] = (byte) (~sum & 0x7F);

        try {
            return new SysexMessage(payload, payload.length);
        } catch (InvalidMidiDataException e) {
            throw new IllegalStateException("Can't build SysEx message", e);
        }
    }

}
