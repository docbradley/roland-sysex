package com.adamdbradley.midi.sysex.roland.a90;

import com.google.common.base.Charsets;

abstract class A90Foundations {

    static byte[] stringBytes(final byte[] buffer, final String string) {
        for (int i=0; i<buffer.length; i++) {
            buffer[i] = 0x20;
        }
        final byte[] stringBytes = string.getBytes(Charsets.US_ASCII);
        if (stringBytes.length > buffer.length) {
            throw new IllegalStateException("Can't fit [" + string + "] into " + buffer.length);
        }
        System.arraycopy(stringBytes, 0, buffer, 0, stringBytes.length);
        return buffer;
    }

}
