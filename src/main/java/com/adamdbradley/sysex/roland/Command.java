package com.adamdbradley.sysex.roland;

import lombok.RequiredArgsConstructor;

public interface Command {

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
