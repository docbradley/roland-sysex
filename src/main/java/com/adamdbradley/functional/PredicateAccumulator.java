package com.adamdbradley.functional;

import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

import javax.sound.midi.MidiDevice;

import com.google.common.collect.ImmutableSet;

/**
 * For use on a {@link Stream} of {@link Predicate}s
 * in the {@link Stream#collect(Collector)} step.
 */
public class PredicateAccumulator
implements Collector<Predicate<MidiDevice>, AtomicReference<Predicate<MidiDevice>>, Predicate<MidiDevice>> {

    public static PredicateAccumulator AND = new PredicateAccumulator(true);
    public static PredicateAccumulator OR = new PredicateAccumulator(false);

    private final boolean and;

    private PredicateAccumulator(final boolean and) {
        this.and = and;
    }

    @Override
    public Set<java.util.stream.Collector.Characteristics> characteristics() {
        return ImmutableSet.of(Collector.Characteristics.UNORDERED);
    }

    @Override
    public Supplier<AtomicReference<Predicate<MidiDevice>>> supplier() {
        return () -> { return new AtomicReference<Predicate<MidiDevice>>(); };
    }

    @Override
    public BiConsumer<AtomicReference<Predicate<MidiDevice>>, Predicate<MidiDevice>> accumulator() {
        return (existing, newPredicate) -> {
            if (existing.get() == null) {
                existing.set(newPredicate);
            } else {
                final Predicate<MidiDevice> old = existing.get();
                existing.set(new Predicate<MidiDevice>() {
                    @Override
                    public boolean test(final MidiDevice t) {
                        if (and) {
                            return old.test(t) && newPredicate.test(t);
                        } else {
                            return old.test(t) || newPredicate.test(t);
                        }
                    }
                });
            }
        };
    }

    @Override
    public BinaryOperator<AtomicReference<Predicate<MidiDevice>>> combiner() {
        return (r1, r2) -> {
            if (r1.get() == null) {
                return r2;
            } else if (r2.get() == null) {
                return r1;
            } else {
                final Predicate<MidiDevice> p1 = r1.get();
                final Predicate<MidiDevice> p2 = r2.get();
                if (and) {
                    return new AtomicReference<>(md -> { return p1.test(md) && p2.test(md); });
                } else {
                    return new AtomicReference<>(md -> { return p1.test(md) || p2.test(md); });
                }
            }
        };
    }

    @Override
    public Function<AtomicReference<Predicate<MidiDevice>>, Predicate<MidiDevice>> finisher() {
        return (r) -> {
            if (r.get() == null) {
                return (md) -> true;
            } else {
                return r.get();
            }
        };
    }

}