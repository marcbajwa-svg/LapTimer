import { StyleSheet, Text, View } from "react-native";

import { PrimaryButton } from "../components/PrimaryButton";
import { ScreenHeader } from "../components/ScreenHeader";
import { SectionCard } from "../components/SectionCard";
import { StatCard } from "../components/StatCard";
import { copy } from "../i18n";
import { Locale, SessionPreview, ScreenId } from "../types";
import { theme } from "../theme";

type LiveSessionScreenProps = {
  locale: Locale;
  session: SessionPreview;
  onNavigate: (screen: ScreenId) => void;
};

export function LiveSessionScreen({ locale, session, onNavigate }: LiveSessionScreenProps) {
  const text = copy[locale];

  return (
    <>
      <ScreenHeader eyebrow={text.live.eyebrow} title={text.live.title} subtitle={text.live.subtitle} />

      <View style={styles.grid}>
        <StatCard label={text.common.currentLap} value={session.currentLap} tone="ink" />
        <StatCard label={text.common.lastLap} value={session.lastLap} tone="clay" />
        <StatCard label={text.common.bestLap} value={session.bestLap} tone="mint" />
        <StatCard label={text.common.sessionTime} value={session.sessionTime} tone="sand" />
      </View>

      <SectionCard title={text.live.recordingStatusTitle} subtitle={text.live.recordingStatusSubtitle}>
        <View style={styles.statusBlock}>
          <View style={styles.statusChip}>
            <Text style={styles.statusChipText}>{text.common.rec}</Text>
          </View>
          <Text style={styles.statusText}>{session.gpsStatus}</Text>
          <Text style={styles.statusMeta}>
            {text.common.estimatedAccuracy}: {session.accuracy}
          </Text>
        </View>
      </SectionCard>

      <SectionCard title={text.live.driverActionsTitle} subtitle={text.live.driverActionsSubtitle}>
        <View style={styles.actions}>
          <PrimaryButton label={text.live.manualLapTrigger} tone="accent" onPress={() => onNavigate("summary")} />
          <PrimaryButton label={text.live.openSummary} tone="soft" onPress={() => onNavigate("summary")} />
        </View>
      </SectionCard>
    </>
  );
}

const styles = StyleSheet.create({
  grid: {
    gap: theme.spacing.md,
  },
  statusBlock: {
    gap: theme.spacing.sm,
  },
  statusChip: {
    alignSelf: "flex-start",
    backgroundColor: theme.colors.accent,
    borderRadius: 999,
    paddingHorizontal: theme.spacing.md,
    paddingVertical: theme.spacing.xs,
  },
  statusChipText: {
    color: theme.colors.textOnDark,
    fontSize: 13,
    fontWeight: "800",
    letterSpacing: 1,
  },
  statusText: {
    color: theme.colors.textStrong,
    fontSize: 20,
    fontWeight: "800",
  },
  statusMeta: {
    color: theme.colors.textMuted,
    fontSize: 14,
    lineHeight: 20,
  },
  actions: {
    gap: theme.spacing.sm,
  },
});
