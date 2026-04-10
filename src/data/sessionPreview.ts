import { Locale, SessionPreview } from "../types";

const sessions: Record<Locale, SessionPreview> = {
  en: {
    trackName: "Rheinring South",
    mode: "Trackday Prototype",
    currentLap: "01:14.82",
    lastLap: "01:13.94",
    bestLap: "01:13.41",
    sessionTime: "00:23:18",
    gpsStatus: "Stable GPS lock",
    accuracy: "4 m",
    startLineLabel: "Gate A / pit exit straight",
    totalLaps: 12,
    flow: [
      "Choose a track or create a new one from the setup screen.",
      "Place the start line and confirm the driving direction.",
      "Start a session and keep the live timer visible during the run.",
      "Use automatic detection first, with a manual lap button as backup.",
      "End the session and review laps before saving.",
    ],
    setupChecklist: [
      "Pin the start and finish area on a map or reuse a saved track.",
      "Define the crossing direction so wrong-way passes do not count.",
      "Show GPS quality before the session can go live.",
      "Keep a manual lap input ready for unreliable signal conditions.",
    ],
    summaryNotes: [
      "Best lap improved by 0.53 s after the third flying lap.",
      "Two laps were marked medium confidence because of lower GPS quality.",
      "The timing flow should stay readable with gloves or at quick glance.",
    ],
    lapDetectionRules: [
      {
        title: "Directional crossing",
        body: "Count a lap only when the movement vector crosses the line in the configured direction.",
      },
      {
        title: "Minimum lap time",
        body: "Use a cooldown window so one pass cannot accidentally create a duplicate lap.",
      },
      {
        title: "GPS smoothing",
        body: "Recent samples should be filtered before validating a crossing event from phone GPS.",
      },
      {
        title: "Manual fallback",
        body: "When signal quality drops, the driver still needs a safe manual lap option.",
      },
    ],
    laps: [
      { number: 1, time: "01:15.62", delta: "+2.21", confidence: "medium" },
      { number: 2, time: "01:14.08", delta: "+0.67", confidence: "high" },
      { number: 3, time: "01:13.41", delta: "Best", confidence: "high" },
      { number: 4, time: "01:13.94", delta: "+0.53", confidence: "high" },
      { number: 5, time: "01:14.82", delta: "+1.41", confidence: "medium" },
    ],
  },
  de: {
    trackName: "Rheinring Süd",
    mode: "Trackday-Prototyp",
    currentLap: "01:14.82",
    lastLap: "01:13.94",
    bestLap: "01:13.41",
    sessionTime: "00:23:18",
    gpsStatus: "Stabiler GPS-Lock",
    accuracy: "4 m",
    startLineLabel: "Tor A / Gerade nach der Boxenausfahrt",
    totalLaps: 12,
    flow: [
      "Wähle eine Strecke oder lege im Setup eine neue an.",
      "Setze die Startlinie und bestätige die Fahrtrichtung.",
      "Starte die Session und halte den Live-Timer während des Turns sichtbar.",
      "Nutze zuerst die automatische Erkennung, mit manueller Runden-Taste als Backup.",
      "Beende die Session und prüfe die Runden vor dem Speichern.",
    ],
    setupChecklist: [
      "Markiere Start und Ziel auf der Karte oder nutze eine gespeicherte Strecke.",
      "Lege die Überquerungsrichtung fest, damit Falschfahrten nicht zählen.",
      "Zeige die GPS-Qualität an, bevor die Session live gehen darf.",
      "Halte eine manuelle Rundeneingabe für schwache Signale bereit.",
    ],
    summaryNotes: [
      "Die beste Runde verbesserte sich nach der dritten fliegenden Runde um 0,53 s.",
      "Zwei Runden wurden wegen schwächerer GPS-Qualität mit mittlerer Sicherheit markiert.",
      "Der Timing-Ablauf soll auch mit Handschuhen und auf einen Blick lesbar bleiben.",
    ],
    lapDetectionRules: [
      {
        title: "Richtungsprüfung",
        body: "Eine Runde zählt nur, wenn der Bewegungsvektor die Linie in der richtigen Richtung überquert.",
      },
      {
        title: "Mindestzeit pro Runde",
        body: "Ein Cooldown-Fenster verhindert, dass eine einzelne Überfahrt doppelt gezählt wird.",
      },
      {
        title: "GPS-Glättung",
        body: "Aktuelle Samples sollten gefiltert werden, bevor ein Crossing-Event durch Handy-GPS bestätigt wird.",
      },
      {
        title: "Manuelles Backup",
        body: "Wenn die Signalqualität sinkt, braucht der Fahrer weiterhin eine sichere manuelle Rundenoption.",
      },
    ],
    laps: [
      { number: 1, time: "01:15.62", delta: "+2.21", confidence: "medium" },
      { number: 2, time: "01:14.08", delta: "+0.67", confidence: "high" },
      { number: 3, time: "01:13.41", delta: "Bestzeit", confidence: "high" },
      { number: 4, time: "01:13.94", delta: "+0.53", confidence: "high" },
      { number: 5, time: "01:14.82", delta: "+1.41", confidence: "medium" },
    ],
  },
};

export function getPreviewSession(locale: Locale): SessionPreview {
  return sessions[locale];
}
