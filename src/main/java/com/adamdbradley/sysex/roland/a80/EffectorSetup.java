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
 * Represents a {@link PatchModel} attribute allowing the controller to send
 * additional Program Change messages whenever the patch is selected.
 * Usually wrapped in an {@link Optional}; only instantiate it if you need it.
 */
@Builder
@EqualsAndHashCode
@Getter
@RequiredArgsConstructor
@ToString
public class EffectorSetup {

    @NonNull @Nonnull final Channel channel;
    @NonNull @Nonnull final ProgramChange programChange;

}
