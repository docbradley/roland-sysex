package com.adamdbradley.sysex.roland.a80;

import com.adamdbradley.sysex.Channel;
import com.adamdbradley.sysex.ProgramChange;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Builder
@EqualsAndHashCode
@Getter
@RequiredArgsConstructor
@ToString
public class AdditionalProgramChangeModel {

    final Channel channel;
    final ProgramChange programChange;

}
