package com.adamdbradley.midi.sysex.roland.a90;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import com.adamdbradley.midi.domain.BitMask;
import com.adamdbradley.midi.message.Message;
import com.adamdbradley.midi.sysex.roland.InstrumentModel;
import com.adamdbradley.midi.sysex.roland.RolandDataSetCommand;
import com.adamdbradley.midi.sysex.roland.RolandSysexCommand;
import com.adamdbradley.midi.sysex.roland.RolandSysexMessage;
import com.google.common.collect.ImmutableList;

/**
 * Represents a single "Performance" for the A-90 controller.
 */
@Builder
@EqualsAndHashCode(callSuper = false)
@Getter
@RequiredArgsConstructor
@ToString
public class PerformanceModel
extends A90Foundations
implements com.adamdbradley.midi.sysex.roland.UpdatingModel {

    /**
     * 0 = "temporary", 1..64 = "internal"
     */
    private final int patchNumber;

    @NonNull @Nonnull private final String performanceName; // Max Length: 12 ASCII
    private final Optional<Integer> tempoChange; // 20-250
    private final Optional<Integer> songChange; // 0-127
    private final BitMask remoteSwitchExts;
    private final BitMask remoteSwitchInts;
    private final boolean in2toInternal;
    private final BitMask in2toOuts;
    @NonNull @Nonnull private final VERD1FX verd1FX;
    @NonNull @Nonnull private final List<Optional<EffectorSetup>> effectorSetups;
    // ...A whole bunch of per-analog "tempo min/max" stuff...
    @NonNull @Nonnull private final List<InternalZone> internalZones;
    @NonNull @Nonnull private final List<ExternalZone> externalZones;


    @Override
    public List<Message<?>> getMessages() {
        return buildCommands()
                .stream()
                .map(cmd -> new RolandSysexMessage((byte) 0,
                        InstrumentModel.A_90,
                        cmd))
                .collect(Collectors.toList());
    }

    private List<RolandSysexCommand> buildCommands() {
        final ImmutableList.Builder<RolandSysexCommand> builder = ImmutableList.builder();

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(128 + 33)) {
            baos.write(titleBytes(performanceName));
            if (tempoChange.isPresent()) {
                baos.write((byte) 1);
                baos.write((byte) ((tempoChange.get() & 0x00F0) >> 4));
                baos.write((byte) ((tempoChange.get() & 0x000F)));
            } else {
                baos.write(new byte[3]);
            }

            if (songChange.isPresent()) {
                baos.write((byte) 1);
                baos.write(songChange.get().byteValue());
            } else {
                baos.write(new byte[3]);
            }

            remoteSwitchExts.stream(4)
            .<Byte>map(on -> (byte) (on ? 1 : 0))
            .forEachOrdered(b -> baos.write(b.byteValue()));

            remoteSwitchInts.stream(4)
            .<Byte>map(on -> (byte) (on ? 1 : 0))
            .forEachOrdered(b -> baos.write(b.byteValue()));

            baos.write((byte) (in2toInternal ? 1 : 0));

            in2toOuts.stream(4)
            .<Byte>map(on -> (byte) (on ? 1 : 0))
            .forEachOrdered(b -> baos.write(b.byteValue()));

            baos.write(verd1FX.build());

            for (int i=0; i<4; i++) {
                final Optional<EffectorSetup> effector = effectorSetups.get(i);
                if (effector.isPresent()) {
                    baos.write(effector.get().build());
                } else {
                    baos.write(new byte[] { (byte) 0x10,
                            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
                }
            }

            for (int i=0; i<16; i++) {
                baos.write(new byte[4]); // Controller tempo min/max stuff
            }

            if (baos.size() != 142) {
                throw new RuntimeException("Wrong size");
            }

            builder.add(new PatchDataSetCommand(patchNumber, 0x0, baos.toByteArray()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (int i=0; i<4; i++) {
            final ExternalZone zone = externalZones.get(i);
            builder.add(new PatchDataSetCommand(patchNumber,
                    0x0200 + (0x0100 * i),
                    zone.build()));
        }

        for (int i=0; i<4; i++) {
            final InternalZone zone = internalZones.get(i);
            builder.add(new PatchDataSetCommand(patchNumber,
                    0x0600 + (0x0100 * i),
                    zone.build()));
        }

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(256 + 16)) {
            for (ExternalZone zone: externalZones) {
                baos.write(commentBytes(zone.getComment()));
            }
            for (InternalZone zone: internalZones) {
                baos.write(commentBytes(zone.getComment()));
            }

            builder.add(new PatchDataSetCommand(patchNumber,
                    0x0A00,
                    baos.toByteArray()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return builder.build();
    }

    private class PatchDataSetCommand extends RolandDataSetCommand {
        private PatchDataSetCommand(final int patchNumber, final int offset,
                final byte[] barr) {
            super(InstrumentModel.A_90,
                    0x00002000 + (patchNumber * 0x01000000) + offset,
                    barr);
        }
    }

    private static byte[] titleBytes(final String performanceName) {
        return stringBytes(new byte[12], performanceName);
    }

    private static byte[] commentBytes(final String comment) {
        return stringBytes(new byte[34], comment);
    }

}
