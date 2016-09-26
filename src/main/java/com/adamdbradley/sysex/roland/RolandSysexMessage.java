package com.adamdbradley.sysex.roland;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.SysexMessage;

import com.adamdbradley.sysex.Message;

/**
 * Used to build a {@link SysexMessage} from a {@link RolandSysexCommand};
 * wraps the latter with the Sysex start byte, Roland's Sysex prefix,
 * the checksum byte, and the Sysex stop byte.
 */
public class RolandSysexMessage extends Message<SysexMessage> {

    public RolandSysexMessage(final byte deviceId,
            final InstrumentModel model,
            final RolandSysexCommand command) {
        super(build(deviceId, model, command));
    }

    private static SysexMessage build(final byte deviceId,
            final InstrumentModel model,
            final RolandSysexCommand command) {
        final byte[] modelId = model.modelIdAsBytes();
        final byte[] body = command.bodyWithoutChecksum();
        final byte[] payload = new byte[5 + modelId.length + body.length + 2];
        payload[0] = (byte) 0xF0;
        payload[1] = (byte) 0x41;
        payload[2] = deviceId;
        System.arraycopy(modelId, 0, payload, 3, modelId.length);
        payload[3 + modelId.length] = command.id().asByte();
        System.arraycopy(body, 0, payload, 4 + modelId.length, body.length);

        byte sum = 0b0;
        for (int i = 0; i < body.length; i++) {
            sum += body[i];
        }

        payload[payload.length - 2] = (byte) (-sum & 0x7F); // checksum

        payload[payload.length - 1] = (byte) 0xF7; // end-of-sysex byte

        try {
            return new SysexMessage(payload, payload.length);
        } catch (InvalidMidiDataException e) {
            throw new IllegalStateException("Can't build SysEx message", e);
        }
    }

    public static RolandSysexCommand parseMessageToCommand(final SysexMessage sysexMessage) {
        final byte[] totalMessage = sysexMessage.getMessage();
        if (totalMessage[0] != 0xF0) { // "Sysex"
            throw new IllegalArgumentException("Unrecognized initial byte");
        }
        if (totalMessage[1] != 0x41) { // "Roland"
            throw new IllegalArgumentException("Unrecognized vendor byte");
        }

        final byte deviceId = totalMessage[2];

        final InstrumentModel instrument = identifyInstrument(totalMessage[3]);

        // Strip off SOX, Mfgr, ModelId, DevId, Command, //, Checksum, EOX
        final byte[] payload = new byte[totalMessage.length - 7];
        System.arraycopy(sysexMessage.getData(), 5, payload, 0, payload.length);

        byte sum = 0x0;
        for (int i=0; i<payload.length; i++) {
            sum += payload[i];
        }
        final byte checksum = (byte) (-sum & 0x7F);
        if (checksum != sysexMessage.getData()[sysexMessage.getData().length - 2]) {
            throw new IllegalArgumentException("Checksum didn't agree");
        }

        final byte commandId = totalMessage[4];
        switch (commandId) {
        case 0x11: // RQ1
            return RolandDataRequestCommand.parse(instrument, deviceId, payload);
        case 0x12: // DT1
            return RolandDataSetCommand.parse(instrument, deviceId, payload);
        default:
            throw new IllegalArgumentException("???");
        }
    }

    private static InstrumentModel identifyInstrument(final byte b) {
        switch (b) {
        case 0x27:
            return InstrumentModel.A_80;
        case 0x7D:
            return InstrumentModel.A_90;
        default:
            throw new IllegalArgumentException("Unrecognized model byte");
        }
    }

}
