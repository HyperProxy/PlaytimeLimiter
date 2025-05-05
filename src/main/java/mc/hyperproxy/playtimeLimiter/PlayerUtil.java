package mc.hyperproxy.playtimeLimiter;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class PlayerUtil {
    private static final Map<String, PlayerTimeInfo> playerMemory = new HashMap<>();

    public static PlayerTimeInfo getTimeInfo(Player p) {
        if (!playerMemory.containsKey(p.getUniqueId().toString())) {
            PlayerTimeInfo timeInfo = new PlayerTimeInfo();
            playerMemory.put(p.getUniqueId().toString(), timeInfo);
            return timeInfo;
        }
        return playerMemory.get(p.getUniqueId().toString());
    }

    public static void setTimeInfo(Player p, PlayerTimeInfo timeInfo) {
        if (timeInfo == null) {
            playerMemory.remove(p.getUniqueId().toString());
        } else {
            playerMemory.put(p.getUniqueId().toString(), timeInfo);
        }
    }

    public static void savePlayerPlaytime(Player p, File folder) {
        PlayerTimeInfo timeInfo = PlayerUtil.getTimeInfo(p);
        File f = new File(folder.getAbsolutePath() + "/player/" + p.getUniqueId() + "/general.yml");
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);
        cfg.set("timeRemaining", timeInfo.getRemainingTime());
        cfg.set("timePlayed", timeInfo.getTimePlayedToday());
        try {
            cfg.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
