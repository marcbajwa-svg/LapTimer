import { Locale, ScreenId } from "./types";

type ScreenText = {
  eyebrow: string;
  title: string;
  subtitle: string;
};

type Dictionary = {
  languageToggle: string;
  nav: Record<ScreenId, string>;
  common: {
    track: string;
    mode: string;
    gps: string;
    currentLap: string;
    lastLap: string;
    bestLap: string;
    sessionTime: string;
    totalLaps: string;
    marker: string;
    direction: string;
    minimumLap: string;
    clockwise: string;
    estimatedAccuracy: string;
    confidence: Record<"high" | "medium" | "low", string>;
    lap: string;
    rec: string;
  };
  home: ScreenText & {
    sessionFlowTitle: string;
    sessionFlowSubtitle: string;
    quickActionsTitle: string;
    quickActionsSubtitle: string;
    configureTrack: string;
    openLiveTimer: string;
    reviewSummary: string;
  };
  setup: ScreenText & {
    startFinishTitle: string;
    startFinishSubtitle: string;
    checklistTitle: string;
    checklistSubtitle: string;
    nextTitle: string;
    nextSubtitle: string;
    previewLiveSession: string;
    backHome: string;
  };
  live: ScreenText & {
    recordingStatusTitle: string;
    recordingStatusSubtitle: string;
    driverActionsTitle: string;
    driverActionsSubtitle: string;
    manualLapTrigger: string;
    openSummary: string;
  };
  summary: ScreenText & {
    reviewNotesTitle: string;
    reviewNotesSubtitle: string;
    lapListTitle: string;
    lapListSubtitle: string;
    nextTitle: string;
    nextSubtitle: string;
    startNewSession: string;
  };
};

