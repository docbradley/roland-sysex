package com.adamdbradley.midi.sysex.roland.a90;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import com.adamdbradley.midi.Message;
import com.adamdbradley.midi.domain.ProgramChange;
import com.adamdbradley.midi.sysex.roland.InstrumentModel;
import com.adamdbradley.midi.sysex.roland.RolandDataSetCommand;
import com.adamdbradley.midi.sysex.roland.RolandSysexCommand;
import com.adamdbradley.midi.sysex.roland.RolandSysexMessage;
import com.adamdbradley.midi.sysex.roland.UpdatingModel;
import com.google.common.collect.ImmutableList;

/**
 * Represents a single "Chain" for the A-90 controller.
 */
@Builder
@EqualsAndHashCode
@Getter
@RequiredArgsConstructor
@ToString
public class ChainModel implements UpdatingModel {

    public enum ChainMode {
        OneWay,
        Loop
    }

    private final int chainNumber; // 0..9, a.k.a. 01-10
    @NonNull @Nonnull private final ChainMode chainMode;
    @NonNull @Nonnull private final List<ProgramChange> patchNumbers; // max length 64

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
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(66)) {
            baos.write(chainMode == ChainMode.OneWay ? ((byte) 0) : ((byte) 1));
            if (patchNumbers.size() > 64) {
                throw new IllegalStateException("Chain can't be longer than 64");
            }
            baos.write((byte) patchNumbers.size());
            for (int i=0; i<64; i++) {
                if (patchNumbers.size() > i) {
                    baos.write(patchNumbers.get(i).getData());
                } else {
                    baos.write((byte) 0);
                }
            }

            if (baos.size() != 66) {
                throw new IllegalStateException("Unexpected message size");
            }

            return ImmutableList.of(new ChainDataSetCommand(chainNumber,
                    baos.toByteArray()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private class ChainDataSetCommand extends RolandDataSetCommand {
        private ChainDataSetCommand(final int patchNumber, final byte[] barr) {
            super(InstrumentModel.A_90,
                    0x01403000 + (patchNumber * 0x00010000),
                    barr);
        }
    }

}
