package me.johnnywoof.sniper;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SniperBow extends JavaPlugin implements Listener {

	private static final String DISPLAY_BOW_NAME = ChatColor.GREEN + "Sniper Bow";

	@Override
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(this, this);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onShoot(EntityShootBowEvent event) {
		if (event.getEntity() instanceof Player && event.getProjectile() instanceof Arrow && event.getBow().hasItemMeta()) {

			ItemMeta m = event.getBow().getItemMeta();

			if (m.hasDisplayName() && DISPLAY_BOW_NAME.equals(m.getDisplayName())) {

				Player shooter = (Player) event.getEntity();

				Arrow arrow = (Arrow) event.getProjectile();

				arrow.setFireTicks(0);
				arrow.setVelocity(arrow.getVelocity().multiply(5));
				arrow.setKnockbackStrength(10);
				arrow.setCritical(false);
				arrow.spigot().setDamage(1000);
				shooter.removePotionEffect(PotionEffectType.SLOW);

			}

		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onSwitch(PlayerItemHeldEvent event) {

		ItemStack i = event.getPlayer().getInventory().getItem(event.getPreviousSlot());

		if (i != null && i.getType() == Material.BOW && i.hasItemMeta()) {

			ItemMeta m = i.getItemMeta();

			if (m.hasDisplayName() && DISPLAY_BOW_NAME.equals(m.getDisplayName())) {

				event.getPlayer().removePotionEffect(PotionEffectType.SLOW);

			}

		}

	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
	public void onInteract(PlayerInteractEvent event) {

		if (event.hasItem() && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
				&& event.getItem().getType() == Material.BOW && event.getItem().hasItemMeta()) {

			ItemMeta m = event.getItem().getItemMeta();

			if (m.hasDisplayName() && DISPLAY_BOW_NAME.equals(m.getDisplayName())) {

				event.getPlayer().addPotionEffect(
						new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 2000, false, false));

			}

		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof InventoryHolder) {

			InventoryHolder holder = (InventoryHolder) sender;

			ItemStack bow = new ItemStack(Material.BOW);
			ItemMeta m = bow.getItemMeta();
			m.setDisplayName(DISPLAY_BOW_NAME);
			bow.setItemMeta(m);

			holder.getInventory().addItem(bow);

			sender.sendMessage(ChatColor.GREEN + "You have been given a Sniper Bow!");

		} else {
			sender.sendMessage(ChatColor.RED + "You must have an inventory!");
		}
		return true;
	}

}
