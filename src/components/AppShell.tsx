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
  return (
    <View style={styles.shell}>
      <View style={styles.topBar}>
        <Pressable onPress={onToggleLanguage} style={styles.languageButton}>
          <Text style={styles.languageText}>{languageToggleLabel}</Text>
        </Pressable>
      </View>
      <ScrollView contentContainerStyle={styles.content}>{children}</ScrollView>
      <BottomNav activeScreen={activeScreen} labels={navCopy} onNavigate={onNavigate} />
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
  languageButton: {
    backgroundColor: theme.colors.surface,
    borderRadius: 999,
    paddingHorizontal: theme.spacing.md,
    paddingVertical: theme.spacing.sm,
  },
  languageText: {
    color: theme.colors.textStrong,
    fontSize: 13,
    fontWeight: "800",
  },
  content: {
    flexGrow: 1,
    paddingHorizontal: theme.spacing.lg,
    paddingTop: theme.spacing.md,
    paddingBottom: 120,
    gap: theme.spacing.lg,
  },
});
