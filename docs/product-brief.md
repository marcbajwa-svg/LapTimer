# Product Brief

## Problem

We want a simple lap timing app that works on a phone without extra hardware in the first version.

The main user need is fast feedback during a session:

- Am I still recording?
- What is my current lap?
- What was my last lap?
- What is my best lap today?

## Users

Primary early users:

- kart drivers
- track day hobbyists
- amateur motorcycle riders

These users care about speed, clarity, and low setup friction more than perfect professional timing precision in V1.

## V1 Goals

- get timing started in under one minute
- make the recording state obvious at all times
- detect laps from phone GPS with reasonable consistency
- save session results locally
- keep the live timing screen readable under stress

## Non-Goals For V1

- race-certified timing accuracy
- cloud sync
- multi-user live timing
- advanced analytics
- external hardware integration

## Core Screens

1. Home
2. Track setup
3. Live session
4. Session summary
5. Saved sessions

## Product Risks

1. Phone GPS can drift or update too slowly.
2. Users may not understand whether lap detection is active.
3. False positives near the start line can damage trust quickly.

## Product Response

- show GPS quality and session state clearly
- support a manual lap trigger
- use cooldown and direction checks for safer detection
- save raw location samples later for debugging if needed
