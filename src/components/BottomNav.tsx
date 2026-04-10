import { Pressable, StyleSheet, Text, View } from "react-native";

import { ScreenId } from "../types";
import { theme } from "../theme";

type BottomNavProps = {
  activeScreen: ScreenId;
  labels: Record<ScreenId, string>;
  onNavigate: (screen: ScreenId) => void;
};

const tabs: ScreenId[] = ["home", "setup", "live", "summary"];

export function BottomNav({ activeScreen, labels, onNavigate }: BottomNavProps) {
  return (
    <View style={styles.wrap}>
      <View style={styles.nav}>
        {tabs.map((tab) => {
          const active = tab === activeScreen;

          return (
            <Pressable
              key={tab}
              onPress={() => onNavigate(tab)}
              style={[styles.item, active && styles.itemActive]}
            >
              <Text style={[styles.label, active && styles.labelActive]}>{labels[tab]}</Text>
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
  item: {
    flex: 1,
    alignItems: "center",
    justifyContent: "center",
    minHeight: 52,
    borderRadius: 18,
  },
  itemActive: {
    backgroundColor: theme.colors.ink,
  },
  label: {
    color: theme.colors.textMuted,
    fontSize: 14,
    fontWeight: "700",
  },
  labelActive: {
    color: theme.colors.textOnDark,
  },
});
