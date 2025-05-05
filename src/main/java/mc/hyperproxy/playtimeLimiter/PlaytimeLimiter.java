package mc.hyperproxy.playtimeLimiter;

import mc.hyperproxy.playtimeLimiter.Commands.SetTime;
import mc.hyperproxy.playtimeLimiter.Commands.ViewTime;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;


public class PlaytimeLimiter extends JavaPlugin implements Listener {

    public static double time = 3; // shouldn't matter, is changed when config is loaded (used when config file is deleted)
    private File pluginConfig;
    private FileConfiguration config;
    private static PlaytimeLimiter instance;

    public static PlaytimeLimiter getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        getDataFolder().mkdirs();
        try {
            loadConfig();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.getCommand("viewtime").setExecutor(new ViewTime());
        this.getCommand("settime").setExecutor(new SetTime());
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();

        initializePlayerPlaytime(p);
        startTracking(p);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player p = event.getPlayer();

        PlayerUtil.savePlayerPlaytime(p, getDataFolder());
        PlayerUtil.setTimeInfo(p, null);
    }

    public void loadConfig() throws IOException {
        pluginConfig = new File(getDataFolder(), "config.yml");
        if (!pluginConfig.exists()) {
            pluginConfig.createNewFile();
            // saveResource("config.yml", true);
            config = new YamlConfiguration();
            config.options().parseComments(true);
            config.set("dailyTime", time);
            try{
                config.save(pluginConfig);
            } catch(IOException e){
                e.printStackTrace();
            }
            time = config.getDouble("dailyTime");
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

            FileConfiguration cfg = new YamlConfiguration();
            cfg.set("timeRemaining", timeInfo.getRemainingTime());
            cfg.set("timePlayed", timeInfo.getTimePlayedToday());
            try {
                cfg.save(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        PlayerUtil.setTimeInfo(p, timeInfo);
    }

    public void startTracking(Player p) {
        new BukkitRunnable() {
            @Override
            public void run() {
                PlayerTimeInfo timeInfo = PlayerUtil.getTimeInfo(p);
                double playTimeRemaining = timeInfo.getRemainingTime();

                playTimeRemaining--;
                timeInfo.setRemainingTime(playTimeRemaining);


                p.sendMessage("Playtime remaining: " + playTimeRemaining + " minutes");
                PlayerUtil.savePlayerPlaytime(p, getDataFolder());

                if (playTimeRemaining <= 0) {
                    playerTimeUp(p);
                    cancel();
                }
            }
        }.runTaskTimer(this, 1200L, 1200L); // 1200 ticks = 60 seconds
    }

    public void playerTimeUp(Player p) {
        // KICK PLAYER AND DO ALL THE CORRECT SAVING STUFF
        System.out.println("kicking player " + p.getDisplayName());
        p.kickPlayer("Your playtime has elapsed!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

}
