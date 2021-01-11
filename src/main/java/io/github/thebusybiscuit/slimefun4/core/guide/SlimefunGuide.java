package io.github.thebusybiscuit.slimefun4.core.guide;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.thebusybiscuit.slimefun4.api.player.PlayerProfile;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.guide.SurvivalSlimefunGuide;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;

/**
 * This is a static utility class that provides convenient access to the methods
 * of {@link SlimefunGuideImplementation} that abstracts away the actual implementation.
 * 
 * @author TheBusyBiscuit
 * 
 * @see SlimefunGuideImplementation
 * @see SurvivalSlimefunGuide
 *
 */
public final class SlimefunGuide {

    private SlimefunGuide() {}

    @Nonnull
    public static ItemStack getItem(@Nonnull SlimefunGuideLayout design) {
        return SlimefunPlugin.getRegistry().getGuideLayout(design).getItem();
    }

    public static void openCheatMenu(@Nonnull Player p) {
        openMainMenuAsync(p, SlimefunGuideLayout.CHEAT_MODE, 1);
    }

    public static void openGuide(@Nonnull Player p, @Nullable ItemStack guide) {
        if (SlimefunUtils.isItemSimilar(guide, getItem(SlimefunGuideLayout.CHEAT_MODE), true)) {
            openGuide(p, SlimefunGuideLayout.CHEAT_MODE);
        } else {
            /*
             * When using /sf cheat or /sf open_guide the ItemStack is null anyway,
             * so we don't even need to check here at this point.
             */
            openGuide(p, SlimefunGuideLayout.SURVIVAL_MODE);
        }
    }

    public static void openGuide(@Nonnull Player p, @Nonnull SlimefunGuideLayout layout) {
        if (!SlimefunPlugin.getWorldSettingsService().isWorldEnabled(p.getWorld())) {
            return;
        }

        Optional<PlayerProfile> optional = PlayerProfile.find(p);

        if (optional.isPresent()) {
            PlayerProfile profile = optional.get();
            SlimefunGuideImplementation guide = SlimefunPlugin.getRegistry().getGuideLayout(layout);
            profile.getGuideHistory().openLastEntry(guide);
        } else {
            openMainMenuAsync(p, layout, 1);
        }
    }

    private static void openMainMenuAsync(Player player, SlimefunGuideLayout layout, int selectedPage) {
        if (!PlayerProfile.get(player, profile -> SlimefunPlugin.runSync(() -> openMainMenu(profile, layout, selectedPage)))) {
            SlimefunPlugin.getLocalization().sendMessage(player, "messages.opening-guide");
        }
    }

    public static void openMainMenu(PlayerProfile profile, SlimefunGuideLayout layout, int selectedPage) {
        SlimefunPlugin.getRegistry().getGuideLayout(layout).openMainMenu(profile, selectedPage);
    }

    public static void openCategory(PlayerProfile profile, Category category, SlimefunGuideLayout layout, int selectedPage) {
        if (category == null) {
            return;
        }

        SlimefunPlugin.getRegistry().getGuideLayout(layout).openCategory(profile, category, selectedPage);
    }

    public static void openSearch(PlayerProfile profile, String input, boolean survival, boolean addToHistory) {
        SlimefunGuideImplementation layout = SlimefunPlugin.getRegistry().getGuideLayout(SlimefunGuideLayout.SURVIVAL_MODE);

        if (!survival) {
            layout = SlimefunPlugin.getRegistry().getGuideLayout(SlimefunGuideLayout.CHEAT_MODE);
        }

        layout.openSearch(profile, input, addToHistory);
    }

    public static void displayItem(PlayerProfile profile, ItemStack item, boolean addToHistory) {
        SlimefunPlugin.getRegistry().getGuideLayout(SlimefunGuideLayout.SURVIVAL_MODE).displayItem(profile, item, 0, addToHistory);
    }

    public static void displayItem(PlayerProfile profile, SlimefunItem item, boolean addToHistory) {
        SlimefunPlugin.getRegistry().getGuideLayout(SlimefunGuideLayout.SURVIVAL_MODE).displayItem(profile, item, addToHistory);
    }

    public static boolean isGuideItem(@Nonnull ItemStack item) {
        return SlimefunUtils.isItemSimilar(item, getItem(SlimefunGuideLayout.SURVIVAL_MODE), true) || SlimefunUtils.isItemSimilar(item, getItem(SlimefunGuideLayout.CHEAT_MODE), true);
    }

    @Nonnull
    public static SlimefunGuideLayout getDefaultLayout() {
        return SlimefunGuideLayout.SURVIVAL_MODE;
    }
}
