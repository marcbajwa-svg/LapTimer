import { Pressable, StyleSheet, Text, View } from "react-native";

import { copy } from "../i18n";
import { Locale, ScreenId, SessionPreview, SessionStatus } from "../types";
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
  const primaryAction = sessionStatus === "paused" ? onPauseSession : onStartSession;
  const primaryDisabled = sessionStatus === "running";
  const statusLabel = getStatusLabel(text.live, sessionStatus);

  return (
    <View style={styles.screen}>
      <View style={styles.topRail}>
        <View style={styles.sessionBadge}>
          <Text style={styles.sessionBadgeText}>{statusLabel}</Text>
        </View>
        <View style={styles.metaCard}>
          <Text style={styles.metaLabel}>{text.common.track}</Text>
          <Text style={styles.metaValue} numberOfLines={1}>
            {session.trackName}
          </Text>
        </View>
        <View style={styles.metaCard}>
          <Text style={styles.metaLabel}>{text.common.gps}</Text>
          <Text style={styles.metaValue} numberOfLines={1}>
            {session.gpsStatus}
          </Text>
          <Text style={styles.metaHint}>
            {text.common.estimatedAccuracy}: {session.accuracy}
          </Text>
        </View>
      </View>

      <View style={styles.mainBoard}>
        <View style={styles.heroPanel}>
          <Text style={styles.heroLabel}>{text.common.currentLap}</Text>
          <Text style={styles.heroValue} numberOfLines={1} adjustsFontSizeToFit minimumFontScale={0.55}>
            {session.currentLap}
          </Text>
          <Text style={styles.heroHint}>
            {text.common.direction}: {session.mode}
          </Text>
        </View>

        <View style={styles.sideMetrics}>
          <View style={[styles.metricCard, styles.metricWarm]}>
            <Text style={styles.metricLabel}>{text.common.lastLap}</Text>
            <Text style={styles.metricValue}>{session.lastLap}</Text>
          </View>
          <View style={[styles.metricCard, styles.metricCool]}>
            <Text style={styles.metricLabel}>{text.common.bestLap}</Text>
            <Text style={styles.metricValue}>{session.bestLap}</Text>
          </View>
          <View style={[styles.metricCard, styles.metricSand]}>
            <Text style={styles.metricLabel}>{text.common.sessionTime}</Text>
            <Text style={styles.metricValue}>{session.sessionTime}</Text>
          </View>
          <View style={[styles.metricCard, styles.metricSoft]}>
            <Text style={styles.metricLabel}>{text.common.totalLaps}</Text>
            <Text style={styles.metricValue}>{String(session.totalLaps)}</Text>
          </View>
        </View>
      </View>

      <View style={styles.actionDeck}>
        <Pressable
          disabled={primaryDisabled}
          onPress={primaryAction}
          style={[styles.actionButton, styles.primaryAction, primaryDisabled && styles.disabled]}
        >
          <Text style={styles.primaryActionLabel}>{primaryLabel}</Text>
        </Pressable>

        <Pressable
          disabled={!canTriggerLap}
          onPress={onManualLap}
          style={[styles.actionButton, styles.lapAction, !canTriggerLap && styles.disabled]}
        >
          <Text style={styles.lapActionLabel}>{text.live.manualLapTrigger}</Text>
        </Pressable>

        <Pressable
          disabled={sessionStatus !== "running"}
          onPress={onPauseSession}
          style={[styles.actionButton, styles.secondaryAction, sessionStatus !== "running" && styles.disabled]}
        >
          <Text style={styles.secondaryActionLabel}>{text.live.pauseSession}</Text>
        </Pressable>

        <Pressable
          disabled={sessionStatus === "idle"}
          onPress={onEndSession}
          style={[styles.actionButton, styles.secondaryAction, sessionStatus === "idle" && styles.disabled]}
        >
          <Text style={styles.secondaryActionLabel}>{text.live.endSession}</Text>
        </Pressable>
      </View>

      <Pressable onPress={() => onNavigate("summary")} style={styles.summaryLink}>
        <Text style={styles.summaryLinkText}>{text.live.openSummary}</Text>
      </Pressable>
    </View>
  );
}

function getStatusLabel(text: LiveSessionCopy, status: SessionStatus) {
  if (status === "running") {
    return text.statusRunning;
  }

  if (status === "paused") {
    return text.statusPaused;
  }

  if (status === "finished") {
    return text.statusFinished;
  }

  return text.startSession;
}

type LiveSessionCopy = (typeof copy)[Locale]["live"];

