package com.projectkorra.ProjectKorra.chiblocking;

import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.projectkorra.ProjectKorra.Methods;
import com.projectkorra.ProjectKorra.ProjectKorra;
import com.projectkorra.ProjectKorra.Ability.StockAbilities;
import com.projectkorra.ProjectKorra.waterbending.Bloodbending;

public class WarriorStance {
	
	public static int strength = ProjectKorra.plugin.getConfig().getInt("Abilities.Chi.WarriorStance.Strength") - 1;
	public static int resistance = ProjectKorra.plugin.getConfig().getInt("Abilities.Chi.WarriorStance.Resistance");
	
	private Player player;
	public static ConcurrentHashMap<Player, WarriorStance> instances = new ConcurrentHashMap<Player, WarriorStance>();
	
	public WarriorStance(Player player) {
		this.player = player;
		if (instances.containsKey(player)) {
			instances.remove(player);
			return;
		}
		instances.put(player, this);
	}
	
	private void progress() {
		if (player.isDead() || !player.isOnline()) {
			remove();
			return;
		}
		if (!Methods.canBend(player.getName(), StockAbilities.WarriorStance.toString())) {
			remove();
			return;
		}
		
		if (Paralyze.isParalyzed(player) || Bloodbending.isBloodbended(player)) {
			remove();
			return;
		}
		if (!player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE)) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60, resistance));
		}
		
		if (!player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 60, strength));
		}
	}
	
	public static void progressAll() {
		for (Player player: instances.keySet()) {
			instances.get(player).progress();
		}
	}
	
	private void remove() {
		instances.remove(player);
	}
	
	public static boolean isInWarriorStance(Player player) {
		if (instances.containsKey(player)) return true;
		return false;
	}

}
