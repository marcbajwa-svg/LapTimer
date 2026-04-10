import { StatusBar } from "expo-status-bar";
import { SafeAreaView, StyleSheet } from "react-native";
import { useState } from "react";

import { AppShell } from "./src/components/AppShell";
import { getPreviewSession } from "./src/data/sessionPreview";
import { copy } from "./src/i18n";
import { HomeScreen } from "./src/screens/HomeScreen";
import { LiveSessionScreen } from "./src/screens/LiveSessionScreen";
import { SessionSummaryScreen } from "./src/screens/SessionSummaryScreen";
import { TrackSetupScreen } from "./src/screens/TrackSetupScreen";
import { Locale, ScreenId } from "./src/types";
import { theme } from "./src/theme";

export default function App() {
  const [activeScreen, setActiveScreen] = useState<ScreenId>("home");
  const [locale, setLocale] = useState<Locale>("de");
  const session = getPreviewSession(locale);
  const text = copy[locale];

  const renderScreen = () => {
    switch (activeScreen) {
      case "home":
        return <HomeScreen locale={locale} session={session} onNavigate={setActiveScreen} />;
      case "setup":
        return <TrackSetupScreen locale={locale} session={session} onNavigate={setActiveScreen} />;
      case "live":
        return <LiveSessionScreen locale={locale} session={session} onNavigate={setActiveScreen} />;
      case "summary":
        return <SessionSummaryScreen locale={locale} session={session} onNavigate={setActiveScreen} />;
      default:
        return <HomeScreen locale={locale} session={session} onNavigate={setActiveScreen} />;
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
