import { StyleSheet, Text, View } from "react-native";

import { InfoRow } from "../components/InfoRow";
import { PrimaryButton } from "../components/PrimaryButton";
import { ScreenHeader } from "../components/ScreenHeader";
import { SectionCard } from "../components/SectionCard";
import { copy } from "../i18n";
import { Locale, SessionPreview, ScreenId } from "../types";
import { theme } from "../theme";

type TrackSetupScreenProps = {
  locale: Locale;
  session: SessionPreview;
  onNavigate: (screen: ScreenId) => void;
};

export function TrackSetupScreen({ locale, session, onNavigate }: TrackSetupScreenProps) {
  const text = copy[locale];

  return (
    <>
      <ScreenHeader eyebrow={text.setup.eyebrow} title={text.setup.title} subtitle={text.setup.subtitle} />

      <SectionCard title={text.setup.startFinishTitle} subtitle={text.setup.startFinishSubtitle}>
        <InfoRow label={text.common.track} value={session.trackName} />
        <InfoRow label={text.common.marker} value={session.startLineLabel} />
        <InfoRow label={text.common.direction} value={text.common.clockwise} />
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
});
