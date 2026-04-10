import { StyleSheet, Text, View } from "react-native";

import { theme } from "../theme";

type ScreenHeaderProps = {
  eyebrow: string;
  title: string;
  subtitle: string;
};

export function ScreenHeader({ eyebrow, title, subtitle }: ScreenHeaderProps) {
  return (
    <View style={styles.hero}>
      <Text style={styles.eyebrow}>{eyebrow}</Text>
      <Text style={styles.title}>{title}</Text>
      <Text style={styles.subtitle}>{subtitle}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
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
    fontSize: 32,
    lineHeight: 38,
    fontWeight: "800",
  },
  subtitle: {
    color: theme.colors.textMuted,
    fontSize: 16,
    lineHeight: 24,
  },
});
