package me.halfquark.blastresistanceoverride;

import java.util.HashSet;

import org.bukkit.plugin.java.JavaPlugin;

import net.minecraft.server.v1_12_R1.Block;

public class BlastResistanceOverride extends JavaPlugin{
	
	private HashSet<BlockOverride> overrideSet = new HashSet<BlockOverride>();
	
	@Override
	public void onEnable() {
		this.saveDefaultConfig();
		getConfig().options().copyDefaults(true);
		if(getConfig().getConfigurationSection("OverrideValues") == null) {
			getLogger().info("No override values found.");
			return;
		}
		for(String key : getConfig().getConfigurationSection("OverrideValues").getKeys(false)) {
		    BlockOverride bo = new BlockOverride(Block.getById(Integer.parseInt(key)));
		    bo.set("durability", 5.0f * Float.parseFloat(getConfig().getString("OverrideValues." + key)));
		    overrideSet.add(bo);
		}
	}
	
	@Override
	public void onDisable() {
		for(BlockOverride bo : overrideSet) {
			bo.revertAll();
		}
	}
	
}
