import { CurrentLocation, Locale, TrackDefinition } from "../types";
import { distanceMeters } from "../utils/location";

export const presetTracks: TrackDefinition[] = [
  {
    id: "hockenheimring",
    name: { en: "Hockenheimring", de: "Hockenheimring" },
    markerLabel: {
      en: "Start / finish on the main straight",
      de: "Start / Ziel auf der Hauptgeraden",
    },
    direction: "clockwise",
    minimumLapMs: 95_000,
    latitude: 49.3277,
    longitude: 8.5655,
    splitMarkers: [
      {
        id: "hockenheimring-s1",
        label: { en: "Sector 1", de: "Sektor 1" },
        latitude: 49.3293,
        longitude: 8.5713,
        radiusMeters: 55,
      },
      {
        id: "hockenheimring-s2",
        label: { en: "Sector 2", de: "Sektor 2" },
        latitude: 49.3238,
        longitude: 8.5732,
        radiusMeters: 55,
      },
      {
        id: "hockenheimring-s3",
        label: { en: "Sector 3", de: "Sektor 3" },
        latitude: 49.3255,
        longitude: 8.5607,
        radiusMeters: 55,
      },
    ],
    source: "preset",
  },
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
    splitMarkers: [
      {
        id: "rheinring-south-s1",
        label: { en: "Sector 1", de: "Sektor 1" },
        latitude: 50.1114,
        longitude: 8.683,
        radiusMeters: 35,
      },
      {
        id: "rheinring-south-s2",
        label: { en: "Sector 2", de: "Sektor 2" },
        latitude: 50.1102,
        longitude: 8.6835,
        radiusMeters: 35,
      },
      {
        id: "rheinring-south-s3",
        label: { en: "Sector 3", de: "Sektor 3" },
        latitude: 50.1101,
        longitude: 8.6814,
        radiusMeters: 35,
      },
    ],
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
    splitMarkers: [
      {
        id: "forest-loop-s1",
        label: { en: "Sector 1", de: "Sektor 1" },
        latitude: 48.1358,
        longitude: 11.5824,
        radiusMeters: 40,
      },
      {
        id: "forest-loop-s2",
        label: { en: "Sector 2", de: "Sektor 2" },
        latitude: 48.1348,
        longitude: 11.5832,
        radiusMeters: 40,
      },
      {
        id: "forest-loop-s3",
        label: { en: "Sector 3", de: "Sektor 3" },
        latitude: 48.1343,
        longitude: 11.5816,
        radiusMeters: 40,
      },
    ],
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
    splitMarkers: [
      {
        id: "harbor-kart-s1",
        label: { en: "Sector 1", de: "Sektor 1" },
        latitude: 53.5514,
        longitude: 9.9943,
        radiusMeters: 25,
      },
      {
        id: "harbor-kart-s2",
        label: { en: "Sector 2", de: "Sektor 2" },
        latitude: 53.5508,
        longitude: 9.9944,
        radiusMeters: 25,
      },
      {
        id: "harbor-kart-s3",
        label: { en: "Sector 3", de: "Sektor 3" },
        latitude: 53.5507,
        longitude: 9.9931,
        radiusMeters: 25,
      },
    ],
    source: "preset",
  },
];

const SUGGESTION_RADIUS_METERS = 6_000;

export function findNearbyTrack(location: CurrentLocation, tracks: TrackDefinition[]) {
  let bestMatch: { track: TrackDefinition; distanceMeters: number } | null = null;

  for (const track of tracks) {
    if (track.source !== "preset") {
      continue;
    }

    const distance = distanceMeters(location, track);
    if (distance > SUGGESTION_RADIUS_METERS) {
      continue;
    }

    if (!bestMatch || distance < bestMatch.distanceMeters) {
      bestMatch = {
        track,
        distanceMeters: distance,
      };
    }
  }

  return bestMatch;
}

export function formatTrackDistance(distanceMetersValue: number): string {
  if (distanceMetersValue < 1000) {
    return `${Math.round(distanceMetersValue)} m`;
  }

  return `${(distanceMetersValue / 1000).toFixed(1)} km`;
}

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
    splitMarkers: [],
    source: "custom",
  };
}

function formatCoords(latitude: number, longitude: number): string {
  return `${latitude.toFixed(5)}, ${longitude.toFixed(5)}`;
}
