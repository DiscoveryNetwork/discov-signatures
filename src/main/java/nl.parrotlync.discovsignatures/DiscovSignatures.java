package nl.parrotlync.discovsignatures;

import nl.parrotlync.discovsignatures.command.SignatureCommandExecutor;
import nl.parrotlync.discovsignatures.listener.SignatureListener;
import nl.parrotlync.discovsignatures.manager.RequestManager;
import nl.parrotlync.discovsignatures.manager.SignatureManager;
import nl.parrotlync.discovsignatures.util.DatabaseUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class DiscovSignatures extends JavaPlugin {
    private static DiscovSignatures instance;
    private final SignatureManager signatureManager;
    private final RequestManager requestManager;
    private DatabaseUtil databaseUtil;

    public DiscovSignatures() {
        instance = this;
        signatureManager = new SignatureManager();
        requestManager = new RequestManager();
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new SignatureListener(), this);
        this.getCommand("signatures").setExecutor(new SignatureCommandExecutor());

        // Database
        getLogger().info("Trying to establish database connection...");
        databaseUtil = new DatabaseUtil(getConfig().getString("database.host"), getConfig().getString("database.username"),
                getConfig().getString("database.password"), getConfig().getString("database.database"));
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            try {
                databaseUtil.connect();
                getLogger().info("Database connection established!");
            } catch (Exception e) {
                getLogger().warning("Something went wrong while trying to establish a database connection.");
                e.printStackTrace();
            }
        });

        for (Player player : Bukkit.getOnlinePlayers()) {
            signatureManager.load(player);
        }
        getLogger().info("DiscovSignatures is now enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("DiscovSignatures is now disabled!");
    }

    public static DiscovSignatures getInstance() {
        return instance;
    }

    public SignatureManager getSignatureManager() {
        return signatureManager;
    }

    public RequestManager getRequestManager() {
        return requestManager;
    }

    public DatabaseUtil getDatabaseUtil() { return databaseUtil; }
}
