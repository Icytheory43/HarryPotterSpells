package com.lavacraftserver.HarryPotterSpells;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;

import com.lavacraftserver.HarryPotterSpells.Jobs.EnableJob;
import com.lavacraftserver.HarryPotterSpells.Spells.Spell;

public class Listeners implements Listener, EnableJob {
    
    public static final Permission CAST_SPELLS = new Permission("HarryPotterSpells.use", PermissionDefault.OP);
    
    @Override
    public void onEnable(PluginManager pm) {
        pm.addPermission(CAST_SPELLS);
        pm.registerEvents(this, HPS.Plugin);
    }
    	
	@EventHandler
	public void PIE(PlayerInteractEvent e) {
		if(e.getPlayer().hasPermission(CAST_SPELLS) && HPS.Wand.isWand(e.getPlayer().getItemInHand())) {
		    
            if (e.getPlayer().getGameMode().equals(GameMode.CREATIVE))
                e.setCancelled(true);
		    
			//Change spell
			if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			    Integer knows = HPS.PlayerSpellConfig.getStringListOrEmpty(e.getPlayer().getName()).size() - 1, cur = HPS.SpellManager.getCurrentSpellPosition(e.getPlayer()), neww;
			    			    
			    if(HPS.PlayerSpellConfig.getStringListOrEmpty(e.getPlayer().getName()).isEmpty() || cur == null) {
			        HPS.PM.tell(e.getPlayer(), HPS.Localisation.getTranslation("genKnowNoSpells"));
			        return;
			    }
			    
			    if(e.getPlayer().isSneaking()) {
			        if(cur == 0)
			            neww = knows;
			        else
			            neww = cur - 1;
			    } else {
			        if(cur == knows)
			            neww = 0;
			        else
			            neww = cur + 1;
			    }
			    
			    try {
			        HPS.PM.newSpell(e.getPlayer(), HPS.SpellManager.setCurrentSpell(e.getPlayer(), neww).getName());
			    } catch(IllegalArgumentException er) {
                    HPS.PM.tell(e.getPlayer(), HPS.Localisation.getTranslation("genKnowNoSpells"));
                    return;
			    }
			    
			    return;
			}
			
			//Cast spell
			if(e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
				Spell currentSpell = HPS.SpellManager.getCurrentSpell(e.getPlayer());
				if(currentSpell != null)
					HPS.SpellManager.cleverCast(e.getPlayer(), currentSpell);
			}
			
		}
	}
	
	@EventHandler
	public void PIEE(PlayerInteractEntityEvent e) {
		if(e.getPlayer().hasPermission(CAST_SPELLS) && HPS.Wand.isWand(e.getPlayer().getItemInHand())) {
		    Integer knows = HPS.PlayerSpellConfig.getStringListOrEmpty(e.getPlayer().getName()).size() - 1, cur = HPS.SpellManager.getCurrentSpellPosition(e.getPlayer()), neww;
            
            if(cur == null) {
                HPS.PM.tell(e.getPlayer(), HPS.Localisation.getTranslation("genKnowNoSpells"));
                return;
            }
            
            if(e.getPlayer().isSneaking()) {
                if(cur == 0)
                    neww = knows;
                else
                    neww = cur - 1;
            } else {
                if(cur == knows)
                    neww = 0;
                else
                    neww = cur + 1;
            }
            
            try {
                HPS.PM.newSpell(e.getPlayer(), HPS.SpellManager.setCurrentSpell(e.getPlayer(), neww).getName());
            } catch(IllegalArgumentException er) {
                HPS.PM.tell(e.getPlayer(), HPS.Localisation.getTranslation("genKnowNoSpells"));
                return;
            }
            
            return;
		}
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e) {
		if(HPS.Plugin.getConfig().getBoolean("spell-castable-with-chat")) {
			if(HPS.SpellManager.isSpell(e.getMessage().substring(0, e.getMessage().length() - 1))) {
				HPS.SpellManager.cleverCast(e.getPlayer(), HPS.SpellManager.getSpell(e.getMessage().substring(0, e.getMessage().length() - 1)));
				return;
			}
		}
	}

	@EventHandler
	public void onPlayerCraft(CraftItemEvent e) {
	    if(HPS.Wand.isLorelessWand(e.getRecipe().getResult())) {
	        e.setCurrentItem(HPS.Wand.getWand());
	        final Player p = (Player) e.getWhoClicked();
	        Bukkit.getScheduler().runTask(HPS.Plugin, new Runnable() {

                @SuppressWarnings("deprecation")
                @Override
                public void run() {
                    p.updateInventory();
                }

	        });
	    }
	}

}
