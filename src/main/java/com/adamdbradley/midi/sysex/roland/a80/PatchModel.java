package com.adamdbradley.midi.sysex.roland.a80;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.BitSet;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import com.adamdbradley.midi.message.Message;
import com.adamdbradley.midi.sysex.roland.InstrumentModel;
import com.adamdbradley.midi.sysex.roland.RolandDataSetCommand;
import com.adamdbradley.midi.sysex.roland.RolandSysexMessage;
import com.google.common.collect.ImmutableList;

/**
 * Represents a single "patch" for the A-80 controller.
 */
@Builder
@EqualsAndHashCode
@Getter
@RequiredArgsConstructor
@ToString
public class PatchModel implements com.adamdbradley.midi.sysex.roland.UpdatingModel {

    @NonNull @Nonnull final PatchNumber patchNumber;
    @NonNull @Nonnull private final String patchName; // Max Length: 16 ASCII
    @NonNull @Nonnull private final BitSet midiOutputUnmuted;
    @NonNull @Nonnull private final List<ZoneModel> zones; // 4 zones
    @NonNull @Nonnull private final List<Optional<EffectorSetup>> effectors; // 4 adt'l PCs

    // Specifies default values for the Builder
    public static class PatchModelBuilder {
        private String patchName = "DEFAULT";
        private BitSet midiOutputUnmuted = BitSet.valueOf(new byte[] { 0x0F });
        private List<Optional<EffectorSetup>> effectors = ImmutableList.of(
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty());
    }

    @Override
    public List<Message<?>> getMessages() {
        return ImmutableList.of(
                new RolandSysexMessage((byte) 0x0, InstrumentModel.A_80,
                        new PatchDataSetCommand(patchNumber)));
    }

    private class PatchDataSetCommand extends RolandDataSetCommand {
        private PatchDataSetCommand(PatchNumber patchNumber) {
            super(InstrumentModel.A_80,
                    (patchNumber.getData() + 1) * 0x00010000,
                    build());
        }
    }

    /**
     * Excluding the checksum byte
     * @return
     */
    private byte[] build() {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(142)) {
            baos.write(titleBytes());
            baos.write((byte) midiOutputUnmuted.toByteArray()[0]);
            baos.write((byte) ((zones.get(0).isUnmuted() ? 1 : 0)
                    | (zones.get(1).isUnmuted() ? 2 : 0)
                    | (zones.get(2).isUnmuted() ? 4 : 0)
                    | (zones.get(3).isUnmuted() ? 8 : 0)));

            if (zones.size() != 4) {
                throw new IllegalStateException("Must have 4 zones: " + zones);
            }
            for (ZoneModel zone: zones) {
                baos.write(zone.build());
            }

            if (effectors.size() != 4) {
                throw new IllegalStateException("Must have 4 optional-effectors: " + effectors);
            }
            for (Optional<EffectorSetup> effector: effectors) {
                if (effector.isPresent()) {
                    baos.write((byte) effector.get().channel.getData());
                } else {
                    baos.write((byte) 0x10);
                }
            }
            for (Optional<EffectorSetup> effector: effectors) {
                if (effector.isPresent()) {
                    baos.write((byte) effector.get().programChange.getData());
                } else {
                    baos.write((byte) 0x00);
                }
            }

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
        final byte[] titleBytes = patchName.getBytes(Charset.forName("ASCII"));
        if (titleBytes.length > 16) {
            throw new IllegalStateException("Too long patch title: " + patchName);
        }
        System.arraycopy(titleBytes, 0, buffer, 0, titleBytes.length);
        return buffer;
    }

}
