import { PropsWithChildren } from "react";
import { Pressable, ScrollView, StyleSheet, Text, View } from "react-native";

import { BottomNav } from "./BottomNav";
import { ScreenId } from "../types";
import { theme } from "../theme";

type AppShellProps = PropsWithChildren<{
  activeScreen: ScreenId;
  navCopy: Record<ScreenId, string>;
  languageToggleLabel: string;
  onNavigate: (screen: ScreenId) => void;
  onToggleLanguage: () => void;
}>;

export function AppShell({
  activeScreen,
  navCopy,
  languageToggleLabel,
  onNavigate,
  onToggleLanguage,
  children,
}: AppShellProps) {
  const compactLiveLayout = activeScreen === "live";
  const Content = compactLiveLayout ? View : ScrollView;

  return (
    <View style={styles.shell}>
      <View style={[styles.topBar, compactLiveLayout && styles.topBarCompact]}>
        <Pressable onPress={onToggleLanguage} style={[styles.languageButton, compactLiveLayout && styles.languageButtonCompact]}>
          <Text style={styles.languageText}>{languageToggleLabel}</Text>
        </Pressable>
      </View>
      <Content
        {...(!compactLiveLayout ? { contentContainerStyle: styles.content } : {})}
        style={compactLiveLayout ? styles.liveContent : undefined}
      >
        <View style={[styles.content, compactLiveLayout && styles.contentCompact]}>{children}</View>
      </Content>
      <BottomNav activeScreen={activeScreen} labels={navCopy} compact={compactLiveLayout} onNavigate={onNavigate} />
    </View>
  );
}

const styles = StyleSheet.create({
  shell: {
    flex: 1,
  },
  topBar: {
    paddingHorizontal: theme.spacing.lg,
    paddingTop: theme.spacing.md,
    alignItems: "flex-end",
  },
  topBarCompact: {
    paddingHorizontal: theme.spacing.md,
    paddingTop: theme.spacing.xs,
  },
  languageButton: {
    backgroundColor: theme.colors.surface,
    borderRadius: 999,
    paddingHorizontal: theme.spacing.md,
    paddingVertical: theme.spacing.sm,
  },
  languageButtonCompact: {
    paddingHorizontal: theme.spacing.sm,
    paddingVertical: theme.spacing.xs,
  },
  languageText: {
    color: theme.colors.textStrong,
    fontSize: 13,
    fontWeight: "800",
  },
  liveContent: {
    flex: 1,
  },
  content: {
    flexGrow: 1,
    paddingHorizontal: theme.spacing.lg,
    paddingTop: theme.spacing.md,
    paddingBottom: 120,
    gap: theme.spacing.lg,
  },
  contentCompact: {
    flex: 1,
    paddingHorizontal: theme.spacing.md,
    paddingTop: theme.spacing.xs,
    paddingBottom: 76,
    gap: theme.spacing.md,
  },
});
