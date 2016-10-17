package com.adamdbradley.midi.sysex.roland.a90;

import com.adamdbradley.midi.domain.ContinuousControlValue;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BankSelect /* twobytemidivalue */ {

    final ContinuousControlValue MSB;
    final ContinuousControlValue LSB;

}
