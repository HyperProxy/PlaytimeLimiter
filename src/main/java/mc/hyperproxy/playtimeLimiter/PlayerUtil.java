package mc.hyperproxy.playtimeLimiter;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;


public class PlayerUtil extends JavaPlugin {
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
}
