package com.adamdbradley.sysex.roland;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.SysexMessage;

import com.adamdbradley.sysex.Message;

public class RolandSysExMessage extends Message<SysexMessage> {

    public RolandSysExMessage(final byte deviceId,
            final InstrumentModel model,
            final Command command) {
        super(build(deviceId, model, command));
    }

    private static SysexMessage build(final byte deviceId,
            final InstrumentModel model,
            final Command command) {
        final byte[] modelId = model.modelIdAsBytes();
        final byte[] body = command.bodyWithoutChecksum();
        final byte[] payload = new byte[5 + modelId.length + body.length + 2];
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
        for (int i = 0; i < body.length; i++) {
            sum += body[i];
        }

        payload[payload.length - 2] = (byte) (-sum & 0x7F);
        payload[payload.length - 1] = (byte) 0xF7; // end-of-sysex byte

        try {
            return new SysexMessage(payload, payload.length);
        } catch (InvalidMidiDataException e) {
            throw new IllegalStateException("Can't build SysEx message", e);
        }
    }

}
