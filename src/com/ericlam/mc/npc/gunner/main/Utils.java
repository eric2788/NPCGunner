package com.ericlam.mc.npc.gunner.main;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Utils {
    public static boolean isExplosiveWeapon(String node) {
        File file = NPCGunner.getCsAPI().csminion.getDefaultConfig("weapons/defaultExplosives.yml");
        return YamlConfiguration.loadConfiguration(file).contains(node);
    }

    public static boolean isNormalWeapon(String node) {
        File file = NPCGunner.getCsAPI().csminion.getDefaultConfig("weapons/defaultWeapons.yml");
        return YamlConfiguration.loadConfiguration(file).contains(node);
    }
}
