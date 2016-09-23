package com.adamdbradley.sysex;

/**
 * Identifies a MIDI note number (0..127).
 * Nominals conform to the "C3 standard" where middle C is named "C3".
 * We also support parsing the "ASA standard" where middle C is named "C4"
 * through the {@link #ofASA(String)} method.
 * On the wire, middle C is 60 (0x3C).
 */
public class Note extends SingleSevenBitData {

    private static String noteName[] = {
            "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"
    };

    private Note(final int data) {
        super(noteName[data % 12] + ((data / 12) - 2), data);
    }

    private static final Note[] noteValues;
    static {
        noteValues = new Note[128];
        for (int i=0; i<128; i++) {
            noteValues[i] = new Note(i);
        }
    };

    /**
     * @param value 0..127.  60 (0x3C) is middle C.
     * @return
     */
    public static Note of(final int value) {
        return noteValues[value];
    }

    /**
     * Normal MIDI instrument convention.
     * @param midiConventional
     * @return
     */
    public static Note of(final String midiConventional) {
        throw new RuntimeException("not implemented yet"); // TODO
    }

    /**
     * Acoustical Society of America convention.
     * Used for Yamaha devices.
     * @param asaConvention
     * @return
     */
    public static Note ofASA(final String asaConvention) {
        throw new RuntimeException("not implemented yet"); // TODO
    }

}
