package nl.parrotlync.discovsignatures.manager;

import nl.parrotlync.discovsignatures.DiscovSignatures;
import nl.parrotlync.discovsignatures.util.DataUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.*;

public class PlayerManager {
    private HashMap<UUID, List<String>> signatures = new HashMap<>();
    private HashMap<UUID, String> players = new HashMap<>();
    private HashMap<UUID, String> requests = new HashMap<>();
    private String storePathSignatures = "plugins/DiscovSignatures/signatures.data";
    private String storePathPlayers = "plugins/DiscovSignatures/players.data";

    public void setNickname(Player player, String nickname) {
        players.put(player.getUniqueId(), nickname);
    }

    public String getNickName(Player player) {
        if (players.get(player.getUniqueId()) != null) {
            return players.get(player.getUniqueId());
        }
        return player.getName();
    }

    public UUID addRequest(String signature) {
        UUID uuid = UUID.randomUUID();
        requests.put(uuid, signature);
        Bukkit.getScheduler().runTaskLater(DiscovSignatures.getInstance(), () -> DiscovSignatures.getPlayerManager().getRequest(uuid), 1200L);
        return uuid;
    }

    public String getRequest(UUID uuid) {
        String signature = requests.get(uuid);
        requests.remove(uuid);
        return signature;
    }

    public void addSignature(Player player, Player sender, String signature) {
        if (!hasItem(player)) { giveItem(player); }
        if (signatures.get(player.getUniqueId()) == null) {
            signatures.put(player.getUniqueId(), new ArrayList<>());
        } else {
            signatures.get(player.getUniqueId()).add(signature);
        }
        ItemStack item = createItem(player, getSignatureList(player));
        player.getInventory().remove(item);
        player.getInventory().setItem(8, item);
    }

    public boolean removeSignature(Player player, Integer pageNumber) {
        if (!hasItem(player)) { giveItem(player); }
        if (signatures.get(player.getUniqueId()) != null) {
            try {
                signatures.get(player.getUniqueId()).remove(pageNumber - 2);
                ItemStack item = createItem(player, getSignatureList(player));
                player.getInventory().remove(item);
                player.getInventory().setItem(8, item);
                return true;
            } catch (IndexOutOfBoundsException e) {
                return false;
            }
        }
        return false;
    }

    public void giveItem(Player player) {
        if (!hasItem(player)) {
            List<String> signatureList = getSignatureList(player);
            player.getInventory().setItem(8, createItem(player, signatureList));
        }
    }

    private boolean hasItem(Player player) {
        ItemStack item = createItem(player, getSignatureList(player));
        return player.getInventory().contains(item);
    }

    private ItemStack createItem(Player player, List<String> signatureList) {
        ItemStack item = new ItemStack(Material.WRITTEN_BOOK, 1);
        BookMeta meta = (BookMeta) item.getItemMeta();
        meta.setDisplayName("§6Autograph Book");
        meta.setLore(Collections.singletonList("Gotta collect 'em all!"));
        meta.setAuthor(player.getName());
        meta.addPage("§9Welcome to your \n§6§lAutograph Book!\n\n§r§7§oYou can collect signatures by asking online §r§aStaff Members §7§oor §r§aCharacters §7§ofor an autograph.\n\nTo accept a signature request, click the §r§8[§2Accept§8] §7§obutton in chat.");
        for (String signature : signatureList) {
            meta.addPage(signature);
        }
        item.setItemMeta(meta);
        return item;
    }

    private List<String> getSignatureList(Player player) {
        List<String> signatureList;
        if (signatures.get(player.getUniqueId()) != null) {
            signatureList = signatures.get(player.getUniqueId());
        } else {
            signatureList = new ArrayList<>();
        }
        return signatureList;
    }

    public void load() {
        HashMap<UUID, List<String>> storedSignatures = DataUtil.loadObjectFromPath(storePathSignatures);
        if (storedSignatures != null) {
            signatures = storedSignatures;
            DiscovSignatures.getInstance().getLogger().info("Signatures have been loaded from file.");
        }
        HashMap<UUID, String> playerData = DataUtil.loadObjectFromPath(storePathPlayers);
        if (playerData != null) {
            players = playerData;
            DiscovSignatures.getInstance().getLogger().info("Player names have been loaded from file.");
        }
    }

    public void save() {
        DataUtil.saveObjectToPath(signatures, storePathSignatures);
        DataUtil.saveObjectToPath(players, storePathPlayers);
        DiscovSignatures.getInstance().getLogger().info("Data has been saved.");
    }
}
