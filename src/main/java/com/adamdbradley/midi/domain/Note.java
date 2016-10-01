package com.adamdbradley.midi.domain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.ImmutableMap;

/**
 * Identifies a MIDI note number (0..127).
 * Nominals conform to the "C3 standard" where middle C is named "C3".
 * We also support parsing the "ASA standard" where middle C is named "C4"
 * through the {@link #ofASA(String)} method.
 * On the wire, middle C is 60 (0x3C).
 */
public class Note extends SingleSevenBitData {

    private static final Pattern C3_PARSER = Pattern.compile("([A-G][#b]?)(-?\\d+)");
    private static final ImmutableMap<String, Integer> NOTE_NAME_VALUES = ImmutableMap
            .<String, Integer>builder()
            .put("C", 0)
            .put("C#", 1).put("Db", 1)
            .put("D", 2)
            .put("D#", 3).put("Eb", 3)
            .put("E", 4)
            .put("F", 5)
            .put("F#", 6).put("Gb", 6)
            .put("G", 7)
            .put("G#", 8).put("Ab", 8)
            .put("A", 9)
            .put("A#", 10).put("Bb", 10)
            .put("B", 11)
            .build();

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
        return parse(midiConventional, -2);
    }

    /**
     * Acoustical Society of America convention.
     * Used for Yamaha devices.
     * @param asaConvention
     * @return
     */
    public static Note ofASA(final String asaConvention) {
        return parse(asaConvention, -1);
    }

    private static Note parse(final String name, final int noteZeroOctave) {
        final Matcher matcher = C3_PARSER.matcher(name);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Unknown note name " + name);
        }
        final String noteName = matcher.group(1);
        final String octaveName = matcher.group(2);
        final int noteValue = NOTE_NAME_VALUES.get(noteName);
        final int octave = Integer.parseInt(octaveName);
        final int midiNoteNumber = ((octave - noteZeroOctave) * 12) + noteValue;
        return of(midiNoteNumber);
    }
}
