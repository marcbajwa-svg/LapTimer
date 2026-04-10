import { StyleSheet, Text, View } from "react-native";

import { PrimaryButton } from "../components/PrimaryButton";
import { ScreenHeader } from "../components/ScreenHeader";
import { SectionCard } from "../components/SectionCard";
import { StatCard } from "../components/StatCard";
import { copy } from "../i18n";
import { Locale, SessionPreview, ScreenId } from "../types";
import { theme } from "../theme";

type HomeScreenProps = {
  locale: Locale;
  session: SessionPreview;
  onNavigate: (screen: ScreenId) => void;
};

export function HomeScreen({ locale, session, onNavigate }: HomeScreenProps) {
  const text = copy[locale];

  return (
    <>
      <ScreenHeader eyebrow={text.home.eyebrow} title={text.home.title} subtitle={text.home.subtitle} />

      <View style={styles.grid}>
        <StatCard label={text.common.track} value={session.trackName} tone="sand" />
        <StatCard label={text.common.mode} value={session.mode} tone="clay" />
        <StatCard label={text.common.gps} value={session.gpsStatus} tone="mint" />
      </View>

      <SectionCard title={text.home.sessionFlowTitle} subtitle={text.home.sessionFlowSubtitle}>
        <View style={styles.list}>
          {session.flow.map((step) => (
            <View key={step} style={styles.item}>
              <View style={styles.dot} />
              <Text style={styles.itemText}>{step}</Text>
            </View>
          ))}
        </View>
      </SectionCard>

      <SectionCard title={text.home.quickActionsTitle} subtitle={text.home.quickActionsSubtitle}>
        <View style={styles.actions}>
          <PrimaryButton label={text.home.configureTrack} onPress={() => onNavigate("setup")} />
          <PrimaryButton label={text.home.openLiveTimer} tone="accent" onPress={() => onNavigate("live")} />
          <PrimaryButton label={text.home.reviewSummary} tone="soft" onPress={() => onNavigate("summary")} />
        </View>
      </SectionCard>
    </>
  );
}

const styles = StyleSheet.create({
  grid: {
    gap: theme.spacing.md,
  },
  list: {
    gap: theme.spacing.md,
  },
  item: {
    flexDirection: "row",
    alignItems: "flex-start",
    gap: theme.spacing.sm,
  },
  dot: {
    width: 10,
    height: 10,
    borderRadius: 999,
    marginTop: 6,
    backgroundColor: theme.colors.ink,
  },
  itemText: {
    flex: 1,
    color: theme.colors.textStrong,
    fontSize: 15,
    lineHeight: 23,
  },
  actions: {
    gap: theme.spacing.sm,
  },
});
