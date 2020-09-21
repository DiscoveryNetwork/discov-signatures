package nl.parrotlync.discovsignatures.listener;

import nl.parrotlync.discovsignatures.DiscovSignatures;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

public class SignatureListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        DiscovSignatures.getPlayerManager().giveItem(player);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        ItemStack item = event.getItemDrop().getItemStack();
        if (item.getType() == Material.WRITTEN_BOOK) {
            if (item.getItemMeta().getDisplayName().equals("§6Autograph Book")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        if (item.getType() == Material.WRITTEN_BOOK) {
            if (item.getItemMeta().getDisplayName().equals("§6Autograph Book")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        ItemStack item = event.getOffHandItem();
        if (item.getType() == Material.WRITTEN_BOOK) {
            if (item.getItemMeta().getDisplayName().equals("§6Autograph Book")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        ItemStack item = event.getCursor();
        if (item.getType() == Material.WRITTEN_BOOK) {
            if (item.getItemMeta().getDisplayName().equals("§6Autograph Book")) {
                event.setCancelled(true);
            }
        }
    }
}
