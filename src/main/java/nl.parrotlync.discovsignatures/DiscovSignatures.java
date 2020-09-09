package nl.parrotlync.discovsignatures;

import nl.parrotlync.discovsignatures.command.SignatureCommandExecutor;
import nl.parrotlync.discovsignatures.listener.PlayerListener;
import nl.parrotlync.discovsignatures.manager.PlayerManager;
import org.bukkit.plugin.java.JavaPlugin;

public class DiscovSignatures extends JavaPlugin {
    private static DiscovSignatures instance;
    private static PlayerManager playerManager;

    public DiscovSignatures() {
        instance = this;
        playerManager = new PlayerManager();
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        this.getCommand("signatures").setExecutor(new SignatureCommandExecutor());
        playerManager.load();
        getLogger().info("DiscovSignatures is now enabled!");
    }

    @Override
    public void onDisable() {
        playerManager.save();
        getLogger().info("DiscovSignatures is now disabled!");
    }

    public static DiscovSignatures getInstance() { return instance; }

    public static PlayerManager getPlayerManager() { return playerManager; }
}
