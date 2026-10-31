# html_tools

Standalone browser tools. Each one is a single self-contained HTML file: open it directly, no build step.

## a80-editor.html

A visual editor for the Roland A-80: all 64 patches (four zones each), the 32 chains, and the system settings (channel names, MIDI receive).

- **Edit:** Drag zone key ranges on an 88-key strip, set curves, controllers, program changes, mutes and outputs.
- **Import:** `.syx`, hex text, or the `.mid` written by `AdamA80Program20230329 file`.
- **Export:** Save `.syx`, or copy the whole library as hex.
- **Send and receive (Web MIDI):** Open the file in Chrome or Edge, click *Connect MIDI*, and pick the interface wired to the A-80's MIDI IN 1. *Send patch*, *Send changed* or *Send all* writes DT1 messages (`F0 41 dev 27 12 aa bb cc … sum F7`), paced 60 ms apart by default. *Request dump* sends the bulk RQ1 and loads whatever comes back. Own exclusive Rx must be on in the A-80.

Patch *n* (1–64) is written as one 142-byte DT1 at `n 00 00`, matching `PatchModel`.

Two bytes disagree between the owner's manual and the Java builder:

- Pitch bend enable: the builder writes 1 = on.
- Aftertouch "off": the builder writes 2, the manual table reads 3.

Both are switchable on the editor's System page. Importing a bulk dump from the A-80 reports which values the unit actually stores.
