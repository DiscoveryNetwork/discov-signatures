package nl.parrotlync.discovsignatures.manager;

import nl.parrotlync.discovsignatures.DiscovSignatures;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.UUID;

public class RequestManager {
    private final HashMap<UUID, String> requests = new HashMap<>();

    public UUID addRequest(String signature) {
        UUID uuid = UUID.randomUUID();
        requests.put(uuid, signature);
        Bukkit.getScheduler().runTaskLater(DiscovSignatures.getInstance(), () -> getRequest(uuid), 1200L);
        return uuid;
    }

    public String getRequest(UUID uuid) {
        String signature = requests.get(uuid);
        requests.remove(uuid);
        return signature;
    }
}
