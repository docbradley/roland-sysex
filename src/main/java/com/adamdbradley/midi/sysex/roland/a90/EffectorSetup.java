package com.adamdbradley.midi.sysex.roland.a90;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;

import javax.annotation.Nonnull;

import com.adamdbradley.midi.domain.BitMask;
import com.adamdbradley.midi.domain.Channel;
import com.adamdbradley.midi.domain.Note;
import com.adamdbradley.midi.domain.ProgramChange;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Builder
@EqualsAndHashCode
@Getter
@RequiredArgsConstructor
@ToString
public class EffectorSetup {

    @NonNull @Nonnull private final Channel midiChannel;
    @NonNull @Nonnull private final Optional<BankSelect> bankSelect;
    @NonNull @Nonnull private final Optional<ProgramChange> programChange;
    @NonNull @Nonnull private final Optional<Note[]> localAndSendKey;

    private final BitMask midiOutputs;

    public byte[] build() {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(13)) {
            baos.write(midiChannel.getData());

            if (bankSelect.isPresent()) {
                baos.write((byte) 1);
                baos.write((byte) (bankSelect.get().MSB.getData()));
                baos.write((byte) (bankSelect.get().LSB.getData()));
            } else {
                baos.write(new byte[] { (byte) 0, 0, 0 });
            }

            if (programChange.isPresent()) {
                baos.write((byte) 1);
                baos.write(programChange.get().getData());
            } else {
                baos.write(new byte[] { (byte) 0, 0 });
            }

            if (localAndSendKey.isPresent()) {
                baos.write((byte) 1);
                baos.write(localAndSendKey.get()[0].getData());
                baos.write(localAndSendKey.get()[1].getData());
            } else {
                baos.write(new byte[] { (byte) 0, 0, 0 });
            }

            midiOutputs.streamAsBooleanBytes(4).forEachOrdered(baos::write);

            if (baos.size() != 13) {
                throw new IllegalStateException("Incorrect length");
            }

            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
