package com.adamdbradley.sysex;

import lombok.RequiredArgsConstructor;

/**
 * Identifies a MIDI Continuous Controller (0-127).
 * Nominal and wire values are identical.
 */
public class ContinuousControllerId extends SingleSevenBitData {

    @RequiredArgsConstructor
    public enum StandardControllers {
        BankSelect(0),
        ModulationWheel(1),
        BreathController(2),
        FootPedal(4),
        PortamentoTime(5),
        DataEntry(6),
        Volume(7),
        Balanace(8),
        PanPosition(10),
        Expression(11),
        EffectControl1(12),
        EffectControl2(13),
        BankSelectLSB(32),
        ModulationWheelLSB(33),
        BreathControllerLSB(34),
        FootPedalLSB(36),
        PortamentoTimeLSB(37),
        DataEntryLSB(38),
        VolumeLSB(39),
        BalanceLSB(40),
        PanPositionLSB(42),
        ExpressionLSB(43),
        EffectControl1LSB(44),
        EffectControl2LSB(45),
        HoldPedal(64),
        Portamento(65),
        SustenutoPedal(66),
        SoftPedal(67),
        LegatoPedal(68),
        Hold2Pedal(69),
        SoundVariation(70),
        Resonance(71),
        SoundReleaseTime(72),
        SoundAttackTime(73),
        FrequencyCutoff(74),
        SoundControl6(75),
        SoundControl7(76),
        SoundControl8(77),
        SoundControl9(78),
        SoundControl10(79),
        ReverbLevel(91),
        TremoloLevel(92),
        ChorusLevel(93),
        PhaserLevel(95),
        AllSoundOff(120),
        AllControllersOff(121),
        AllNotesOff(123),
        OmniModeOff(124),
        OmniModeOn(125),
        MonoOperation(126),
        PolyOperation(127);

        private final int number;

        public ContinuousControllerId get() {
            return of(number);
        }
    }

    private ContinuousControllerId(final int data) {
        super("CC" + data, data);
    }

    private static final ContinuousControllerId controllerIds[];
    static {
        controllerIds = new ContinuousControllerId[128];
        for (int i=0; i<128; i++) {
            controllerIds[i] = new ContinuousControllerId(i);
        }
    }

    /**
     * @param ccid 0..127
     * @return
     */
    public static ContinuousControllerId of(final int ccid) {
        return controllerIds[ccid];
    }

}
