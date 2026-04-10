import { StatusBar } from "expo-status-bar";
import { SafeAreaView, StyleSheet } from "react-native";
import { useEffect, useState } from "react";

import { AppShell } from "./src/components/AppShell";
import { getPreviewSession } from "./src/data/sessionPreview";
import { copy } from "./src/i18n";
import { HomeScreen } from "./src/screens/HomeScreen";
import { LiveSessionScreen } from "./src/screens/LiveSessionScreen";
import { SessionSummaryScreen } from "./src/screens/SessionSummaryScreen";
import { TrackSetupScreen } from "./src/screens/TrackSetupScreen";
import { LiveSessionState, Locale, ScreenId } from "./src/types";
import { theme } from "./src/theme";
import { buildSessionPreview, createInitialLiveState, createLapFromCurrent } from "./src/utils/session";

export default function App() {
  const [activeScreen, setActiveScreen] = useState<ScreenId>("home");
  const [locale, setLocale] = useState<Locale>("de");
  const text = copy[locale];
  const seedSession = getPreviewSession(locale);
  const [liveState, setLiveState] = useState<LiveSessionState>(() => createInitialLiveState(seedSession));
  const session = buildSessionPreview(seedSession, liveState, locale);

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

      const nextBest = current.bestLapTimeMs === null || current.currentLapTimeMs < current.bestLapTimeMs
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

  const renderScreen = () => {
    switch (activeScreen) {
      case "home":
        return <HomeScreen locale={locale} session={session} onNavigate={setActiveScreen} onStartSession={startSession} />;
      case "setup":
        return <TrackSetupScreen locale={locale} session={session} onNavigate={setActiveScreen} />;
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
