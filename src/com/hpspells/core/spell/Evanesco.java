package com.hpspells.core.spell;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.hpspells.core.HPS;
import com.hpspells.core.spell.Spell.SpellInfo;

@SpellInfo (
		name="Evanesco",
		description="descEvanesco",
		range=0,
		goThroughWalls=false,
		cooldown=45
)
public class Evanesco extends Spell {
    
    public Evanesco(HPS instance) {
        super(instance);
    }

    public boolean cast(final Player p) {
		for (Player players : Bukkit.getServer().getOnlinePlayers()) {
            players.hidePlayer(p);
            Location loc = new Location(p.getWorld(), p.getLocation().getBlockX(), p.getLocation().getBlockY() + 1, p.getLocation().getBlockZ());
            p.getWorld().createExplosion(loc, 0, false);
        }
		
		long duration = getTime("duration", 300l);
		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(HPS, new Runnable() {
		    
		    @Override    
		    public void run() {
			    for (Player players : Bukkit.getServer().getOnlinePlayers()) {
		             players.showPlayer(p);
		        }
			    p.getWorld().createExplosion(p.getLocation(), 0, false);
		    }
		    
		}, duration);
		return true;
	}

}
