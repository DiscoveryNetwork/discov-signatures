package nl.parrotlync.discovsignatures.listener;

import nl.parrotlync.discovsignatures.DiscovSignatures;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        DiscovSignatures.getPlayerManager().giveItem(player);
    }
}
