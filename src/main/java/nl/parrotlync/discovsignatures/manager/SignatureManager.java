package nl.parrotlync.discovsignatures.manager;

import nl.parrotlync.discovsignatures.DiscovSignatures;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.*;

public class SignatureManager {
    private final HashMap<UUID, List<String>> signatures = new HashMap<>();

    public void addSignature(Player player, String signature) {
        signatures.computeIfAbsent(player.getUniqueId(), k -> new ArrayList<>());
        signatures.get(player.getUniqueId()).add(signature);
        Bukkit.getScheduler().runTaskAsynchronously(DiscovSignatures.getInstance(), () -> {
            try {
                DiscovSignatures.getInstance().getDatabaseUtil().addSignature(player.getUniqueId(), signature);
            } catch (Exception e) {
                DiscovSignatures.getInstance().getLogger().warning("Something went wrong while updating a signature.");
                e.printStackTrace();
            }
        });
        player.getInventory().setItem(7, getItem(player));
    }

    public boolean removeSignature(Player player, int pageNumber) {
        signatures.computeIfAbsent(player.getUniqueId(), k -> new ArrayList<>());
        try {
            String signature = signatures.get(player.getUniqueId()).get(pageNumber - 2);
            signatures.get(player.getUniqueId()).remove(pageNumber - 2);
            player.getInventory().setItem(7, getItem(player));
            Bukkit.getScheduler().runTaskAsynchronously(DiscovSignatures.getInstance(), () -> {
                try {
                    DiscovSignatures.getInstance().getDatabaseUtil().removeSignature(player.getUniqueId(), signature);
                } catch (Exception e) {
                    DiscovSignatures.getInstance().getLogger().warning("Something went wrong while removing a signature.");
                    e.printStackTrace();
                }
            });
            return true;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    public ItemStack getItem(Player player) {
        ItemStack item = new ItemStack(Material.WRITTEN_BOOK, 1);
        BookMeta meta = (BookMeta) item.getItemMeta();
        meta.setDisplayName("§6Autograph Book");
        meta.setLore(Collections.singletonList("Gotta collect 'em all!"));
        meta.setAuthor(player.getName());
        meta.addPage("§9Welcome to your \n§6§lAutograph Book!\n\n§r§7§oYou can collect signatures by asking online §r§aStaff Members §7§oor §r§aCharacters §7§ofor an autograph.\n\nTo accept a signature request, click the §r§8[§2Accept§8] §7§obutton in chat.");
        for (String signature : signatures.get(player.getUniqueId())) {
            meta.addPage(signature);
        }
        item.setItemMeta(meta);
        return item;
    }

    public void load(Player player) {
        Bukkit.getScheduler().runTaskAsynchronously(DiscovSignatures.getInstance(), () -> {
            List<String> loadedSignatures = null;
            try {
                loadedSignatures = DiscovSignatures.getInstance().getDatabaseUtil().getSignatures(player.getUniqueId());
                signatures.put(player.getUniqueId(), loadedSignatures);
                DiscovSignatures.getInstance().getLogger().info("Loaded " + loadedSignatures.size() + " signatures for player " + player.getUniqueId());
                Bukkit.getScheduler().runTask(DiscovSignatures.getInstance(), () -> {
                    player.getInventory().setItem(7, getItem(player));
                });
            } catch (Exception e) {
                DiscovSignatures.getInstance().getLogger().warning("Something went wrong while fetching signatures for player " + player.getUniqueId());
                e.printStackTrace();
            }
        });
    }
}
