export const liveSessionPreview = {
  trackName: "Rheinring South",
  currentLap: "01:14.82",
  lastLap: "01:13.94",
  bestLap: "01:13.41",
  flow: [
    "Choose a saved track or create a new start/finish point.",
    "Start the session and begin GPS tracking with a visible recording state.",
    "Detect a valid lap only after a directional line crossing and minimum lap time.",
    "Show current lap, last lap, best lap, and a manual lap button as backup.",
    "Save the full session locally when the run ends.",
  ],
};

export const lapPreview = [
  {
    title: "Directional crossing",
    body: "A lap should count only if the rider or driver crosses the start line in the correct direction.",
  },
  {
    title: "Minimum lap time",
    body: "Ignore repeated crossings inside a short cooldown window so one pass cannot create duplicate laps.",
  },
  {
    title: "GPS smoothing",
    body: "Use recent position samples to reduce noisy phone GPS jumps before validating a crossing event.",
  },
  {
    title: "Manual fallback",
    body: "If GPS quality drops, the driver still needs a simple button to mark a lap or correct a result.",
  },
];

export const nextSteps = [
  "Add session state and lap models in TypeScript.",
  "Integrate live GPS updates with permission handling.",
  "Build track setup on a map with start/finish placement.",
  "Persist sessions on device storage.",
  "Add an actual live timing screen instead of the static concept view.",
];