export const copy: Record<Locale, Dictionary> = {
  en: {
    languageToggle: "Deutsch",
    nav: { home: "Home", setup: "Setup", live: "Live", summary: "Summary" },
    common: {
      track: "Track",
      mode: "Mode",
      gps: "GPS",
      currentLap: "Current Lap",
      lastLap: "Last Lap",
      bestLap: "Best Lap",
      sessionTime: "Session Time",
      totalLaps: "Total Laps",
      marker: "Marker",
      direction: "Direction",
      minimumLap: "Minimum Lap",
      clockwise: "Clockwise",
      estimatedAccuracy: "Estimated accuracy",
      confidence: {
        high: "high confidence",
        medium: "medium confidence",
        low: "low confidence",
      },
      lap: "Lap",
      rec: "REC",
    },
    home: {
      eyebrow: "LapTimer V1",
      title: "Phone-first timing with a clear route into the session.",
      subtitle:
        "This home screen gives the driver one obvious path: confirm track setup, start timing, and review saved laps without hunting through menus.",
      sessionFlowTitle: "Session Flow",
      sessionFlowSubtitle: "This becomes the onboarding logic for the first-time user.",
      quickActionsTitle: "Quick Actions",
      quickActionsSubtitle: "For V1 the user should always understand the next safe action.",
      configureTrack: "Configure Track",
      openLiveTimer: "Open Live Timer",
      reviewSummary: "Review Session Summary",
    },
    setup: {
      eyebrow: "Track Setup",
      title: "Define the start line before timing begins.",
      subtitle:
        "This screen is where GPS, direction, and minimum validation rules get locked in before the live session starts.",
      startFinishTitle: "Start / Finish",
      startFinishSubtitle: "V1 uses a simple guided setup before we add real map interaction.",
      checklistTitle: "Setup Checklist",
      checklistSubtitle: "These checks reduce bad laps before the user even leaves pit lane.",
      nextTitle: "Next Interaction",
      nextSubtitle: "The real implementation here will open the map and capture start-line coordinates.",
      previewLiveSession: "Preview Live Session",
      backHome: "Back To Home",
    },
    live: {
      eyebrow: "Live Session",
      title: "Keep timing information readable at speed.",
      subtitle:
        "The live screen should prioritize large timing data, visible recording state, GPS confidence, and a manual lap action that works instantly.",
      recordingStatusTitle: "Recording Status",
      recordingStatusSubtitle: "This is the information the user should trust at a glance.",
      driverActionsTitle: "Driver Actions",
      driverActionsSubtitle: "These actions become the first high-priority interaction targets.",
      manualLapTrigger: "Manual Lap Trigger",
      openSummary: "Open Summary",
    },
    summary: {
      eyebrow: "Session Summary",
      title: "Review the session before it is saved as a trusted result.",
      subtitle:
        "This screen groups the best lap, total lap count, confidence notes, and the lap list so the driver can quickly spot suspicious timing.",
      reviewNotesTitle: "Review Notes",
      reviewNotesSubtitle: "These notes model what later becomes confidence and validation feedback.",
      lapListTitle: "Lap List",
      lapListSubtitle: "A V1 summary needs a fast, scan-friendly lap table.",
      nextTitle: "Next Step",
      nextSubtitle: "From here the user should be able to save, export, or start another run.",
      startNewSession: "Start New Session",
    },
  },
  de: {
    languageToggle: "English",
    nav: { home: "Start", setup: "Setup", live: "Live", summary: "Auswertung" },
    common: {
      track: "Strecke",
      mode: "Modus",
      gps: "GPS",
      currentLap: "Aktuelle Runde",
      lastLap: "Letzte Runde",
      bestLap: "Beste Runde",
      sessionTime: "Sessionzeit",
      totalLaps: "Runden gesamt",
      marker: "Marker",
      direction: "Richtung",
      minimumLap: "Mindestzeit",
      clockwise: "Im Uhrzeigersinn",
      estimatedAccuracy: "Geschätzte Genauigkeit",
      confidence: {
        high: "hohe Sicherheit",
        medium: "mittlere Sicherheit",
        low: "niedrige Sicherheit",
      },
      lap: "Runde",
      rec: "REC",
    },
    home: {
      eyebrow: "LapTimer V1",
      title: "Handy-zentriertes Timing mit einem klaren Weg in die Session.",
      subtitle:
        "Dieser Startscreen gibt dem Fahrer einen klaren Ablauf: Setup prüfen, Timing starten und die Session ohne Menüsuche auswerten.",
      sessionFlowTitle: "Session-Ablauf",
      sessionFlowSubtitle: "Daraus wird später das Onboarding für den ersten Start.",
      quickActionsTitle: "Schnellaktionen",
      quickActionsSubtitle: "In V1 soll der Nutzer jederzeit die nächste sichere Aktion verstehen.",
      configureTrack: "Strecke einrichten",
      openLiveTimer: "Live-Timer öffnen",
      reviewSummary: "Session auswerten",
    },
    setup: {
      eyebrow: "Strecken-Setup",
      title: "Lege die Start-Ziel-Linie fest, bevor das Timing beginnt.",
      subtitle:
        "Hier werden GPS, Fahrtrichtung und Mindestvalidierung festgelegt, bevor die Live-Session startet.",
      startFinishTitle: "Start / Ziel",
      startFinishSubtitle: "V1 nutzt zunächst ein einfaches geführtes Setup, bevor wir echte Karteninteraktion hinzufügen.",
      checklistTitle: "Setup-Checkliste",
      checklistSubtitle: "Diese Checks reduzieren fehlerhafte Runden schon vor dem Losfahren.",
      nextTitle: "Nächster Schritt",
      nextSubtitle: "Später öffnet sich hier die Karte und erfasst die Koordinaten der Startlinie.",
      previewLiveSession: "Live-Session ansehen",
      backHome: "Zurück zum Start",
    },
    live: {
      eyebrow: "Live-Session",
      title: "Halte die Timing-Daten auch bei Geschwindigkeit gut lesbar.",
      subtitle:
        "Der Live-Screen priorisiert große Zeitwerte, einen klar sichtbaren Aufnahmezustand, GPS-Vertrauen und eine sofort nutzbare manuelle Rundenaktion.",
      recordingStatusTitle: "Aufnahmestatus",
      recordingStatusSubtitle: "Das sind die Informationen, denen der Nutzer auf einen Blick vertrauen soll.",
      driverActionsTitle: "Fahreraktionen",
      driverActionsSubtitle: "Diese Aktionen werden später die wichtigsten Interaktionsziele.",
      manualLapTrigger: "Runde manuell auslösen",
      openSummary: "Auswertung öffnen",
    },
    summary: {
      eyebrow: "Session-Auswertung",
      title: "Prüfe die Session, bevor sie als vertrauenswürdiges Ergebnis gespeichert wird.",
      subtitle:
        "Dieser Screen bündelt beste Runde, Rundenzahl, Vertrauenshinweise und die Rundenliste, damit auffällige Zeiten schnell sichtbar werden.",
      reviewNotesTitle: "Prüfhinweise",
      reviewNotesSubtitle: "Diese Hinweise modellieren später Validierung und Vertrauensstufen.",
      lapListTitle: "Rundenliste",
      lapListSubtitle: "Eine V1-Auswertung braucht eine schnell lesbare Übersicht aller Runden.",
      nextTitle: "Nächster Schritt",
      nextSubtitle: "Von hier aus soll der Nutzer speichern, exportieren oder eine neue Session starten können.",
      startNewSession: "Neue Session starten",
    },
  },
};
