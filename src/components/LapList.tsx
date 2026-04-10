import { StyleSheet, Text, View } from "react-native";

import { copy } from "../i18n";
import { Lap, Locale } from "../types";
import { theme } from "../theme";

type LapListProps = {
  laps: Lap[];
  locale: Locale;
};

export function LapList({ laps, locale }: LapListProps) {
  const text = copy[locale];

  return (
    <View style={styles.list}>
      {laps.map((lap) => (
        <View key={lap.number} style={styles.row}>
          <View>
            <Text style={styles.lapNumber}>
              {text.common.lap} {lap.number}
            </Text>
            <Text style={styles.confidence}>{text.common.confidence[lap.confidence]}</Text>
          </View>
          <View style={styles.right}>
            <Text style={styles.time}>{lap.time}</Text>
            <Text style={styles.delta}>{lap.delta}</Text>
          </View>
        </View>
      ))}
    </View>
  );
}

const styles = StyleSheet.create({
  list: {
    gap: theme.spacing.sm,
  },
  row: {
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
    backgroundColor: theme.colors.panelSoft,
    borderRadius: 18,
    padding: theme.spacing.md,
    gap: theme.spacing.md,
  },
  lapNumber: {
    color: theme.colors.textStrong,
    fontSize: 15,
    fontWeight: "800",
  },
  confidence: {
    color: theme.colors.textMuted,
    fontSize: 13,
    marginTop: 2,
  },
  right: {
    alignItems: "flex-end",
  },
  time: {
    color: theme.colors.textStrong,
    fontSize: 16,
    fontWeight: "800",
  },
  delta: {
    color: theme.colors.accent,
    fontSize: 13,
    marginTop: 2,
  },
});
