import { Pressable, StyleSheet, Text, View } from "react-native";

import { InfoRow } from "../components/InfoRow";
import { PrimaryButton } from "../components/PrimaryButton";
import { ScreenHeader } from "../components/ScreenHeader";
import { SectionCard } from "../components/SectionCard";
import { copy } from "../i18n";
import { Locale, PermissionState, SessionPreview, ScreenId, TrackDefinition } from "../types";
import { theme } from "../theme";
import { formatTrackDirection } from "../utils/session";

type TrackSetupScreenProps = {
  locale: Locale;
  session: SessionPreview;
  selectedTrack: TrackDefinition;
  permissionState: PermissionState;
  currentPositionLabel: string;
  presetTracks: TrackDefinition[];
  nearbyTrack: TrackDefinition | null;
  nearbyTrackDistance: string | null;
  onNavigate: (screen: ScreenId) => void;
  onRequestLocationPermission: () => void;
  onSelectTrack: (track: TrackDefinition) => void;
  onUseCurrentPosition: () => void;
};

export function TrackSetupScreen({
  locale,
  session,
  selectedTrack,
  permissionState,
  currentPositionLabel,
  presetTracks,
  nearbyTrack,
  nearbyTrackDistance,
  onNavigate,
  onRequestLocationPermission,
  onSelectTrack,
  onUseCurrentPosition,
}: TrackSetupScreenProps) {
  const text = copy[locale];
  const permissionLabel =
    permissionState === "granted"
      ? text.setup.locationGranted
      : permissionState === "denied"
        ? text.setup.locationDenied
        : text.setup.allowLocation;

  return (
    <>
      <ScreenHeader eyebrow={text.setup.eyebrow} title={text.setup.title} subtitle={text.setup.subtitle} />

      <SectionCard title={text.setup.locationPermissionTitle} subtitle={text.setup.locationPermissionSubtitle}>
        <InfoRow label={text.common.gps} value={permissionLabel} />
        <InfoRow label={text.setup.currentPositionLabel} value={currentPositionLabel} multiline />
        <PrimaryButton
          label={text.setup.allowLocation}
          tone="accent"
          disabled={permissionState === "granted"}
          onPress={onRequestLocationPermission}
        />
      </SectionCard>

      <SectionCard title={text.setup.nearbyTrackTitle} subtitle={text.setup.nearbyTrackSubtitle}>
        {nearbyTrack ? (
          <View style={styles.suggestionBlock}>
            <View style={styles.suggestionCard}>
              <Text style={styles.trackName}>{nearbyTrack.name[locale]}</Text>
              <Text style={styles.trackMeta}>{nearbyTrack.markerLabel[locale]}</Text>
              <Text style={styles.trackMeta}>
                {text.setup.nearbyTrackDistance}: {nearbyTrackDistance}
              </Text>
            </View>
            <PrimaryButton label={text.setup.useSuggestedTrack} tone="accent" onPress={() => onSelectTrack(nearbyTrack)} />
            <PrimaryButton
              label={text.setup.setManualStartPoint}
              tone="soft"
              disabled={permissionState !== "granted" || currentPositionLabel === "--"}
              onPress={onUseCurrentPosition}
            />
          </View>
        ) : (
          <View style={styles.suggestionBlock}>
            <Text style={styles.emptyState}>{text.setup.nearbyTrackNone}</Text>
            <PrimaryButton
              label={text.setup.setManualStartPoint}
              tone="soft"
              disabled={permissionState !== "granted" || currentPositionLabel === "--"}
              onPress={onUseCurrentPosition}
            />
          </View>
        )}
      </SectionCard>

      <SectionCard title={text.setup.trackLibraryTitle} subtitle={text.setup.trackLibrarySubtitle}>
        <View style={styles.trackList}>
          {presetTracks.map((track) => {
            const active = track.id === selectedTrack.id;
            return (
              <Pressable
                key={track.id}
                onPress={() => onSelectTrack(track)}
                style={[styles.trackCard, active && styles.trackCardActive]}
              >
                <Text style={styles.trackName}>{track.name[locale]}</Text>
                <Text style={styles.trackMeta}>{track.markerLabel[locale]}</Text>
                <Text style={styles.trackMeta}>{formatTrackDirection(locale, track.direction)}</Text>
              </Pressable>
            );
          })}
        </View>
      </SectionCard>

      <SectionCard title={text.setup.selectedTrackTitle} subtitle={text.setup.selectedTrackSubtitle}>
        <InfoRow label={text.common.track} value={selectedTrack.name[locale]} />
        <InfoRow label={text.common.marker} value={selectedTrack.markerLabel[locale]} multiline />
        <InfoRow label={text.common.direction} value={formatTrackDirection(locale, selectedTrack.direction)} />
        <InfoRow label={text.common.minimumLap} value={`00:${String(Math.floor(selectedTrack.minimumLapMs / 1000)).padStart(2, "0")}.00`} />
      </SectionCard>

      <SectionCard title={text.setup.startFinishTitle} subtitle={text.setup.startFinishSubtitle}>
        <InfoRow label={text.common.track} value={session.trackName} />
        <InfoRow label={text.common.marker} value={session.startLineLabel} multiline />
        <InfoRow label={text.common.direction} value={formatTrackDirection(locale, selectedTrack.direction)} />
        <InfoRow label={text.common.minimumLap} value="00:25.00" />
      </SectionCard>

      <SectionCard title={text.setup.checklistTitle} subtitle={text.setup.checklistSubtitle}>
        <View style={styles.list}>
          {session.setupChecklist.map((item) => (
            <View key={item} style={styles.row}>
              <View style={styles.dot} />
              <Text style={styles.text}>{item}</Text>
            </View>
          ))}
        </View>
      </SectionCard>

      <SectionCard title={text.setup.nextTitle} subtitle={text.setup.nextSubtitle}>
        <View style={styles.actions}>
          <PrimaryButton label={text.setup.previewLiveSession} onPress={() => onNavigate("live")} />
          <PrimaryButton label={text.setup.backHome} tone="soft" onPress={() => onNavigate("home")} />
        </View>
      </SectionCard>
    </>
  );
}

const styles = StyleSheet.create({
  list: {
    gap: theme.spacing.md,
  },
  row: {
    flexDirection: "row",
    alignItems: "flex-start",
    gap: theme.spacing.sm,
  },
  dot: {
    width: 10,
    height: 10,
    borderRadius: 999,
    marginTop: 6,
    backgroundColor: theme.colors.accent,
  },
  text: {
    flex: 1,
    color: theme.colors.textStrong,
    fontSize: 15,
    lineHeight: 23,
  },
  actions: {
    gap: theme.spacing.sm,
  },
  trackList: {
    gap: theme.spacing.sm,
  },
  suggestionBlock: {
    gap: theme.spacing.sm,
  },
  suggestionCard: {
    backgroundColor: theme.colors.panelSoft,
    borderRadius: 18,
    padding: theme.spacing.md,
    gap: theme.spacing.xs,
  },
  trackCard: {
    backgroundColor: theme.colors.panelSoft,
    borderRadius: 18,
    padding: theme.spacing.md,
    gap: theme.spacing.xs,
  },
  trackCardActive: {
    borderWidth: 2,
    borderColor: theme.colors.accent,
  },
  trackName: {
    color: theme.colors.textStrong,
    fontSize: 16,
    fontWeight: "800",
  },
  trackMeta: {
    color: theme.colors.textMuted,
    fontSize: 13,
    lineHeight: 20,
  },
  emptyState: {
    color: theme.colors.textStrong,
    fontSize: 15,
    lineHeight: 22,
  },
});
