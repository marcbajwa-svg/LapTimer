export type ScreenId = "home" | "setup" | "live" | "summary";
export type Locale = "en" | "de";

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
