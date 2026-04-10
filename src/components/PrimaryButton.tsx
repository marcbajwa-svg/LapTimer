import { Pressable, StyleSheet, Text } from "react-native";

import { theme } from "../theme";

type PrimaryButtonProps = {
  label: string;
  tone?: "ink" | "accent" | "soft";
  onPress: () => void;
};

export function PrimaryButton({ label, tone = "ink", onPress }: PrimaryButtonProps) {
  return (
    <Pressable onPress={onPress} style={[styles.button, toneStyles[tone].button]}>
      <Text style={[styles.label, toneStyles[tone].label]}>{label}</Text>
    </Pressable>
  );
}

const toneStyles = {
  ink: StyleSheet.create({
    button: {
      backgroundColor: theme.colors.ink,
    },
    label: {
      color: theme.colors.textOnDark,
    },
  }),
  accent: StyleSheet.create({
    button: {
      backgroundColor: theme.colors.accent,
    },
    label: {
      color: theme.colors.textOnDark,
    },
  }),
  soft: StyleSheet.create({
    button: {
      backgroundColor: theme.colors.panelSoft,
    },
    label: {
      color: theme.colors.textStrong,
    },
  }),
};

const styles = StyleSheet.create({
  button: {
    minHeight: 54,
    borderRadius: 18,
    alignItems: "center",
    justifyContent: "center",
    paddingHorizontal: theme.spacing.md,
  },
  label: {
    fontSize: 15,
    fontWeight: "800",
  },
});
