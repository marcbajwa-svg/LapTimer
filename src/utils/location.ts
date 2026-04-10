import { CurrentLocation, LapTracePoint, SplitMarker } from "../types";

const EARTH_RADIUS_METERS = 6_371_000;

type Coordinate = Pick<CurrentLocation, "latitude" | "longitude"> | Pick<SplitMarker, "latitude" | "longitude">;

export function distanceMeters(a: Coordinate, b: Coordinate): number {
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

export function findNearestTracePoint(
  location: CurrentLocation,
  trace: LapTracePoint[],
): { point: LapTracePoint; distanceMeters: number } | null {
  if (trace.length === 0) {
    return null;
  }

  let nearest = trace[0];
  let nearestDistance = distanceMeters(location, trace[0]);

  for (let index = 1; index < trace.length; index += 1) {
    const candidate = trace[index];
    const candidateDistance = distanceMeters(location, candidate);

    if (candidateDistance < nearestDistance) {
      nearest = candidate;
      nearestDistance = candidateDistance;
    }
  }

  return {
    point: nearest,
    distanceMeters: nearestDistance,
  };
}

function toRadians(value: number): number {
  return (value * Math.PI) / 180;
}
