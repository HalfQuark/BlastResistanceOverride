package me.halfquark.blastresistanceoverride;

import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.HashSet;

public class BlastResistanceOverride extends JavaPlugin{

    private HashSet<BlockOverride> overrideSet = new HashSet<BlockOverride>();
    private ResistFile overrideDur;
    private static String version;
    private String fieldName;

    @Override
    public void onEnable(){

        String packageName = this.getServer().getClass().getPackage().getName();
        version = packageName.substring(packageName.lastIndexOf('.') + 1);
        fieldName = FieldMappings.getFieldName();

        saveResource("overrides.resist");
        saveResource("defaults.resist");
        overrideDur = new ResistFile(getDataFolder(), "overrides.resist");
        try {
            for (Material mat : overrideDur.getMap().keySet()) {
                BlockOverride bo = new BlockOverride(mat);
                if (!bo.isValid()){
                    getLogger().warning("Block \"" + mat.name() + "\" is not applicable");
                    continue;
                }
                bo.set(fieldName, overrideDur.getMap().get(mat));
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

    public static String getVersion(){return version;}

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
                BlockOverride bo = new BlockOverride(mat);
                if (!bo.isValid())
                    continue;
                Float d = (Float) bo.get(fieldName);
                bw.write(mat.name() + "=" + d);
                bw.newLine();
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        bw.close();
    }

}
