package io.github.thebusybiscuit.slimefun4.core.guide.options;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuide;
import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuideLayout;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.utils.ChatUtils;
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils;

class GuideLayoutOption implements SlimefunGuideOption<SlimefunGuideLayout> {

    @Override
    public SlimefunAddon getAddon() {
        return SlimefunPlugin.instance();
    }

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(SlimefunPlugin.instance(), "guide_layout");
    }

    @Override
    public Optional<ItemStack> getDisplayItem(Player p, ItemStack guide) {
        if (!p.hasPermission("slimefun.cheat.items")) {
            // Only Players with the appropriate permission can access the cheat sheet
            return Optional.empty();
        }

        Optional<SlimefunGuideLayout> current = getSelectedOption(p, guide);

        if (current.isPresent()) {
            SlimefunGuideLayout layout = current.get();
            ItemStack item = new ItemStack(Material.AIR);

            if (layout == SlimefunGuideLayout.SURVIVAL_MODE) {
                item.setType(Material.CHEST);
            } else {
                item.setType(Material.COMMAND_BLOCK);
            }

            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.GRAY + "Slimefun Guide Design: " + ChatColor.YELLOW + ChatUtils.humanize(layout.name()));
            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add((layout == SlimefunGuideLayout.SURVIVAL_MODE ? ChatColor.GREEN : ChatColor.GRAY) + "Chest");

            lore.add((layout == SlimefunGuideLayout.CHEAT_MODE ? ChatColor.GREEN : ChatColor.GRAY) + "Cheat Sheet");

            lore.add("");
            lore.add(ChatColor.GRAY + "\u21E8 " + ChatColor.YELLOW + "Click to change your layout");
            meta.setLore(lore);
            item.setItemMeta(meta);

            return Optional.of(item);
        }

        return Optional.empty();
    }

    @Override
    public void onClick(Player p, ItemStack guide) {
        Optional<SlimefunGuideLayout> current = getSelectedOption(p, guide);

        if (current.isPresent()) {
            SlimefunGuideLayout next = getNextLayout(p, current.get());
            setSelectedOption(p, guide, next);
        }

        SlimefunGuideSettings.openSettings(p, guide);
    }

    @Nonnull
    private SlimefunGuideLayout getNextLayout(@Nonnull Player p, @Nonnull SlimefunGuideLayout layout) {
        if (p.hasPermission("slimefun.cheat.items")) {
            if (layout == SlimefunGuideLayout.SURVIVAL_MODE) {
                return SlimefunGuideLayout.CHEAT_MODE;
            } else {
                return SlimefunGuideLayout.SURVIVAL_MODE;
            }
        } else {
            return SlimefunGuideLayout.SURVIVAL_MODE;
        }
    }

    @Override
    public Optional<SlimefunGuideLayout> getSelectedOption(Player p, ItemStack guide) {
        if (SlimefunUtils.isItemSimilar(guide, SlimefunGuide.getItem(SlimefunGuideLayout.CHEAT_MODE), true, false)) {
            return Optional.of(SlimefunGuideLayout.CHEAT_MODE);
        } else {
            return Optional.of(SlimefunGuideLayout.SURVIVAL_MODE);
        }
    }

    @Override
    public void setSelectedOption(Player p, ItemStack guide, SlimefunGuideLayout value) {
        guide.setItemMeta(SlimefunGuide.getItem(value).getItemMeta());
    }

}
