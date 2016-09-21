package com.adamdbradley.sysex;

public class Note extends SingleSevenBitData {

    private Note(final int data) {
        super(data);
    }

    private static final Note[] noteValues;
    static {
        noteValues = new Note[128];
        for (int i=0; i<128; i++) {
            noteValues[i] = new Note(i);
        }
    };

    /**
     * 
     * @param value 0..127
     * @return
     */
    public static Note of(final int value) {
        return noteValues[value];
    }

}
