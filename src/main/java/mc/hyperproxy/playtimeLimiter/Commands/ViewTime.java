package mc.hyperproxy.playtimeLimiter.Commands;

import mc.hyperproxy.playtimeLimiter.PlayerUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ViewTime implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player p) {
            double remainingTime = PlayerUtil.getTimeInfo(p).getRemainingTime();
            p.sendMessage(ChatColor.DARK_PURPLE + "Your remaining time is " + ChatColor.GOLD + remainingTime);
        }

        // If the player (or console) uses our command correct, we can return true
        return true;
    }
}
