import { Pressable, StyleSheet, Text, TextStyle, View, ViewStyle, useWindowDimensions } from "react-native";

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
  const { height, width } = useWindowDimensions();
  const compactHeight = height < 430;

  if (sessionStatus === "running") {
    return (
      <DrivingModeScreen
        compactHeight={compactHeight}
        locale={locale}
        session={session}
        onPauseSession={onPauseSession}
        onEndSession={onEndSession}
        onManualLap={onManualLap}
      />
    );
  }

  const primaryLabel = sessionStatus === "paused" ? text.live.resumeSession : text.live.startSession;
  const primaryAction = sessionStatus === "paused" ? onPauseSession : onStartSession;
  const primaryDisabled = false;
  const statusLabel = getStatusLabel(text.live, sessionStatus);
  const heroValueSize = compactHeight ? 58 : width < 780 ? 64 : 74;
  const heroLineHeight = heroValueSize + 6;
  const metricValueSize = compactHeight ? 22 : 26;
  const actionLabelSize = compactHeight ? 15 : 17;

  return (
    <View style={styles.screen}>
      <View style={[styles.topRail, compactHeight && styles.topRailCompact]}>
        <View style={[styles.sessionBadge, compactHeight && styles.sessionBadgeCompact]}>
          <Text style={[styles.sessionBadgeText, compactHeight && styles.sessionBadgeTextCompact]}>{statusLabel}</Text>
        </View>

        <View style={[styles.metaCard, compactHeight && styles.metaCardCompact]}>
          <Text style={styles.metaLabel}>{text.common.track}</Text>
          <Text style={[styles.metaValue, compactHeight && styles.metaValueCompact]} numberOfLines={1}>
            {session.trackName}
          </Text>
        </View>

        <View style={[styles.metaCard, compactHeight && styles.metaCardCompact]}>
          <Text style={styles.metaLabel}>{text.common.gps}</Text>
          <Text style={[styles.metaValue, compactHeight && styles.metaValueCompact]} numberOfLines={1}>
            {session.gpsStatus}
          </Text>
          <Text style={[styles.metaHint, compactHeight && styles.metaHintCompact]}>
            {text.common.estimatedAccuracy}: {session.accuracy}
          </Text>
        </View>
      </View>

      <View style={[styles.mainBoard, compactHeight && styles.mainBoardCompact]}>
        <View style={[styles.heroPanel, compactHeight && styles.heroPanelCompact]}>
          <Text style={[styles.heroLabel, compactHeight && styles.heroLabelCompact]}>{text.common.currentLap}</Text>
          <Text
            style={[styles.heroValue, { fontSize: heroValueSize, lineHeight: heroLineHeight }]}
            numberOfLines={1}
            adjustsFontSizeToFit
            minimumFontScale={0.55}
          >
            {session.currentLap}
          </Text>
          <Text style={[styles.heroHint, compactHeight && styles.heroHintCompact]} numberOfLines={1}>
            {text.common.direction}: {session.mode}
          </Text>
        </View>

        <View style={styles.metricsGrid}>
          <MetricCard label={text.common.lastLap} value={session.lastLap} tone="warm" valueSize={metricValueSize} compact={compactHeight} />
          <MetricCard label={text.common.bestLap} value={session.bestLap} tone="cool" valueSize={metricValueSize} compact={compactHeight} />
          <MetricCard label={text.common.deltaToBest} value={session.currentDelta} tone="sand" valueSize={metricValueSize} compact={compactHeight} />
          <MetricCard
            label={text.common.sessionTime}
            value={session.sessionTime}
            tone="soft"
            valueSize={metricValueSize}
            compact={compactHeight}
          />
        </View>
      </View>

      <View style={[styles.actionGrid, compactHeight && styles.actionGridCompact]}>
        <ActionButton
          label={primaryLabel}
          onPress={primaryAction}
          disabled={primaryDisabled}
          tone="primary"
          compact={compactHeight}
          labelSize={actionLabelSize}
        />
        <ActionButton
          label={text.live.manualLapTrigger}
          onPress={onManualLap}
          disabled
          tone="lap"
          compact={compactHeight}
          labelSize={actionLabelSize}
        />
        <ActionButton
          label={text.live.pauseSession}
          onPress={onPauseSession}
          disabled={sessionStatus !== "paused"}
          tone="secondary"
          compact={compactHeight}
          labelSize={actionLabelSize}
        />
        <ActionButton
          label={text.live.endSession}
          onPress={onEndSession}
          disabled={sessionStatus === "idle"}
          tone="secondary"
          compact={compactHeight}
          labelSize={actionLabelSize}
        />
      </View>

      <Pressable onPress={() => onNavigate("summary")} style={styles.summaryLink}>
        <Text style={[styles.summaryLinkText, compactHeight && styles.summaryLinkTextCompact]}>{text.live.openSummary}</Text>
      </Pressable>
    </View>
  );
}

