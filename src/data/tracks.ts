import { Locale, TrackDefinition } from "../types";

export const presetTracks: TrackDefinition[] = [
  {
    id: "rheinring-south",
    name: { en: "Rheinring South", de: "Rheinring Sued" },
    markerLabel: {
      en: "Gate A / pit exit straight",
      de: "Tor A / Gerade nach der Boxenausfahrt",
    },
    direction: "clockwise",
    minimumLapMs: 25_000,
    latitude: 50.1109,
    longitude: 8.6821,
    source: "preset",
  },
  {
    id: "forest-loop",
    name: { en: "Forest Loop", de: "Forest Loop" },
    markerLabel: {
      en: "Main bridge braking zone",
      de: "Bremszone an der Hauptbruecke",
    },
    direction: "clockwise",
    minimumLapMs: 35_000,
    latitude: 48.1351,
    longitude: 11.582,
    source: "preset",
  },
  {
    id: "harbor-kart",
    name: { en: "Harbor Kart Track", de: "Harbor Kart Track" },
    markerLabel: {
      en: "Grid line after the final hairpin",
      de: "Grid-Linie nach der letzten Haarnadel",
    },
    direction: "counterclockwise",
    minimumLapMs: 18_000,
    latitude: 53.5511,
    longitude: 9.9937,
    source: "preset",
  },
];

export function buildCustomTrack(locale: Locale, latitude: number, longitude: number): TrackDefinition {
  return {
    id: `custom-${Date.now()}`,
    name: {
      en: "Custom Track",
      de: "Eigene Strecke",
    },
    markerLabel: {
      en: `Custom start point ${formatCoords(latitude, longitude)}`,
      de: `Eigener Startpunkt ${formatCoords(latitude, longitude)}`,
    },
    direction: "clockwise",
    minimumLapMs: 25_000,
    latitude,
    longitude,
    source: "custom",
  };
}

function formatCoords(latitude: number, longitude: number): string {
  return `${latitude.toFixed(5)}, ${longitude.toFixed(5)}`;
}
