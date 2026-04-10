import { Pressable, StyleSheet, Text, View } from "react-native";

import { ScreenId } from "../types";
import { theme } from "../theme";

type BottomNavProps = {
  activeScreen: ScreenId;
  labels: Record<ScreenId, string>;
  compact?: boolean;
  onNavigate: (screen: ScreenId) => void;
};

const tabs: ScreenId[] = ["home", "setup", "live", "summary"];

export function BottomNav({ activeScreen, labels, compact = false, onNavigate }: BottomNavProps) {
  return (
    <View style={[styles.wrap, compact && styles.wrapCompact]}>
      <View style={[styles.nav, compact && styles.navCompact]}>
        {tabs.map((tab) => {
          const active = tab === activeScreen;

          return (
            <Pressable
              key={tab}
              onPress={() => onNavigate(tab)}
              style={[styles.item, compact && styles.itemCompact, active && styles.itemActive]}
            >
              <Text style={[styles.label, compact && styles.labelCompact, active && styles.labelActive]}>{labels[tab]}</Text>
            </Pressable>
          );
        })}
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  wrap: {
    position: "absolute",
    left: 0,
    right: 0,
    bottom: 0,
    paddingHorizontal: theme.spacing.lg,
    paddingBottom: theme.spacing.lg,
  },
  wrapCompact: {
    paddingHorizontal: theme.spacing.md,
    paddingBottom: theme.spacing.md,
  },
  nav: {
    flexDirection: "row",
    gap: theme.spacing.sm,
    backgroundColor: theme.colors.surface,
    borderRadius: 26,
    padding: theme.spacing.sm,
    shadowColor: "#000000",
    shadowOffset: { width: 0, height: 10 },
    shadowOpacity: 0.08,
    shadowRadius: 18,
    elevation: 6,
  },
  navCompact: {
    borderRadius: 22,
    padding: theme.spacing.xs,
  },
  item: {
    flex: 1,
    alignItems: "center",
    justifyContent: "center",
    minHeight: 52,
    borderRadius: 18,
  },
  itemCompact: {
    minHeight: 42,
    borderRadius: 14,
  },
  itemActive: {
    backgroundColor: theme.colors.ink,
  },
  label: {
    color: theme.colors.textMuted,
    fontSize: 14,
    fontWeight: "700",
  },
  labelCompact: {
    fontSize: 12,
  },
  labelActive: {
    color: theme.colors.textOnDark,
  },
});
