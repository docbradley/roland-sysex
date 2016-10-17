package com.adamdbradley.midi;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;

import com.adamdbradley.functional.PredicateAccumulator;

/**
 * Find {@link MidiDevice}s whose names match a provided regex.
 */
public class DeviceFinder {

    public static final Predicate<MidiDevice> MUST_HAVE_INPUT = new MustHaveInputFilter();
    public static final Predicate<MidiDevice> MUST_HAVE_OUTPUT = new MustHaveOutputFilter();
    public static Predicate<MidiDevice> regexFilter(final String regex) {
        return new RegexFilter(regex);
    }

    public static Stream<MidiDevice> find(final Predicate<MidiDevice> predicate) {
        return enumerateDevices()
                .filter(predicate);
    }

    public static Stream<MidiDevice> find(final List<Predicate<MidiDevice>> predicates) {
        return find(predicates.stream().collect(PredicateAccumulator.AND));
    }

    @SafeVarargs // TODO: necessary?
    public static Stream<MidiDevice> find(final Predicate<MidiDevice> ... predicates) {
        return find(Arrays.asList(predicates));
    }

    public static Stream<MidiDevice> enumerateDevices() {
        return Arrays.asList(MidiSystem.getMidiDeviceInfo())
                .stream()
                .map(DeviceFinder::infoToDevice)
                .filter(p -> p.isPresent())
                .map(p -> p.get());
    }

    private static Optional<MidiDevice> infoToDevice(final MidiDevice.Info info) {
        try {
            return Optional.of(MidiSystem.getMidiDevice(info));
        } catch (MidiUnavailableException e) {
            return Optional.empty();
        }
    }

    private static class RegexFilter implements Predicate<MidiDevice> {
        private final Pattern desiredNameRegex;

        public RegexFilter(final String desiredNameRegex) {
            this.desiredNameRegex = Pattern.compile(desiredNameRegex);
        }

        @Override
        public boolean test(final MidiDevice md) {
            return desiredNameRegex.matcher(md.getDeviceInfo().getName()).matches();
        }
    }

    private static class MustHaveInputFilter implements Predicate<MidiDevice> {
        @Override
        public boolean test(final MidiDevice md) {
            return md.getMaxTransmitters() != 0;
        }
    }

    private static class MustHaveOutputFilter implements Predicate<MidiDevice> {
        @Override
        public boolean test(final MidiDevice md) {
            return md.getMaxReceivers() != 0;
        }
    }

}
