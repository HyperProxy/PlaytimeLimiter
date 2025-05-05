package mc.hyperproxy.playtimeLimiter.Commands;

import mc.hyperproxy.playtimeLimiter.PlayerTimeInfo;
import mc.hyperproxy.playtimeLimiter.PlayerUtil;
import mc.hyperproxy.playtimeLimiter.PlaytimeLimiter;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;

import static org.bukkit.Bukkit.getOfflinePlayer;
import static org.bukkit.Bukkit.getPlayer;

public class SetTime implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player target;
        try{
            target = getPlayer(args[0]);
        } catch (Exception e) {
            System.out.println(e);
            target = (Player)getOfflinePlayer(args[0]);
        }
        assert target != null;
        PlayerTimeInfo timeInfo = PlayerUtil.getTimeInfo(target);
        double playTimeRemaining = Double.parseDouble(args[1]);
        if(sender instanceof Player p){
            p.sendMessage(ChatColor.DARK_PURPLE + "Player " + args[0] + " remaining playtime set to"
                    + ChatColor.GOLD + Double.parseDouble(args[1]));
        } else{
            System.out.println("Player " + args[0] + " remaining playtime set to" + playTimeRemaining);
        }

        timeInfo.setRemainingTime(playTimeRemaining);
        assert sender instanceof Player;
        File pluginFolder = PlaytimeLimiter.getInstance().getDataFolder();
        PlayerUtil.savePlayerPlaytime(target, pluginFolder);

        return true;
    }
}
