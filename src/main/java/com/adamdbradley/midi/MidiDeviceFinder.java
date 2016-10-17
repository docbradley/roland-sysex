package com.adamdbradley.midi;

import java.util.function.Supplier;
import java.util.stream.Stream;

import javax.sound.midi.MidiDevice;

public interface MidiDeviceFinder extends Supplier<Stream<MidiDevice>> {

}
