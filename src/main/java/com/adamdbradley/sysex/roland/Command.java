package com.adamdbradley.sysex.roland;

public interface Command {

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

        private CommandId(final byte id) {
            this.id = id;
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
