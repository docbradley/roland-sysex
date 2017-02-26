package com.adamdbradley.midi.message;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;

import com.adamdbradley.midi.domain.Channel;
import com.adamdbradley.midi.domain.SingleSevenBitData;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Immutable wrapper object for {@link MidiMessage}.
 */
@EqualsAndHashCode(callSuper=true)
public class PitchBendChangeMessage extends ChannelMessage {

    private static final int COMMAND = ShortMessage.PITCH_BEND;

    @Getter
    private final int value;

    public PitchBendChangeMessage(final Channel channel, final int value) {
        super(buildMessage(COMMAND, channel,
                new SingleSevenBitData(false, "PB:LSB", (value + 0x2000) & 0x007F) {},
                new SingleSevenBitData(false, "PB:MSB", (((value + 0x2000) & 0x3F80)) >> 7) {}));
        if (value < -0x2000 || value >= 0x2000) {
            throw new IllegalArgumentException("PB out of bounds: " + Integer.toHexString(value));
        }
        this.value = value;
    }

    protected PitchBendChangeMessage(final ShortMessage message) {
        super(message);
        if (message.getCommand() != COMMAND) {
            throw new IllegalArgumentException();
        }
        value = (message.getData1() | (message.getData2() << 7))
                - 0x2000;
    }

}
