package me.halfquark.blastresistanceoverride;

import java.util.ArrayList;
import java.util.HashSet;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.plugin.java.JavaPlugin;

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
			getLogger().info(key);
			getLogger().info(getBlocks(key).toString());
			for(Material mat : getBlocks(key)) {
				BlockOverride bo = new BlockOverride(mat);
				bo.set("durability", (short) (getConfig().getInt("OverrideValues." + key)));
				overrideSet.add(bo);
				getLogger().info(key + " has been successfully registered.");
			}
		}
	}
	
    public static ArrayList<Material> getBlocks(String name){
        ArrayList<Material> blocks = new ArrayList<>();
        if(name.startsWith("#")){
            Iterable<Tag<Material>> tags = Bukkit.getTags(Tag.REGISTRY_BLOCKS, Material.class);
            for(Tag<Material> tag : tags){
                if(tag.getKey().toString().equals(name.substring(1))){
                    blocks.addAll(tag.getValues());
                    return blocks;
                } 
            }
        }else{
            blocks.add(Material.matchMaterial(name));
        }
        return blocks;
    }
	
	@Override
	public void onDisable() {
		for(BlockOverride bo : overrideSet) {
			bo.revertAll();
		}
	}
	
}
