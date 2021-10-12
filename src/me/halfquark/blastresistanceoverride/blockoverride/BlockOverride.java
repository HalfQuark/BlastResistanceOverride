package me.halfquark.blastresistanceoverride.blockoverride;

import com.google.common.collect.ImmutableMap;
import org.bukkit.Material;

import java.lang.reflect.Field;

public abstract class BlockOverride {

    public abstract boolean isValid();
    public abstract void set(String fieldName, Object object);
    public abstract Object get(String fieldName);
    public abstract Object getVanilla(String fieldName);
    public abstract ImmutableMap<String, Object> getVanillaValues();
    public abstract void revertAll();
    public abstract void saveAll();

}
