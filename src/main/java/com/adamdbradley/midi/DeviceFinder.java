package com.adamdbradley.midi;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import javax.sound.midi.MidiDevice;

import com.adamdbradley.functional.PredicateAccumulator;
import com.google.common.annotations.VisibleForTesting;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * Find {@link MidiDevice}s matching particular criteria.
 */
@VisibleForTesting
@RequiredArgsConstructor(access=AccessLevel.PACKAGE)
public class DeviceFinder {

    private final Supplier<Stream<MidiDevice>> supplier;

    public DeviceFinder() {
        this(MidiUtils::enumerateDevices);
    }

    public static final Predicate<MidiDevice> TRUE = (x) -> true;
    public static final Predicate<MidiDevice> FALSE = (x) -> false;
    public static final Predicate<MidiDevice> MUST_HAVE_INPUT = new MustHaveInputFilter();
    public static final Predicate<MidiDevice> MUST_HAVE_OUTPUT = new MustHaveOutputFilter();

    public static Predicate<MidiDevice> regexFilter(final String regex) {
        return new RegexFilter(regex);
    }

    public static Predicate<MidiDevice> nameMatchFilter(final String name) {
        return new NameMatchFilter(name);
    }

    public Stream<MidiDevice> find(final Predicate<MidiDevice> predicate) {
        return supplier.get().filter(predicate);
    }

    public Stream<MidiDevice> find(final List<Predicate<MidiDevice>> predicates) {
        return find(predicates.stream().collect(PredicateAccumulator.and()));
    }

    @SafeVarargs
    public final Stream<MidiDevice> find(final Predicate<MidiDevice> ... predicates) {
        return find(Arrays.asList(predicates));
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

    @RequiredArgsConstructor
    private static class NameMatchFilter implements Predicate<MidiDevice> {
        private final String desiredName;

        @Override
        public boolean test(final MidiDevice md) {
            return desiredName.equals(md.getDeviceInfo().getName());
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
