import { StyleSheet, Text, View } from "react-native";

import { theme } from "../theme";

type Tone = "sand" | "ink" | "clay" | "mint";

const toneMap: Record<Tone, { backgroundColor: string; valueColor: string }> = {
  sand: {
    backgroundColor: theme.colors.sand,
    valueColor: theme.colors.textStrong,
  },
  ink: {
    backgroundColor: theme.colors.ink,
    valueColor: theme.colors.textOnDark,
  },
  clay: {
    backgroundColor: theme.colors.clay,
    valueColor: theme.colors.textStrong,
  },
  mint: {
    backgroundColor: theme.colors.mint,
    valueColor: theme.colors.textStrong,
  },
};

type StatCardProps = {
  label: string;
  value: string;
  tone: Tone;
};

export function StatCard({ label, value, tone }: StatCardProps) {
  const palette = toneMap[tone];

  return (
    <View style={[styles.card, { backgroundColor: palette.backgroundColor }]}>
      <Text style={styles.label}>{label}</Text>
      <Text style={[styles.value, { color: palette.valueColor }]}>{value}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  card: {
    borderRadius: 24,
    padding: theme.spacing.lg,
    gap: theme.spacing.xs,
  },
  label: {
    color: theme.colors.textMuted,
    fontSize: 13,
    fontWeight: "700",
    letterSpacing: 0.8,
    textTransform: "uppercase",
  },
  value: {
    fontSize: 28,
    lineHeight: 34,
    fontWeight: "800",
  },
});
