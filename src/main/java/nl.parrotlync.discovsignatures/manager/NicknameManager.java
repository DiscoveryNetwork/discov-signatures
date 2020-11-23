package nl.parrotlync.discovsignatures.manager;

import org.bukkit.entity.Player;

import java.util.*;

public class NicknameManager {
    private final HashMap<UUID, String> nicknames = new HashMap<>();

    public String getNickname(Player player) {
        if (nicknames.get(player.getUniqueId()) != null) {
            return nicknames.get(player.getUniqueId());
        } else {
            return player.getName();
        }
    }

    public void setNickname(Player player, String nickname) {
        nicknames.put(player.getUniqueId(), nickname);
    }
}
