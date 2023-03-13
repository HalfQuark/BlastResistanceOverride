package me.halfquark.blastresistanceoverride;

import org.bukkit.Bukkit;

public class FieldMappings {

    static String getFieldName(){
        String gameVersion = Bukkit.getBukkitVersion().split("-")[0];
        if(gameVersion.startsWith("1.14") || gameVersion.startsWith("1.15") || gameVersion.startsWith("1.16"))
            return "durability";
        if(gameVersion.startsWith("1.17") || gameVersion.equals("1.18") || gameVersion.equals("1.18.1"))
            return "aI";
        return "aH";
    }

}
