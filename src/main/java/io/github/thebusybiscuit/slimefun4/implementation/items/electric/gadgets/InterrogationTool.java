package io.github.thebusybiscuit.slimefun4.implementation.items.electric.gadgets;

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType;
import io.github.thebusybiscuit.slimefun4.core.attributes.EnergyNetComponent;
import io.github.thebusybiscuit.slimefun4.core.attributes.Interrogatable;
import io.github.thebusybiscuit.slimefun4.core.handlers.ItemUseHandler;
import io.github.thebusybiscuit.slimefun4.core.networks.energy.EnergyNet;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.items.SimpleSlimefunItem;
import io.github.thebusybiscuit.slimefun4.utils.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

/**
 * The {@link InterrogationTool} is used to get custom-defined information from a {@link SlimefunItem}
 * 
 * @author Sefiraat
 * 
 * @see Interrogatable
 *
 */
public class InterrogationTool extends SimpleSlimefunItem<ItemUseHandler> {

    @ParametersAreNonnullByDefault
    public InterrogationTool(ItemGroup itemGroup, SlimefunItemStack item, RecipeType recipeType, ItemStack[] recipe) {
        super(itemGroup, item, recipeType, recipe);
    }

    @Override
    public ItemUseHandler getItemHandler() {
        return e -> {
            Optional<SlimefunItem> block = e.getSlimefunBlock();

            if (e.getClickedBlock().isPresent() && block.isPresent()) {
                SlimefunItem item = block.get();
                Player player = e.getPlayer();
                if (item instanceof Interrogatable) {
                    Interrogatable interrogatable = (Interrogatable) item;
                    Slimefun.getLocalization().sendMessage(
                        player,
                        "messages.interrogation-tool.success",
                        false,
                        s -> s.replace("%name%", ChatColor.stripColor(item.getItemName())
                    ));
                    interrogatable.sendMessages(player);
                } else {
                    Slimefun.getLocalization().sendMessage(player, "messages.interrogation-tool.failure", false);
                }
            }
        };
    }
}
