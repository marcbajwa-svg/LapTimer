import { StatusBar } from "expo-status-bar";
import { SafeAreaView, ScrollView, StyleSheet, Text, View } from "react-native";

import { StatCard } from "./src/components/StatCard";
import { lapPreview, liveSessionPreview, nextSteps } from "./src/data/v1Scope";
import { theme } from "./src/theme";

export default function App() {
  return (
    <SafeAreaView style={styles.safeArea}>
      <StatusBar style="dark" />
      <ScrollView contentContainerStyle={styles.content}>
        <View style={styles.hero}>
          <Text style={styles.eyebrow}>LapTimer V1</Text>
          <Text style={styles.title}>Mobile lap timing for the first real prototype.</Text>
          <Text style={styles.subtitle}>
            The app starts simple: define a start line, track a session, detect laps from GPS,
            and keep the timing view clean under pressure.
          </Text>
        </View>

        <View style={styles.grid}>
          <StatCard label="Track" value={liveSessionPreview.trackName} tone="sand" />
          <StatCard label="Current Lap" value={liveSessionPreview.currentLap} tone="ink" />
          <StatCard label="Last Lap" value={liveSessionPreview.lastLap} tone="clay" />
          <StatCard label="Best Lap" value={liveSessionPreview.bestLap} tone="mint" />
        </View>

        <View style={styles.panel}>
          <Text style={styles.panelTitle}>V1 Session Flow</Text>
          <View style={styles.flowList}>
            {liveSessionPreview.flow.map((step) => (
              <View key={step} style={styles.flowItem}>
                <View style={styles.bullet} />
                <Text style={styles.flowText}>{step}</Text>
              </View>
            ))}
          </View>
        </View>

        <View style={styles.panel}>
          <Text style={styles.panelTitle}>Lap Detection Rules</Text>
          <View style={styles.ruleList}>
            {lapPreview.map((rule) => (
              <View key={rule.title} style={styles.ruleCard}>
                <Text style={styles.ruleTitle}>{rule.title}</Text>
                <Text style={styles.ruleBody}>{rule.body}</Text>
              </View>
            ))}
          </View>
        </View>

        <View style={styles.panel}>
          <Text style={styles.panelTitle}>Next Build Steps</Text>
          <View style={styles.flowList}>
            {nextSteps.map((step) => (
              <View key={step} style={styles.flowItem}>
                <View style={styles.bulletAccent} />
                <Text style={styles.flowText}>{step}</Text>
              </View>
            ))}
          </View>
        </View>
      </ScrollView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  safeArea: {
    flex: 1,
    backgroundColor: theme.colors.background,
  },
  content: {
    paddingHorizontal: theme.spacing.lg,
    paddingTop: theme.spacing.lg,
    paddingBottom: theme.spacing.xxl,
    gap: theme.spacing.lg,
  },
  hero: {
    backgroundColor: theme.colors.hero,
    borderRadius: 28,
    padding: theme.spacing.xl,
    gap: theme.spacing.md,
  },
  eyebrow: {
    color: theme.colors.heroAccent,
    fontSize: 14,
    fontWeight: "700",
    letterSpacing: 1.4,
    textTransform: "uppercase",
  },
  title: {
    color: theme.colors.textStrong,
    fontSize: 34,
    lineHeight: 40,
    fontWeight: "800",
  },
  subtitle: {
    color: theme.colors.textMuted,
    fontSize: 16,
    lineHeight: 24,
  },
  grid: {
    gap: theme.spacing.md,
  },
  panel: {
    backgroundColor: theme.colors.surface,
    borderRadius: 24,
    padding: theme.spacing.lg,
    gap: theme.spacing.md,
  },
  panelTitle: {
    color: theme.colors.textStrong,
    fontSize: 20,
    fontWeight: "800",
  },
  flowList: {
    gap: theme.spacing.md,
  },
  flowItem: {
    flexDirection: "row",
    alignItems: "flex-start",
    gap: theme.spacing.sm,
  },
  bullet: {
    width: 10,
    height: 10,
    borderRadius: 999,
    marginTop: 6,
    backgroundColor: theme.colors.ink,
  },
  bulletAccent: {
    width: 10,
    height: 10,
    borderRadius: 999,
    marginTop: 6,
    backgroundColor: theme.colors.accent,
  },
  flowText: {
    flex: 1,
    color: theme.colors.textStrong,
    fontSize: 15,
    lineHeight: 23,
  },
  ruleList: {
    gap: theme.spacing.md,
  },
  ruleCard: {
    backgroundColor: theme.colors.panelSoft,
    borderRadius: 18,
    padding: theme.spacing.md,
    gap: theme.spacing.xs,
  },
  ruleTitle: {
    color: theme.colors.textStrong,
    fontSize: 16,
    fontWeight: "700",
  },
  ruleBody: {
    color: theme.colors.textMuted,
    fontSize: 14,
    lineHeight: 21,
  },
});
