export type ScreenId = "home" | "setup" | "live" | "summary";
export type Locale = "en" | "de";
export type SessionStatus = "idle" | "running" | "paused" | "finished";
export type PermissionState = "unknown" | "granted" | "denied";

export type Lap = {
  number: number;
  time: string;
  delta: string;
  confidence: "high" | "medium" | "low";
};

export type SessionPreview = {
  trackName: string;
  mode: string;
  currentLap: string;
  currentDelta: string;
  lastLap: string;
  bestLap: string;
  sessionTime: string;
  gpsStatus: string;
  accuracy: string;
  startLineLabel: string;
  totalLaps: number;
  flow: string[];
  setupChecklist: string[];
  summaryNotes: string[];
  lapDetectionRules: {
    title: string;
    body: string;
  }[];
  laps: Lap[];
};

export type LiveSessionState = {
  status: SessionStatus;
  sessionTimeMs: number;
  currentLapTimeMs: number;
  bestLapTimeMs: number | null;
  lastLapTimeMs: number | null;
  lapCount: number;
  laps: Lap[];
};

export type TrackDefinition = {
  id: string;
  name: Record<Locale, string>;
  markerLabel: Record<Locale, string>;
  direction: "clockwise" | "counterclockwise";
  minimumLapMs: number;
  latitude: number;
  longitude: number;
  source: "preset" | "custom";
};

export type CurrentLocation = {
  latitude: number;
  longitude: number;
  accuracy: number | null;
};
