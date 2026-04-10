import { StyleSheet, Text, View } from "react-native";

import { LapList } from "../components/LapList";
import { PrimaryButton } from "../components/PrimaryButton";
import { ScreenHeader } from "../components/ScreenHeader";
import { SectionCard } from "../components/SectionCard";
import { StatCard } from "../components/StatCard";
import { copy } from "../i18n";
import { Locale, SessionPreview, ScreenId } from "../types";
import { theme } from "../theme";

type SessionSummaryScreenProps = {
  locale: Locale;
  session: SessionPreview;
  onNavigate: (screen: ScreenId) => void;
};

export function SessionSummaryScreen({ locale, session, onNavigate }: SessionSummaryScreenProps) {
  const text = copy[locale];

  return (
    <>
      <ScreenHeader eyebrow={text.summary.eyebrow} title={text.summary.title} subtitle={text.summary.subtitle} />

      <View style={styles.grid}>
        <StatCard label={text.common.bestLap} value={session.bestLap} tone="mint" />
        <StatCard label={text.common.totalLaps} value={String(session.totalLaps)} tone="sand" />
      </View>

      <SectionCard title={text.summary.reviewNotesTitle} subtitle={text.summary.reviewNotesSubtitle}>
        <View style={styles.notes}>
          {session.summaryNotes.map((note) => (
            <View key={note} style={styles.noteRow}>
              <View style={styles.dot} />
              <Text style={styles.noteText}>{note}</Text>
            </View>
          ))}
        </View>
      </SectionCard>

      <SectionCard title={text.summary.lapListTitle} subtitle={text.summary.lapListSubtitle}>
        <LapList laps={session.laps} locale={locale} />
      </SectionCard>

      <SectionCard title={text.summary.nextTitle} subtitle={text.summary.nextSubtitle}>
        <PrimaryButton label={text.summary.startNewSession} onPress={() => onNavigate("live")} />
      </SectionCard>
    </>
  );
}

const styles = StyleSheet.create({
  grid: {
    gap: theme.spacing.md,
  },
  notes: {
    gap: theme.spacing.md,
  },
  noteRow: {
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
  noteText: {
    flex: 1,
    color: theme.colors.textStrong,
    fontSize: 15,
    lineHeight: 23,
  },
});