const styles = StyleSheet.create({
  screen: {
    flex: 1,
    gap: theme.spacing.md,
  },
  topRail: {
    flexDirection: "row",
    gap: theme.spacing.md,
    alignItems: "stretch",
  },
  sessionBadge: {
    minWidth: 150,
    borderRadius: 20,
    backgroundColor: theme.colors.ink,
    alignItems: "center",
    justifyContent: "center",
    paddingHorizontal: theme.spacing.lg,
    paddingVertical: theme.spacing.sm,
  },
  sessionBadgeText: {
    color: theme.colors.textOnDark,
    fontSize: 20,
    fontWeight: "900",
    textAlign: "center",
  },
  metaCard: {
    flex: 1,
    borderRadius: 20,
    backgroundColor: theme.colors.surface,
    paddingHorizontal: theme.spacing.lg,
    paddingVertical: theme.spacing.sm,
    justifyContent: "center",
    gap: 4,
  },
  metaLabel: {
    color: theme.colors.textMuted,
    fontSize: 13,
    fontWeight: "700",
    letterSpacing: 0.8,
    textTransform: "uppercase",
  },
  metaValue: {
    color: theme.colors.textStrong,
    fontSize: 20,
    fontWeight: "900",
  },
  metaHint: {
    color: theme.colors.textMuted,
    fontSize: 12,
    fontWeight: "600",
  },
  mainBoard: {
    flex: 1,
    flexDirection: "row",
    gap: theme.spacing.md,
    minHeight: 0,
  },
  heroPanel: {
    flex: 1.6,
    borderRadius: 26,
    backgroundColor: theme.colors.hero,
    paddingHorizontal: theme.spacing.xl,
    paddingVertical: theme.spacing.md,
    justifyContent: "space-between",
  },
  heroLabel: {
    color: theme.colors.heroAccent,
    fontSize: 16,
    fontWeight: "800",
    textTransform: "uppercase",
    letterSpacing: 1.2,
  },
  heroValue: {
    color: theme.colors.textStrong,
    fontSize: 82,
    lineHeight: 88,
    fontWeight: "900",
  },
  heroHint: {
    color: theme.colors.textMuted,
    fontSize: 16,
    fontWeight: "700",
  },
  sideMetrics: {
    flex: 1,
    gap: theme.spacing.md,
  },
  metricCard: {
    flex: 1,
    borderRadius: 20,
    paddingHorizontal: theme.spacing.lg,
    paddingVertical: theme.spacing.sm,
    justifyContent: "center",
    gap: 8,
  },
  metricWarm: {
    backgroundColor: theme.colors.clay,
  },
  metricCool: {
    backgroundColor: theme.colors.mint,
  },
  metricSand: {
    backgroundColor: theme.colors.sand,
  },
  metricSoft: {
    backgroundColor: theme.colors.panelSoft,
  },
  metricLabel: {
    color: theme.colors.textStrong,
    fontSize: 12,
    fontWeight: "800",
    textTransform: "uppercase",
    letterSpacing: 1,
  },
  metricValue: {
    color: theme.colors.textStrong,
    fontSize: 28,
    fontWeight: "900",
  },
  actionDeck: {
    flexDirection: "row",
    gap: theme.spacing.md,
  },
  actionButton: {
    flex: 1,
    minHeight: 72,
    borderRadius: 20,
    alignItems: "center",
    justifyContent: "center",
    paddingHorizontal: theme.spacing.md,
  },
  primaryAction: {
    backgroundColor: theme.colors.ink,
  },
  lapAction: {
    backgroundColor: theme.colors.accent,
  },
  secondaryAction: {
    backgroundColor: theme.colors.panelSoft,
  },
  primaryActionLabel: {
    color: theme.colors.textOnDark,
    fontSize: 20,
    fontWeight: "900",
    textAlign: "center",
  },
  lapActionLabel: {
    color: theme.colors.textOnDark,
    fontSize: 20,
    fontWeight: "900",
    textAlign: "center",
  },
  secondaryActionLabel: {
    color: theme.colors.textStrong,
    fontSize: 18,
    fontWeight: "900",
    textAlign: "center",
  },
  disabled: {
    opacity: 0.4,
  },
  summaryLink: {
    alignSelf: "flex-end",
    paddingHorizontal: theme.spacing.md,
    paddingVertical: theme.spacing.xs,
  },
  summaryLinkText: {
    color: theme.colors.heroAccent,
    fontSize: 13,
    fontWeight: "800",
  },
});
