package com.adamdbradley.midi.sysex.roland.a90;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.annotation.Nonnull;

import com.adamdbradley.midi.domain.ContinuousControlValue;
import com.adamdbradley.midi.domain.Volume;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Builder
@EqualsAndHashCode
@Getter
@RequiredArgsConstructor
@ToString
public class VERD1FX {

    @RequiredArgsConstructor
    public enum ReverbType {
        Room1(0), Room2(1),
        Stage1(2), Stage2(3),
        Hall1(4), Hall2(5),
        Delay(6), PanDelay(7);
        private final int value;
        private byte getData() {
            return (byte) value;
        }
    }

    @RequiredArgsConstructor
    public enum Frequency {
        F200(0), F250(1), F315(2), F400(3),
        F500(4), F630(5), F800(6), F1000(7),
        F1250(8), F1600(9), F2000(10), F2500(11),
        F3150(12), F4000(13), F5000(14), F6300(15),
        D8000(16), BYPASS(17);
        private final int value;
        private byte getData() {
            return (byte) value;
        }
    }

    @RequiredArgsConstructor
    public enum ChorusOutput {
        Mix(0),
        Rev(1),
        MixAndRev(2);
        private final int value;
        private byte getData() {
            return (byte) value;
        }
    }

    @NonNull @Nonnull private final ReverbType reverbType;
    @NonNull @Nonnull private final Volume reverbLevel;
    @NonNull @Nonnull private final ContinuousControlValue reverbTime;
    @NonNull @Nonnull private final Frequency reverbHighFrequencyDamp;
    @NonNull @Nonnull private final Volume reverbFeedback;

    @NonNull @Nonnull private final Volume chorusLevel;
    @NonNull @Nonnull private final ContinuousControlValue chorusRate;
    @NonNull @Nonnull private final ContinuousControlValue chorusDepth;
    @NonNull @Nonnull private final ContinuousControlValue chorusPreDelay;
    @NonNull @Nonnull private final Volume chorusFeedback;
    @NonNull @Nonnull private final ChorusOutput chorusOutput;

    @NonNull @Nonnull private final EQBiasControlValue eqLowGain;
    @NonNull @Nonnull private final EQBiasControlValue eqMidGain;
    @NonNull @Nonnull private final EQBiasControlValue eqHighGain;
    @NonNull @Nonnull private final Frequency eqMidFrequency;

    public byte[] build() {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(15)) {
            baos.write(reverbType.getData());
            baos.write(reverbLevel.getData());
            baos.write(reverbTime.getData());
            baos.write(reverbHighFrequencyDamp.getData());
            baos.write(reverbFeedback.getData());
            baos.write(chorusLevel.getData());
            baos.write(chorusRate.getData());
            baos.write(chorusDepth.getData());
            baos.write(chorusPreDelay.getData());
            baos.write(chorusFeedback.getData());
            baos.write(chorusOutput.getData());
            baos.write(eqLowGain.getData());
            baos.write(eqMidGain.getData());
            baos.write(eqHighGain.getData());
            baos.write(eqMidFrequency.getData());

            if (baos.size() != 15) {
                throw new RuntimeException("Wrong size");
            }

            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
