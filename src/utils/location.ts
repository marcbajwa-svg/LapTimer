import { CurrentLocation, SplitMarker } from "../types";

const EARTH_RADIUS_METERS = 6_371_000;

export function distanceMeters(a: CurrentLocation, b: Pick<SplitMarker, "latitude" | "longitude">): number {
  const lat1 = toRadians(a.latitude);
  const lat2 = toRadians(b.latitude);
  const deltaLat = toRadians(b.latitude - a.latitude);
  const deltaLon = toRadians(b.longitude - a.longitude);

  const haversine =
    Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
    Math.cos(lat1) * Math.cos(lat2) * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

  const arc = 2 * Math.atan2(Math.sqrt(haversine), Math.sqrt(1 - haversine));
  return EARTH_RADIUS_METERS * arc;
}

export function isWithinSplitRadius(location: CurrentLocation, splitMarker: SplitMarker): boolean {
  const distance = distanceMeters(location, splitMarker);
  const accuracyPadding = location.accuracy ?? 0;
  return distance <= splitMarker.radiusMeters + accuracyPadding;
}

function toRadians(value: number): number {
  return (value * Math.PI) / 180;
}
