package com.adamdbradley.sysex.roland;

import javax.sound.midi.SysexMessage;

import lombok.RequiredArgsConstructor;

/**
 * Immutable container for a Roland Sysex MIDI command
 * (the command code and command-specific payload).
 * Other classes handle adding Roland's checksum byte and wrapping the whole
 * thing in a {@link SysexMessage}.
 */
public interface RolandSysexCommand {

    @RequiredArgsConstructor
    public enum CommandId {
        /**
         * RQ1
         */
        RequestData((byte) 0x11),
        /**
         * DT1
         */
        DataSet((byte) 0x12);

        private final byte id;

        public byte asByte() {
            return id;
        }

    }

    /**
     * @return The Roland command ID.
     */
    CommandId id();

    /**
     * @return The body beginning with the byte after the Command ID
     */
    byte[] bodyWithoutChecksum();

}
