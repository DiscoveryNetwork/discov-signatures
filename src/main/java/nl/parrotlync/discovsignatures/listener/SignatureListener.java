package nl.parrotlync.discovsignatures.listener;

import nl.parrotlync.discovsignatures.DiscovSignatures;
import nl.parrotlync.discovsignatures.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class SignatureListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        DiscovSignatures.getInstance().getSignatureManager().load(player);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        ItemStack item = event.getItemDrop().getItemStack();
        if (item.getType() == Material.WRITTEN_BOOK) {
            if (Objects.requireNonNull(item.getItemMeta()).getDisplayName().equals("§6Autograph Book")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        if (item != null && item.getType() == Material.WRITTEN_BOOK) {
            if (Objects.requireNonNull(item.getItemMeta()).getDisplayName().equals("§6Autograph Book")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        ItemStack item = event.getOffHandItem();
        if (item != null && item.getType() == Material.WRITTEN_BOOK) {
            if (Objects.requireNonNull(item.getItemMeta()).getDisplayName().equals("§6Autograph Book")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        ItemStack item = event.getCursor();
        if (item != null && item.getType() == Material.WRITTEN_BOOK) {
            if (Objects.requireNonNull(item.getItemMeta()).getDisplayName().equals("§6Autograph Book")) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage().toLowerCase();
        if (message.contains("auto") || message.contains("autograph") || message.contains("signature")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (message.contains(player.getName().toLowerCase()) && player.hasPermission("discovsignatures.send")) {
                    ChatUtil.sendMessage(player, "§7(§9§l!§7) It seems like §6" + event.getPlayer().getName() + " §7might want your autograph!", false);
                    break;
                }
            }
        }
    }
}
