package com.adamdbradley.midi.sysex.roland.a90;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper=true)
@Getter
@SuperBuilder
@ToString
public class ExternalZone extends ZoneModel {

    public byte[] build() {
        byte[] buffer = new byte[90];
        super.build(buffer, 0);
        return buffer;
    }

}
