package me.spikey.maplocker;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);

        getCommand("lock").setExecutor(new LockCommand());
        getCommand("unlock").setExecutor(new UnLockCommand());

        Metrics metrics = new Metrics(this, 13934);
    }

    public boolean mapIsLocked(ItemStack itemStack, String name) {
        if (itemStack.getItemMeta().getLore() == null) return false;
        for (String s : itemStack.getItemMeta().getLore()) {
            if (s.contains("Locked") && !s.contains("Locked by %s.".formatted(name))) return true;
        }
        return false;
    }

    @EventHandler
    public void prepareCraftEvent(final PrepareItemCraftEvent event) {
        final ItemStack result = event.getInventory().getResult();
        if (event.getInventory().getResult() != null) {
            assert result != null;
            if (result.getType() == Material.FILLED_MAP && result.getAmount() >= 2 && mapIsLocked(result, event.getViewers().get(0).getName())) {
                event.getInventory().setResult(new ItemStack(Material.AIR));
                for (final HumanEntity humanEntity : event.getViewers()) {
                    final Player player = (Player)humanEntity;
                    player.sendMessage(ChatColor.DARK_RED + "Error - " + ChatColor.RESET + "Cloning maps is disabled on this server!");
                }
            }
            else if (result.getType() == Material.WRITTEN_BOOK && result.getAmount() >= 1 && mapIsLocked(result, event.getViewers().get(0).getName())) {
                event.getInventory().setResult(new ItemStack(Material.AIR));
                for (final HumanEntity humanEntity : event.getViewers()) {
                    final Player player = (Player)humanEntity;
                    player.sendMessage(ChatColor.DARK_RED + "Error - " + ChatColor.RESET + "Cloning books is disabled on this server!");
                }
            }
        }
    }

    @EventHandler
    public void inventoryClickEvent(final InventoryClickEvent event) {
        final Player player = (Player)event.getWhoClicked();
        final ItemStack itemStack = event.getCurrentItem();
        if (event.getSlotType() == InventoryType.SlotType.RESULT) {
            assert itemStack != null;
            if (itemStack.getType() == Material.FILLED_MAP && itemStack.getAmount() >= 2 && mapIsLocked(itemStack, player.getName())) {
                event.setCancelled(true);
                player.updateInventory();
                player.sendMessage(ChatColor.DARK_RED + "Error - " + ChatColor.RESET + "Cloning maps is disabled on this server!");

            }
            else if (itemStack.getType() == Material.WRITTEN_BOOK && itemStack.getAmount() >= 1 && mapIsLocked(itemStack, player.getName())) {
                event.setCancelled(true);
                player.updateInventory();
                player.sendMessage(ChatColor.DARK_RED + "Error - " + ChatColor.RESET + "Cloning books is disabled on this server!");
            }
        }
    }
}
