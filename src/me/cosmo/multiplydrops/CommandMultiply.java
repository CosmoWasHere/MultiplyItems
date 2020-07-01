package me.cosmo.multiplydrops;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Objects;

public class CommandMultiply implements CommandExecutor, Listener {
    int multiplier = 0;
    boolean dropsEnabled = false;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        dropsEnabled = !dropsEnabled;
        multiplier = 0;

        System.out.println(dropsEnabled);
        System.out.println(multiplier);

        return true;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent breakEvent) {
        if (dropsEnabled) {

            Block block = breakEvent.getBlock();
            ItemStack tool = breakEvent.getPlayer().getInventory().getItemInMainHand();

            Collection<ItemStack> drops = block.getDrops(tool);
            block.setType(Material.AIR);

            drops.forEach(itemStack -> {
                int dropAmount = (int) (itemStack.getAmount() * Math.pow(2, this.multiplier));
                int maxDropAmount = itemStack.getMaxStackSize();

                while (dropAmount > 0)  {
                    itemStack.setAmount(Math.min(dropAmount, maxDropAmount));
                    dropAmount -= itemStack.getAmount();
                    Objects.requireNonNull(block.getLocation().getWorld()).dropItemNaturally(block.getLocation(), itemStack);
                }
            });

            breakEvent.isCancelled();
            multiplier += 1;
        }
    }
}
