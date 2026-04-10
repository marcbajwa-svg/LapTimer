import { CurrentLocation, Lap, LiveSessionState, Locale, SessionPreview, TrackDefinition } from "../types";
import { copy } from "../i18n";
import { formatDuration, formatSessionClock, formatSignedDuration } from "./time";
import { isWithinSplitRadius } from "./location";

export function createInitialLiveState(seed: SessionPreview): LiveSessionState {
  return {
    status: "idle",
    sessionTimeMs: 0,
    currentLapTimeMs: 0,
    currentDeltaMs: null,
    bestLapTimeMs: parseLap(seed.bestLap),
    lastLapTimeMs: null,
    bestLapSplitTimesMs: null,
    currentLapSplitTimesMs: [],
    nextSplitIndex: 0,
    lapCount: 0,
    laps: [],
  };
}

export function createLapFromCurrent(state: LiveSessionState, locale: Locale): Lap | null {
  if (state.currentLapTimeMs < 10_000) {
    return null;
  }

  const previousBest = state.bestLapTimeMs;
  const nextLapTime = state.currentLapTimeMs;
  const confidence = nextLapTime < 75_000 ? "high" : nextLapTime < 90_000 ? "medium" : "low";
  const isBest = previousBest === null || nextLapTime < previousBest;

  let delta = copy[locale].common.bestTag;
  if (!isBest && previousBest !== null) {
    const difference = nextLapTime - previousBest;
    delta = `+${formatDuration(difference)}`;
  }

  return {
    number: state.lapCount + 1,
    time: formatDuration(nextLapTime),
    delta,
    confidence,
  };
}

export function buildSessionPreview(
  seed: SessionPreview,
  state: LiveSessionState,
  locale: Locale,
): SessionPreview {
  const text = copy[locale];
  const bestLapTimeMs = state.bestLapTimeMs ?? parseLap(seed.bestLap);

  const summaryNotes =
    state.lapCount === 0
      ? text.summary.emptyNotes
      : [
          `${text.summary.noteLapsRecorded} ${state.lapCount}.`,
          `${text.summary.noteBestLap} ${state.bestLapTimeMs ? formatDuration(state.bestLapTimeMs) : seed.bestLap}.`,
          state.status === "finished" ? text.summary.noteFinished : text.summary.noteReady,
        ];

  return {
    ...seed,
    currentLap: formatDuration(state.currentLapTimeMs || 0),
    currentDelta: buildCurrentDelta(state, seed.currentDelta),
    lastLap: state.lastLapTimeMs ? formatDuration(state.lastLapTimeMs) : seed.lastLap,
    bestLap: bestLapTimeMs ? formatDuration(bestLapTimeMs) : seed.bestLap,
    sessionTime: formatSessionClock(state.sessionTimeMs),
    totalLaps: state.lapCount,
    summaryNotes,
    laps: state.laps.length > 0 ? state.laps : seed.laps,
    gpsStatus: statusCopy(state.status, locale, seed.gpsStatus),
  };
}

function buildCurrentDelta(state: LiveSessionState, fallback: string): string {
  if (state.currentDeltaMs !== null) {
    return formatSignedDuration(state.currentDeltaMs);
  }

  if (state.bestLapTimeMs === null) {
    return fallback;
  }

  return "--";
}

function statusCopy(status: LiveSessionState["status"], locale: Locale, idleFallback: string): string {
  const text = copy[locale];

  switch (status) {
    case "running":
      return text.live.statusRunning;
    case "paused":
      return text.live.statusPaused;
    case "finished":
      return text.live.statusFinished;
    default:
      return idleFallback;
  }
}

function parseLap(value: string): number | null {
  const match = /^(\d{2}):(\d{2})\.(\d{2})$/.exec(value);
  if (!match) {
    return null;
  }

  const [, minutes, seconds, centiseconds] = match;
  return Number(minutes) * 60_000 + Number(seconds) * 1000 + Number(centiseconds) * 10;
}

export function formatTrackDirection(locale: Locale, direction: "clockwise" | "counterclockwise"): string {
  const text = copy[locale];
  return direction === "clockwise" ? text.common.clockwiseShort : text.common.counterclockwiseShort;
}

export function maybeRegisterSplit(
  state: LiveSessionState,
  track: TrackDefinition,
  location: CurrentLocation,
): LiveSessionState {
  if (state.status !== "running") {
    return state;
  }

  const splitMarker = track.splitMarkers[state.nextSplitIndex];
  if (!splitMarker) {
    return state;
  }

  if (!isWithinSplitRadius(location, splitMarker)) {
    return state;
  }

  const nextSplitTimes = [...state.currentLapSplitTimesMs, state.currentLapTimeMs];
  const referenceSplitTime = state.bestLapSplitTimesMs?.[state.nextSplitIndex] ?? null;

  return {
    ...state,
    currentLapSplitTimesMs: nextSplitTimes,
    currentDeltaMs: referenceSplitTime !== null ? state.currentLapTimeMs - referenceSplitTime : state.currentDeltaMs,
    nextSplitIndex: Math.min(state.nextSplitIndex + 1, track.splitMarkers.length),
  };
}

export function finishLap(
  state: LiveSessionState,
  locale: Locale,
): LiveSessionState {
  const nextLap = createLapFromCurrent(state, locale);
  if (!nextLap) {
    return state;
  }

  const isBestLap = state.bestLapTimeMs === null || state.currentLapTimeMs < state.bestLapTimeMs;
  const nextBestTime = isBestLap ? state.currentLapTimeMs : state.bestLapTimeMs;
  const nextBestSplits =
    isBestLap && state.currentLapSplitTimesMs.length > 0 ? [...state.currentLapSplitTimesMs] : state.bestLapSplitTimesMs;

  return {
    ...state,
    bestLapTimeMs: nextBestTime,
    bestLapSplitTimesMs: nextBestSplits,
    lastLapTimeMs: state.currentLapTimeMs,
    currentLapTimeMs: 0,
    currentDeltaMs: null,
    currentLapSplitTimesMs: [],
    nextSplitIndex: 0,
    lapCount: state.lapCount + 1,
    laps: [nextLap, ...state.laps],
  };
}
