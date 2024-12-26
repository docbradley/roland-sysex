package com.adamdbradley.midi.sysex.roland.a90;

import javax.annotation.Nonnull;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper=true)
@Getter
@SuperBuilder
@ToString
public class InternalZone extends ZoneModel {

    @NonNull @Nonnull private final CentBiasControlValue attackTime;
    @NonNull @Nonnull private final CentBiasControlValue decayTime;
    @NonNull @Nonnull private final CentBiasControlValue releaseTime;
    @NonNull @Nonnull private final CentBiasControlValue brightValue;
    @NonNull @Nonnull private final CentBiasControlValue fineTune;

    public byte[] build() {
        byte[] buffer = new byte[95];
        super.build(buffer, 5);
        buffer[0x52] = attackTime.getData();
        buffer[0x53] = decayTime.getData();
        buffer[0x54] = releaseTime.getData();
        buffer[0x55] = brightValue.getData();
        buffer[0x56] = fineTune.getData();
        return buffer;
    }

}
