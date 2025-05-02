package mc.hyperproxy.playtimeLimiter;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;


public class PlaytimeLimiter extends JavaPlugin implements Listener {

    public static double time = 28800.0;   //  28800 seconds in 8 hrs
    private File pluginConfig;
    private FileConfiguration config;

    @Override
    public void onEnable() {
        getDataFolder().mkdirs();
        loadConfig();

        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();

        initializePlayerPlaytime(p);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player p = event.getPlayer();

        savePlayerPlaytime(p);
    }

    public void loadConfig() {
        pluginConfig = new File(getDataFolder(), "config.yml");
        if (!pluginConfig.exists()) {
            saveResource("config.yml", false);
        }

        config = new YamlConfiguration();
        config.options().parseComments(true);

        try {
            config.load(pluginConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
        time = config.getDouble("dailyTime");
    }

    public void initializePlayerPlaytime(Player p) {
        PlayerTimeInfo timeInfo = new PlayerTimeInfo();
        File f = new File(getDataFolder().getAbsolutePath() + "/player/" + p.getUniqueId() + "/general.yml");
        if (f.exists()) {
            FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);
            timeInfo.setRemainingTime(cfg.getDouble("timeRemaining"));
            timeInfo.setTimePlayedToday(cfg.getDouble("timePlayed"));
        } else {
            timeInfo.setRemainingTime(time);
            timeInfo.setTimePlayedToday(0);
        }

        PlayerUtil.setTimeInfo(p, timeInfo);
    }

    public void savePlayerPlaytime(Player p) {
        PlayerTimeInfo timeInfo = PlayerUtil.getTimeInfo(p);
        File f = new File(getDataFolder().getAbsolutePath() + "/player/" + p.getUniqueId() + "/general.yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);
        cfg.set("timeRemaining", timeInfo.getRemainingTime());
        cfg.set("timePlayed", timeInfo.getTimePlayedToday());
        try {
            cfg.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PlayerUtil.setTimeInfo(p, null);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
