package me.spikey.maplocker;

import com.google.common.collect.Lists;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class UnLockCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This is a player only command!");
            return true;
        }

        Player player = (Player) sender;

        if (!player.getInventory().getItemInMainHand().getType().equals(Material.FILLED_MAP)) {
            player.sendMessage("This command can only be run with a map in your main hand.");
            return true;
        }

        ItemStack map = player.getInventory().getItemInMainHand();


        ItemMeta iM = map.getItemMeta();

        if (iM.getLore() != null) {
            //
            for (int x = 0; x < iM.getLore().size(); x++) {
                if (iM.getLore().get(x).contains("Locked by %s.".formatted(player.getName()))) {
                    iM.setLore(Lists.newArrayList());
                    map.setItemMeta(iM);
                    player.sendMessage("This map has been unlocked.");
                    return true;
                }
            }
            player.sendMessage("This map is locked by another player.");
            return true;
        }

        player.sendMessage("This map is not locked.");
        return true;
    }
}
