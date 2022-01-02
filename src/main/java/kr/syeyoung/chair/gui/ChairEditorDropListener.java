package kr.syeyoung.chair.gui;

import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;
import java.util.function.Consumer;

public class ChairEditorDropListener implements Listener {
    private HashMap<UUID, Consumer<ItemStack>> ItemStackConsumers = new HashMap<>();

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        if (ItemStackConsumers.containsKey(event.getPlayer().getUniqueId())) {
            ItemStackConsumers.remove(event.getPlayer().getUniqueId()).accept(event.getItemDrop().getItemStack());
            event.setCancelled(true);
        }
    }

    public void addConsumer(Player p, Consumer<ItemStack> consumer) {
        ItemStackConsumers.put(p.getUniqueId(), consumer);
    }
    public void removeConsumer(Player p) {
        ItemStackConsumers.remove(p.getUniqueId());
    }
}
