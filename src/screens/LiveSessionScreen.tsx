import { StyleSheet, Text, View } from "react-native";

import { PrimaryButton } from "../components/PrimaryButton";
import { ScreenHeader } from "../components/ScreenHeader";
import { SectionCard } from "../components/SectionCard";
import { StatCard } from "../components/StatCard";
import { copy } from "../i18n";
import { Locale, SessionPreview, ScreenId, SessionStatus } from "../types";
import { theme } from "../theme";

type LiveSessionScreenProps = {
  locale: Locale;
  session: SessionPreview;
  sessionStatus: SessionStatus;
  onNavigate: (screen: ScreenId) => void;
  onStartSession: () => void;
  onPauseSession: () => void;
  onEndSession: () => void;
  onManualLap: () => void;
};

export function LiveSessionScreen({
  locale,
  session,
  sessionStatus,
  onNavigate,
  onStartSession,
  onPauseSession,
  onEndSession,
  onManualLap,
}: LiveSessionScreenProps) {
  const text = copy[locale];
  const canTriggerLap = sessionStatus === "running";
  const primaryLabel = sessionStatus === "paused" ? text.live.resumeSession : text.live.startSession;
  const primaryDisabled = sessionStatus === "running";

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
          <PrimaryButton
            label={primaryLabel}
            disabled={primaryDisabled}
            onPress={sessionStatus === "paused" ? onPauseSession : onStartSession}
          />
          <PrimaryButton
            label={text.live.pauseSession}
            tone="soft"
            disabled={sessionStatus !== "running"}
            onPress={onPauseSession}
          />
          <PrimaryButton
            label={text.live.manualLapTrigger}
            tone="accent"
            disabled={!canTriggerLap}
            onPress={onManualLap}
          />
          <PrimaryButton label={text.live.endSession} tone="soft" disabled={sessionStatus === "idle"} onPress={onEndSession} />
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
