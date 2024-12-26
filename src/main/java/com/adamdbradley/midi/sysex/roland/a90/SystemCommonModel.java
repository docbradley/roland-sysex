package com.adamdbradley.midi.sysex.roland.a90;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import com.adamdbradley.midi.domain.BitMask;
import com.adamdbradley.midi.domain.Channel;
import com.adamdbradley.midi.domain.ProgramChange;
import com.adamdbradley.midi.message.Message;
import com.adamdbradley.midi.sysex.roland.InstrumentModel;
import com.adamdbradley.midi.sysex.roland.RolandSysexCommand;
import com.adamdbradley.midi.sysex.roland.RolandSysexMessage;
import com.google.common.collect.ImmutableList;

/**
 * Represents the entire "System Common" parameter block for the A-90 controller.
 */
@Builder
@EqualsAndHashCode(callSuper = false)
@Getter
@RequiredArgsConstructor
@ToString
public class SystemCommonModel
extends A90Foundations
implements com.adamdbradley.midi.sysex.roland.UpdatingModel {

    @Builder.Default @Nonnull private final PanelMode panelMode = PanelMode.PERFORMANCE;
    @Builder.Default @Nonnull private final ProgramChange performanceNumber = ProgramChange.of(65);
    @Builder.Default @Nonnull private final ProgramChange chainNumber = ProgramChange.of(1);
    @Builder.Default @Nonnull private final boolean controlChannelSwitch = true;
    @Builder.Default @Nonnull private final Channel controlChannel = Channel.of(16);
    @Builder.Default @Nonnull private final BitMask midiOutEnables = BitMask.FOUR_ON;
    @Builder.Default @Nonnull private final BitMask midiOutSequencerEnables = BitMask.FOUR_ON;
    @Builder.Default @Nonnull private final BiasControlValue globalKeyTranspose = BiasControlValue.ZERO;
    @Builder.Default @Nonnull private final boolean vExpEnable = true;

    public enum PanelMode {
        PERFORMANCE,
        MANUAL,
        CHAIN
    }

    @Override
    public List<Message<?>> getMessages() {
        return buildCommands()
                .stream()
                .map(cmd -> new RolandSysexMessage((byte) 0,
                        InstrumentModel.A_90,
                        cmd))
                .collect(Collectors.toList());
    }

    private List<RolandSysexCommand> buildCommands() {
        final ImmutableList.Builder<RolandSysexCommand> builder = ImmutableList.builder();

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(56)) {
            
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return builder.build();
    }

}
