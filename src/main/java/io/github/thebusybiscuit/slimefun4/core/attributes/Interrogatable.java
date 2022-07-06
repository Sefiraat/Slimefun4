package io.github.thebusybiscuit.slimefun4.core.attributes;

import org.bukkit.entity.Player;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A {@link Interrogatable} {@link SlimefunItem} can return information about the current
 * state of the item when queried.
 *
 * @author Sefiraat
 */
public interface Interrogatable extends ItemAttribute {

    /**
     * This method should return an array of messages to be sent to the
     * player interrogating this {@link SlimefunItem}
     *
     * @return The {@link String[]} containing the messages to be sent in order
     */
    @Nullable
    String[] messagesToDisplay();

    /**
     * Sends the message(s) to the {@link Player} that this {@link SlimefunItem} has generated.
     *
     * @param player The {@link Player} who should receive the message(s)
     */
    default void sendMessages(@Nonnull Player player) {
        String[] messages = messagesToDisplay();
        if (messages != null && messages.length > 0) {
            player.sendMessage(messages);
        }
    }
}
