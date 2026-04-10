# Lap Detection Concept

## Goal

Detect a lap from phone GPS without extra hardware in the first prototype.

## Setup

The user defines a start/finish line from two map points, or from one point plus a generated perpendicular line.

Each GPS update provides:

- latitude
- longitude
- timestamp
- speed if available
- heading if available
- accuracy radius if available

## Detection Strategy

Each new GPS point is compared to the previous one.

A lap is counted only when all rules pass:

1. The movement segment crosses the start/finish line.
2. The crossing direction matches the configured direction.
3. The elapsed time since the last counted lap is above a minimum threshold.
4. GPS accuracy is acceptable for the crossing event.

## Safety Rules

- Ignore crossings within a cooldown window.
- Ignore near-stationary crossings in the pit or setup area.
- Ignore low-confidence samples with poor accuracy.
- Keep a manual lap trigger available.

## V1 Confidence Model

Each possible lap event can be tagged internally as:

- high confidence
- medium confidence
- low confidence

Low-confidence events should not be auto-counted in early versions unless testing shows acceptable behavior.

## Future Improvements

- Kalman smoothing or another basic filter for noisy GPS paths
- map snapping for known tracks
- external Bluetooth GPS receivers
- per-track calibration rules
- ghost lap and sector timing
