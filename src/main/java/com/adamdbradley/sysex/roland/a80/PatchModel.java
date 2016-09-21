package com.adamdbradley.sysex.roland.a80;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import com.adamdbradley.sysex.roland.DeviceModel;
import com.adamdbradley.sysex.roland.RolandDataSetCommand;
import com.adamdbradley.sysex.roland.RolandSysExMessage;

/**
 * 142 bytes (7-bit 01 0E)
 */
@Builder
@EqualsAndHashCode
@Getter
@RequiredArgsConstructor
@ToString
public class PatchModel implements com.adamdbradley.sysex.roland.PatchModel {

    final PatchNumber patchNumber;

    // Length: 16 ASCII
    private final String patchName;

    private final BitSet midiOutputUnmuted;

    // Four zones
    private final List<ZoneModel> zones;

    // Four additional program change commands
    private final List<AdditionalProgramChangeModel> effectors;

    @Override
    public List<RolandSysExMessage> getDataSetMessages() {
        return Collections.singletonList(
                new RolandSysExMessage((byte) 0x0, DeviceModel.A_80,
                        new PatchDataSetCommand(patchNumber)));
    }

    private class PatchDataSetCommand extends RolandDataSetCommand {
        private PatchDataSetCommand(PatchNumber patchNumber) {
            super(DeviceModel.A_80,
                    (patchNumber.getData() + 1) * 0x00010000,
                    build());
        }
    }

    private byte[] build() {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(142)) {
            baos.write(titleBytes());
            baos.write((byte) this.midiOutputUnmuted.toByteArray()[0]);
            baos.write((byte) ((this.zones.get(0).unmuted ? 1 : 0)
                    | (this.zones.get(1).unmuted ? 2 : 0)
                    | (this.zones.get(2).unmuted ? 4 : 0)
                    | (this.zones.get(3).unmuted ? 8 : 0)));
            baos.write(this.zones.get(0).build());
            baos.write(this.zones.get(1).build());
            baos.write(this.zones.get(2).build());
            baos.write(this.zones.get(3).build());
            baos.write((byte) this.effectors.get(0).channel.getData());
            baos.write((byte) this.effectors.get(1).channel.getData());
            baos.write((byte) this.effectors.get(2).channel.getData());
            baos.write((byte) this.effectors.get(3).channel.getData());
            baos.write((byte) this.effectors.get(0).programChange.getData());
            baos.write((byte) this.effectors.get(1).programChange.getData());
            baos.write((byte) this.effectors.get(2).programChange.getData());
            baos.write((byte) this.effectors.get(3).programChange.getData());
            if (baos.size() != 142) {
                throw new RuntimeException("Wrong size");
            }
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] titleBytes() {
        final byte[] buffer = new byte[] { 0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x20,
                0x20, 0x20, 0x20, 0x20 };
        final byte[] titleBytes = this.patchName.getBytes(Charset.forName("ASCII"));
        if (titleBytes.length > 16) {
            throw new IllegalStateException("Too long patch title: " + patchName);
        }
        System.arraycopy(titleBytes, 0, buffer, 0, titleBytes.length);
        return buffer;
    }

}