type DrivingModeScreenProps = {
  compactHeight: boolean;
  locale: Locale;
  session: SessionPreview;
  onPauseSession: () => void;
  onEndSession: () => void;
  onManualLap: () => void;
};

function DrivingModeScreen({
  compactHeight,
  locale,
  session,
  onPauseSession,
  onEndSession,
  onManualLap,
}: DrivingModeScreenProps) {
  const text = copy[locale];
  const deltaPositive = session.currentDelta.startsWith("+");
  const deltaNeutral = session.currentDelta === "--";

  return (
    <View style={styles.driveScreen}>
      <View style={[styles.driveHeader, compactHeight && styles.driveHeaderCompact]}>
        <View style={styles.driveHeaderLeft}>
          <Text style={styles.driveMetaLabel}>{text.common.track}</Text>
          <Text style={[styles.driveMetaValue, compactHeight && styles.driveMetaValueCompact]} numberOfLines={1}>
            {session.trackName}
          </Text>
        </View>
        <View style={styles.driveHeaderRight}>
          <Text style={styles.driveMetaLabel}>{text.common.gps}</Text>
          <Text style={[styles.driveMetaValue, compactHeight && styles.driveMetaValueCompact]} numberOfLines={1}>
            {session.accuracy}
          </Text>
        </View>
      </View>

      <View style={[styles.driveMain, compactHeight && styles.driveMainCompact]}>
        <View style={styles.driveHero}>
          <Text style={[styles.driveHeroLabel, compactHeight && styles.driveHeroLabelCompact]}>{text.common.currentLap}</Text>
          <Text
            style={[styles.driveHeroValue, compactHeight && styles.driveHeroValueCompact]}
            numberOfLines={1}
            adjustsFontSizeToFit
            minimumFontScale={0.5}
          >
            {session.currentLap}
          </Text>
        </View>

        <View
          style={[
            styles.deltaPanel,
            deltaPositive ? styles.deltaPanelHot : styles.deltaPanelCool,
            deltaNeutral && styles.deltaPanelNeutral,
            compactHeight && styles.deltaPanelCompact,
          ]}
        >
          <Text style={[styles.deltaLabel, compactHeight && styles.deltaLabelCompact]}>{text.common.deltaToBest}</Text>
          <Text
            style={[styles.deltaValue, compactHeight && styles.deltaValueCompact]}
            numberOfLines={1}
            adjustsFontSizeToFit
            minimumFontScale={0.55}
          >
            {session.currentDelta}
          </Text>
          <Text style={[styles.deltaHint, compactHeight && styles.deltaHintCompact]}>
            {text.common.bestLap}: {session.bestLap}
          </Text>
        </View>
      </View>

      <View style={[styles.driveStatsRow, compactHeight && styles.driveStatsRowCompact]}>
        <MetricCard label={text.common.lastLap} value={session.lastLap} tone="warm" valueSize={compactHeight ? 20 : 24} compact={compactHeight} />
        <MetricCard label={text.common.bestLap} value={session.bestLap} tone="cool" valueSize={compactHeight ? 20 : 24} compact={compactHeight} />
        <MetricCard label={text.common.sessionTime} value={session.sessionTime} tone="soft" valueSize={compactHeight ? 20 : 24} compact={compactHeight} />
      </View>

      <View style={[styles.driveActions, compactHeight && styles.driveActionsCompact]}>
        <ActionButton
          label={text.live.manualLapTrigger}
          onPress={onManualLap}
          disabled={false}
          tone="lap"
          compact={compactHeight}
          labelSize={compactHeight ? 16 : 18}
        />
        <ActionButton
          label={text.live.pauseSession}
          onPress={onPauseSession}
          disabled={false}
          tone="secondary"
          compact={compactHeight}
          labelSize={compactHeight ? 16 : 18}
        />
        <ActionButton
          label={text.live.endSession}
          onPress={onEndSession}
          disabled={false}
          tone="secondary"
          compact={compactHeight}
          labelSize={compactHeight ? 16 : 18}
        />
      </View>
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
type MetricTone = "warm" | "cool" | "sand" | "soft";
type ActionTone = "primary" | "lap" | "secondary";

type MetricCardProps = {
  label: string;
  value: string;
  tone: MetricTone;
  valueSize: number;
  compact: boolean;
};

function MetricCard({ label, value, tone, valueSize, compact }: MetricCardProps) {
  return (
    <View style={[styles.metricCard, metricToneStyles[tone], compact && styles.metricCardCompact]}>
      <Text style={[styles.metricLabel, compact && styles.metricLabelCompact]}>{label}</Text>
      <Text style={[styles.metricValue, { fontSize: valueSize }, compact && styles.metricValueCompact]} numberOfLines={1}>
        {value}
      </Text>
    </View>
  );
}

type ActionButtonProps = {
  label: string;
  onPress: () => void;
  disabled: boolean;
  tone: ActionTone;
  compact: boolean;
  labelSize: number;
};

function ActionButton({ label, onPress, disabled, tone, compact, labelSize }: ActionButtonProps) {
  return (
    <Pressable
      disabled={disabled}
      onPress={onPress}
      style={[styles.actionButton, actionToneStyles[tone].button, compact && styles.actionButtonCompact, disabled && styles.disabled]}
    >
      <Text style={[styles.actionLabel, actionToneStyles[tone].label, { fontSize: labelSize }, compact && styles.actionLabelCompact]}>
        {label}
      </Text>
    </Pressable>
  );
}

const metricToneStyles = StyleSheet.create({
  warm: {
    backgroundColor: theme.colors.clay,
  },
  cool: {
    backgroundColor: theme.colors.mint,
  },
  sand: {
    backgroundColor: theme.colors.sand,
  },
  soft: {
    backgroundColor: theme.colors.panelSoft,
  },
});

const actionToneStyles: Record<ActionTone, { button: ViewStyle; label: TextStyle }> = {
  primary: {
    button: {
      backgroundColor: theme.colors.ink,
    },
    label: {
      color: theme.colors.textOnDark,
    },
  },
  lap: {
    button: {
      backgroundColor: theme.colors.accent,
    },
    label: {
      color: theme.colors.textOnDark,
    },
  },
  secondary: {
    button: {
      backgroundColor: theme.colors.panelSoft,
    },
    label: {
      color: theme.colors.textStrong,
    },
  },
};

const styles = StyleSheet.create({
  screen: {
    flex: 1,
    gap: theme.spacing.sm,
  },
  topRail: {
    flexDirection: "row",
    gap: theme.spacing.sm,
    alignItems: "stretch",
  },
  topRailCompact: {
    gap: theme.spacing.xs,
  },
  sessionBadge: {
    flex: 0.9,
    minWidth: 120,
    borderRadius: 18,
    backgroundColor: theme.colors.ink,
    alignItems: "center",
    justifyContent: "center",
    paddingHorizontal: theme.spacing.md,
    paddingVertical: theme.spacing.sm,
  },
  sessionBadgeCompact: {
    borderRadius: 16,
    paddingHorizontal: theme.spacing.sm,
    paddingVertical: theme.spacing.xs,
  },
  sessionBadgeText: {
    color: theme.colors.textOnDark,
    fontSize: 18,
    fontWeight: "900",
    textAlign: "center",
  },
  sessionBadgeTextCompact: {
    fontSize: 15,
  },
  metaCard: {
    flex: 1.4,
    borderRadius: 18,
    backgroundColor: theme.colors.surface,
    paddingHorizontal: theme.spacing.md,
    paddingVertical: theme.spacing.sm,
    justifyContent: "center",
    gap: 2,
  },
  metaCardCompact: {
    borderRadius: 16,
    paddingHorizontal: theme.spacing.sm,
    paddingVertical: theme.spacing.xs,
  },
  metaLabel: {
    color: theme.colors.textMuted,
    fontSize: 11,
    fontWeight: "800",
    letterSpacing: 0.8,
    textTransform: "uppercase",
  },
  metaValue: {
    color: theme.colors.textStrong,
    fontSize: 18,
    fontWeight: "900",
  },
  metaValueCompact: {
    fontSize: 15,
  },
  metaHint: {
    color: theme.colors.textMuted,
    fontSize: 11,
    fontWeight: "600",
  },
  metaHintCompact: {
    fontSize: 10,
  },
  mainBoard: {
    flex: 1,
    flexDirection: "row",
    gap: theme.spacing.sm,
    minHeight: 0,
  },
  mainBoardCompact: {
    gap: theme.spacing.xs,
  },
  heroPanel: {
    flex: 1.3,
    borderRadius: 22,
    backgroundColor: theme.colors.hero,
    paddingHorizontal: theme.spacing.lg,
    paddingVertical: theme.spacing.md,
    justifyContent: "space-between",
    minHeight: 0,
  },
  heroPanelCompact: {
    borderRadius: 18,
    paddingHorizontal: theme.spacing.md,
    paddingVertical: theme.spacing.sm,
  },
  heroLabel: {
    color: theme.colors.heroAccent,
    fontSize: 14,
    fontWeight: "800",
    textTransform: "uppercase",
    letterSpacing: 1,
  },
  heroLabelCompact: {
    fontSize: 12,
  },
  heroValue: {
    color: theme.colors.textStrong,
    fontWeight: "900",
  },
  heroHint: {
    color: theme.colors.textMuted,
    fontSize: 15,
    fontWeight: "700",
  },
  heroHintCompact: {
    fontSize: 12,
  },
  metricsGrid: {
    flex: 1,
    flexDirection: "row",
    flexWrap: "wrap",
    gap: theme.spacing.sm,
    alignContent: "space-between",
  },
  metricCard: {
    width: "48%",
    flexGrow: 1,
    borderRadius: 18,
    paddingHorizontal: theme.spacing.md,
    paddingVertical: theme.spacing.sm,
    justifyContent: "center",
    minHeight: 0,
  },
  metricCardCompact: {
    borderRadius: 16,
    paddingHorizontal: theme.spacing.sm,
    paddingVertical: theme.spacing.xs,
  },
  metricLabel: {
    color: theme.colors.textStrong,
    fontSize: 11,
    fontWeight: "800",
    textTransform: "uppercase",
    letterSpacing: 0.8,
    marginBottom: 4,
  },
  metricLabelCompact: {
    fontSize: 10,
    marginBottom: 2,
  },
  metricValue: {
    color: theme.colors.textStrong,
    fontWeight: "900",
  },
  metricValueCompact: {
    lineHeight: 24,
  },
  actionGrid: {
    flexDirection: "row",
    gap: theme.spacing.sm,
  },
  actionGridCompact: {
    gap: theme.spacing.xs,
  },
  actionButton: {
    flex: 1,
    minHeight: 58,
    borderRadius: 18,
    alignItems: "center",
    justifyContent: "center",
    paddingHorizontal: theme.spacing.sm,
  },
  actionButtonCompact: {
    minHeight: 50,
    borderRadius: 16,
  },
  actionLabel: {
    fontWeight: "900",
    textAlign: "center",
  },
  actionLabelCompact: {
    lineHeight: 18,
  },
  disabled: {
    opacity: 0.4,
  },
  summaryLink: {
    alignSelf: "flex-end",
    paddingHorizontal: theme.spacing.sm,
    paddingVertical: 2,
  },
  summaryLinkText: {
    color: theme.colors.heroAccent,
    fontSize: 12,
    fontWeight: "800",
  },
  summaryLinkTextCompact: {
    fontSize: 11,
  },
  driveScreen: {
    flex: 1,
    gap: theme.spacing.sm,
  },
  driveHeader: {
    flexDirection: "row",
    justifyContent: "space-between",
    gap: theme.spacing.sm,
    alignItems: "center",
  },
  driveHeaderCompact: {
    gap: theme.spacing.xs,
  },
  driveHeaderLeft: {
    flex: 1.4,
    minWidth: 0,
  },
  driveHeaderRight: {
    flex: 0.8,
    minWidth: 0,
    alignItems: "flex-end",
  },
  driveMetaLabel: {
    color: theme.colors.textMuted,
    fontSize: 11,
    fontWeight: "800",
    letterSpacing: 0.8,
    textTransform: "uppercase",
  },
  driveMetaValue: {
    color: theme.colors.textStrong,
    fontSize: 18,
    fontWeight: "900",
  },
  driveMetaValueCompact: {
    fontSize: 15,
  },
  driveMain: {
    flex: 1,
    flexDirection: "row",
    gap: theme.spacing.sm,
    minHeight: 0,
  },
  driveMainCompact: {
    gap: theme.spacing.xs,
  },
  driveHero: {
    flex: 1.25,
    borderRadius: 24,
    backgroundColor: theme.colors.ink,
    paddingHorizontal: theme.spacing.lg,
    paddingVertical: theme.spacing.md,
    justifyContent: "space-between",
  },
  driveHeroLabel: {
    color: theme.colors.sand,
    fontSize: 14,
    fontWeight: "800",
    letterSpacing: 1,
    textTransform: "uppercase",
  },
  driveHeroLabelCompact: {
    fontSize: 11,
  },
  driveHeroValue: {
    color: theme.colors.textOnDark,
    fontSize: 82,
    lineHeight: 88,
    fontWeight: "900",
  },
  driveHeroValueCompact: {
    fontSize: 64,
    lineHeight: 68,
  },
  deltaPanel: {
    flex: 1,
    borderRadius: 24,
    paddingHorizontal: theme.spacing.lg,
    paddingVertical: theme.spacing.md,
    justifyContent: "space-between",
  },
  deltaPanelCompact: {
    borderRadius: 18,
    paddingHorizontal: theme.spacing.md,
    paddingVertical: theme.spacing.sm,
  },
  deltaPanelHot: {
    backgroundColor: "#f2c5b5",
  },
  deltaPanelCool: {
    backgroundColor: "#cfe6d4",
  },
  deltaPanelNeutral: {
    backgroundColor: theme.colors.panelSoft,
  },
  deltaLabel: {
    color: theme.colors.textMuted,
    fontSize: 14,
    fontWeight: "800",
    letterSpacing: 1,
    textTransform: "uppercase",
  },
  deltaLabelCompact: {
    fontSize: 11,
  },
  deltaValue: {
    color: theme.colors.textStrong,
    fontSize: 62,
    lineHeight: 66,
    fontWeight: "900",
  },
  deltaValueCompact: {
    fontSize: 46,
    lineHeight: 50,
  },
  deltaHint: {
    color: theme.colors.textMuted,
    fontSize: 15,
    fontWeight: "700",
  },
  deltaHintCompact: {
    fontSize: 12,
  },
  driveStatsRow: {
    flexDirection: "row",
    gap: theme.spacing.sm,
  },
  driveStatsRowCompact: {
    gap: theme.spacing.xs,
  },
  driveActions: {
    flexDirection: "row",
    gap: theme.spacing.sm,
  },
  driveActionsCompact: {
    gap: theme.spacing.xs,
  },
});
