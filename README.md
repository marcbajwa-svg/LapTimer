# LapTimer

Handy-first Laptimer prototype for track sessions, karting, and hobby motorsport.

## Vision

LapTimer starts as a mobile app that can:

- define a start/finish point
- track a session with phone GPS
- detect laps automatically
- show current, last, and best lap
- save sessions for later review

The first milestone is not race-grade timing accuracy. The goal is a solid V1 prototype that proves the core flow on a phone.

## Product Focus

The current product direction is:

- platform: mobile first
- stack: Expo + React Native + TypeScript
- session model: one active track session at a time
- lap detection: GPS-based start/finish crossing with safety rules

## Planned V1

- create a track setup with a start/finish marker
- start and stop a timing session
- show live session timing
- detect laps from GPS updates
- display current lap, last lap, best lap, and lap list
- persist sessions locally on device
- allow manual lap trigger as a fallback

## Project Structure

- `App.tsx`: current prototype entry screen
- `src/components`: reusable UI blocks
- `src/data`: seed data for prototype screens
- `src/theme.ts`: shared colors and spacing
- `native-kotlin/`: fresh native Android rebuild in Kotlin + Jetpack Compose
- `docs/product-brief.md`: product scope and assumptions
- `docs/lap-detection.md`: technical concept for GPS lap detection
- `docs/kotlin-rebuild.md`: migration notes for the native Android rebuild

## Local Development

This repository is prepared for Expo, but the current machine still needs a JavaScript toolchain installed.

Suggested next steps on a dev machine:

1. Install Node.js LTS.
2. Run `npm install`.
3. Run `npx expo start`.

## Android Testing Note

If Expo Go on Android shows a red runtime screen before the app code loads, use a development build instead of Expo Go.

Recommended flow:

1. Install dependencies with `npm install`.
2. Build a development client with EAS.
3. Start the Metro server with `npx expo start --dev-client`.

This is often more stable than Expo Go for projects that hit runtime issues inside the Expo Go environment.

## Notes

- GPS on phones is good enough for a prototype, but not as precise as dedicated motorsport hardware.
- V1 should always keep a manual fallback for lap marking and correction.
