package com.lavacraftserver.HarryPotterSpells.Spells;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.lavacraftserver.HarryPotterSpells.HPS;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell.spell;

@spell(
		name = "Wingardium Leviosa", 
		description = "Makes the caster fly for a short while or until next cast", 
		range = 0, 
		goThroughWalls = false,
		cooldown=180
)
public class WingardiumLeviosa extends Spell implements Listener {
	private List<String> players = new ArrayList<String>();
	private int taskid;

	public boolean cast(final Player p) {
		if (players.contains(p.getName())) {
			p.setFlying(false);
			p.setAllowFlight(false);
			players.remove(p.getName());
			Bukkit.getServer().getScheduler().cancelTask(taskid);
		} else {
			p.setAllowFlight(true);
			p.setFlying(true);
			if(HPS.Plugin.getConfig().getBoolean("spells.wingardium-leviosa.cancel-fall-damage", true) && !players.contains(p.getName())){
				players.add(p.getName());
			}
			
			long duration = 0;
			String durationString = HPS.Plugin.getConfig().getString("spells.wingardium-leviosa.duration", "200t");
			if (durationString.endsWith("t")) {
				String ticks = durationString.substring(0, durationString.length() - 1);
				duration = Integer.parseInt(ticks);
			} else {
				duration = Integer.parseInt(durationString) * 20;
			}
			
			taskid = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(HPS.Plugin, new Runnable() {
				public void run() {
					if (players.contains(p.getName())) {
						p.setFlying(false);
						p.setAllowFlight(false);
						players.remove(p.getName());
					}
				}
			}, duration);
		}
		return true;
	}

	@EventHandler
	public void onPlayerDamage(EntityDamageEvent e) {
		if (e.getCause() == DamageCause.FALL && e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if (players.contains(p.getName())) {
				e.setDamage(0);
			}

		}
	}
}
