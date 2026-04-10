import { StyleSheet, Text, View } from "react-native";

import { theme } from "../theme";

type InfoRowProps = {
  label: string;
  value: string;
  multiline?: boolean;
};

export function InfoRow({ label, value, multiline = false }: InfoRowProps) {
  return (
    <View style={styles.row}>
      <Text style={styles.label}>{label}</Text>
      <Text style={[styles.value, multiline && styles.valueMultiline]}>{value}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  row: {
    flexDirection: "row",
    justifyContent: "space-between",
    gap: theme.spacing.md,
    paddingVertical: theme.spacing.xs,
  },
  label: {
    flex: 1,
    color: theme.colors.textMuted,
    fontSize: 14,
    lineHeight: 20,
  },
  value: {
    color: theme.colors.textStrong,
    fontSize: 14,
    lineHeight: 20,
    fontWeight: "700",
    textAlign: "right",
  },
  valueMultiline: {
    maxWidth: "55%",
  },
});
