import { StatusBar } from "expo-status-bar";
import { SafeAreaView, StyleSheet } from "react-native";
import { useEffect, useRef, useState } from "react";
import * as Location from "expo-location";

import { AppShell } from "./src/components/AppShell";
import { presetTracks, buildCustomTrack } from "./src/data/tracks";
import { getPreviewSession } from "./src/data/sessionPreview";
import { copy } from "./src/i18n";
import { HomeScreen } from "./src/screens/HomeScreen";
import { LiveSessionScreen } from "./src/screens/LiveSessionScreen";
import { SessionSummaryScreen } from "./src/screens/SessionSummaryScreen";
import { TrackSetupScreen } from "./src/screens/TrackSetupScreen";
import {
  CurrentLocation,
  LiveSessionState,
  Locale,
  PermissionState,
  ScreenId,
  TrackDefinition,
} from "./src/types";
import { theme } from "./src/theme";
import { buildSessionPreview, createInitialLiveState, createLapFromCurrent, formatTrackDirection } from "./src/utils/session";

export default function App() {
  const [activeScreen, setActiveScreen] = useState<ScreenId>("home");
  const [locale, setLocale] = useState<Locale>("de");
  const [selectedTrack, setSelectedTrack] = useState<TrackDefinition>(presetTracks[0]);
  const [permissionState, setPermissionState] = useState<PermissionState>("unknown");
  const [currentLocation, setCurrentLocation] = useState<CurrentLocation | null>(null);
  const [liveState, setLiveState] = useState<LiveSessionState>(() => createInitialLiveState(getPreviewSession("de")));
  const locationSubscription = useRef<Location.LocationSubscription | null>(null);

  const text = copy[locale];
  const seedSession = getPreviewSession(locale);

  useEffect(() => {
    let cancelled = false;

    async function checkPermission() {
      const permission = await Location.getForegroundPermissionsAsync();
      if (cancelled) {
        return;
      }

      if (permission.granted) {
        setPermissionState("granted");
      } else if (permission.canAskAgain) {
        setPermissionState("unknown");
      } else {
        setPermissionState("denied");
      }
    }

    void checkPermission();

    return () => {
      cancelled = true;
    };
  }, []);

  useEffect(() => {
    let active = true;

    async function beginWatching() {
      if (permissionState !== "granted") {
        return;
      }

      const position = await Location.getCurrentPositionAsync({
        accuracy: Location.Accuracy.Balanced,
      });
      if (active) {
        setCurrentLocation({
          latitude: position.coords.latitude,
          longitude: position.coords.longitude,
          accuracy: position.coords.accuracy ?? null,
        });
      }

      locationSubscription.current?.remove();
      locationSubscription.current = await Location.watchPositionAsync(
        {
          accuracy: Location.Accuracy.Balanced,
          timeInterval: 2000,
          distanceInterval: 5,
        },
        (update) => {
          setCurrentLocation({
            latitude: update.coords.latitude,
            longitude: update.coords.longitude,
            accuracy: update.coords.accuracy ?? null,
          });
        },
      );
    }

    void beginWatching();

    return () => {
      active = false;
      locationSubscription.current?.remove();
      locationSubscription.current = null;
    };
  }, [permissionState]);

  useEffect(() => {
    setLiveState((current) => ({
      ...current,
      bestLapTimeMs: current.bestLapTimeMs ?? createInitialLiveState(seedSession).bestLapTimeMs,
    }));
  }, [locale, seedSession]);

  useEffect(() => {
    if (liveState.status !== "running") {
      return;
    }

    const interval = setInterval(() => {
      setLiveState((current) => ({
        ...current,
        sessionTimeMs: current.sessionTimeMs + 100,
        currentLapTimeMs: current.currentLapTimeMs + 100,
      }));
    }, 100);

    return () => clearInterval(interval);
  }, [liveState.status]);

  const requestLocationPermission = async () => {
    const permission = await Location.requestForegroundPermissionsAsync();
    setPermissionState(permission.granted ? "granted" : "denied");
  };

  const useCurrentPositionAsTrack = () => {
    if (!currentLocation) {
      return;
    }

    setSelectedTrack(buildCustomTrack(locale, currentLocation.latitude, currentLocation.longitude));
  };

  const startSession = () => {
    setLiveState((current) => {
      if (current.status === "finished") {
        return {
          ...createInitialLiveState(seedSession),
          status: "running",
        };
      }

      return {
        ...current,
        status: "running",
      };
    });
    setActiveScreen("live");
  };

  const pauseSession = () => {
    setLiveState((current) => ({
      ...current,
      status: current.status === "paused" ? "running" : "paused",
    }));
  };

  const endSession = () => {
    setLiveState((current) => ({
      ...current,
      status: "finished",
    }));
    setActiveScreen("summary");
  };

  const triggerManualLap = () => {
    setLiveState((current) => {
      if (current.status !== "running") {
        return current;
      }

      const nextLap = createLapFromCurrent(current, locale);
      if (!nextLap) {
        return current;
      }

      const nextBest =
        current.bestLapTimeMs === null || current.currentLapTimeMs < current.bestLapTimeMs
          ? current.currentLapTimeMs
          : current.bestLapTimeMs;

      return {
        ...current,
        bestLapTimeMs: nextBest,
        lastLapTimeMs: current.currentLapTimeMs,
        currentLapTimeMs: 0,
        lapCount: current.lapCount + 1,
        laps: [nextLap, ...current.laps],
      };
    });
  };

  const locationAccuracy = currentLocation?.accuracy ? `${Math.round(currentLocation.accuracy)} m` : seedSession.accuracy;
  const locationSummary = currentLocation
    ? `${currentLocation.latitude.toFixed(5)}, ${currentLocation.longitude.toFixed(5)}`
    : "--";

  const session = {
    ...buildSessionPreview(seedSession, liveState, locale),
    trackName: selectedTrack.name[locale],
    startLineLabel: selectedTrack.markerLabel[locale],
    accuracy: locationAccuracy,
    gpsStatus:
      permissionState === "granted"
        ? currentLocation
          ? buildSessionPreview(seedSession, liveState, locale).gpsStatus
          : text.setup.locationGranted
        : permissionState === "denied"
          ? text.setup.locationDenied
          : seedSession.gpsStatus,
  };

  const renderScreen = () => {
    switch (activeScreen) {
      case "home":
        return <HomeScreen locale={locale} session={session} onNavigate={setActiveScreen} onStartSession={startSession} />;
      case "setup":
        return (
          <TrackSetupScreen
            locale={locale}
            session={session}
            selectedTrack={selectedTrack}
            permissionState={permissionState}
            currentPositionLabel={locationSummary}
            onNavigate={setActiveScreen}
            onRequestLocationPermission={requestLocationPermission}
            onSelectTrack={setSelectedTrack}
            onUseCurrentPosition={useCurrentPositionAsTrack}
            presetTracks={presetTracks}
          />
        );
      case "live":
        return (
          <LiveSessionScreen
            locale={locale}
            session={session}
            sessionStatus={liveState.status}
            onNavigate={setActiveScreen}
            onStartSession={startSession}
            onPauseSession={pauseSession}
            onEndSession={endSession}
            onManualLap={triggerManualLap}
          />
        );
      case "summary":
        return (
          <SessionSummaryScreen
            locale={locale}
            session={session}
            sessionStatus={liveState.status}
            onNavigate={setActiveScreen}
            onStartSession={startSession}
          />
        );
      default:
        return <HomeScreen locale={locale} session={session} onNavigate={setActiveScreen} onStartSession={startSession} />;
    }
  };

  return (
    <SafeAreaView style={styles.safeArea}>
      <StatusBar style="dark" />
      <AppShell
        activeScreen={activeScreen}
        navCopy={text.nav}
        languageToggleLabel={text.languageToggle}
        onNavigate={setActiveScreen}
        onToggleLanguage={() => setLocale((current) => (current === "de" ? "en" : "de"))}
      >
        {renderScreen()}
      </AppShell>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  safeArea: {
    flex: 1,
    backgroundColor: theme.colors.background,
  },
});
