package com.adamdbradley.sysex.roland.a80;

import java.util.Optional;

import javax.annotation.Nonnull;

import com.adamdbradley.sysex.Channel;
import com.adamdbradley.sysex.ProgramChange;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * Generally wrapped in an {@link Optional};
 * only instantiate it if you mean it.
 */
@Builder
@EqualsAndHashCode
@Getter
@RequiredArgsConstructor
@ToString
public class AdditionalProgramChangeModel {

    @NonNull @Nonnull final Channel channel;
    @NonNull @Nonnull final ProgramChange programChange;

}
