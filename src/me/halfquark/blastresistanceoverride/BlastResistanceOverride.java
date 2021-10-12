package me.halfquark.blastresistanceoverride;

import me.halfquark.blastresistanceoverride.blockoverride.BlockOverride;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;

public class BlastResistanceOverride extends JavaPlugin{

    private HashSet<BlockOverride> overrideSet = new HashSet<BlockOverride>();
    private ResistFile overrideDur;
    private Class<?> BOClass;

    @Override
    public void onEnable(){

        // Get version specific nms classes. Code from:
        // https://github.com/APDevTeam/Movecraft/blob/main/modules/Movecraft/src/main/java/net/countercraft/movecraft/Movecraft.java.
        String packageName = this.getServer().getClass().getPackage().getName();
        String version = packageName.substring(packageName.lastIndexOf('.') + 1);
        try {
            BOClass = Class.forName("me.halfquark.blastresistanceoverride.blockoverride." + version + "BlockOverride");
        } catch (final Exception e) {
            e.printStackTrace();
            this.getLogger().severe("Startup - Version Not Supported");
            this.setEnabled(false);
            return;
        }

        saveResource("overrides.resist");
        saveResource("defaults.resist");
        overrideDur = new ResistFile(getDataFolder(), "overrides.resist");
        try {
            for (Material mat : overrideDur.getMap().keySet()) {
                BlockOverride bo = (BlockOverride) BOClass.getDeclaredConstructor(Material.class).newInstance(mat);
                bo.set("durability", overrideDur.getMap().get(mat));
                overrideSet.add(bo);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable(){
        for(BlockOverride bo : overrideSet) {
            bo.revertAll();
        }
    }

    private void saveResource(String path){
        File f = new File(getDataFolder(), path);
        if(!f.exists()){
            this.saveResource(path, false);
        }
    }

    private void copyDefaultValues() throws IOException {
        File fout = new File(getDataFolder(), "defaults.resist");
        FileOutputStream fos = new FileOutputStream(fout);

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

        bw.write("//Native minecraft blast resistance values");
        bw.newLine();
        try {
            for (Material mat : Material.values()) {
                BlockOverride bo = (BlockOverride) BOClass.getConstructor().newInstance(mat);
                if (!bo.isValid())
                    continue;
                Float d = (Float) bo.get("durability");
                bw.write(mat.name() + "=" + String.valueOf(d));
                bw.newLine();
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        bw.close();
    }

}
