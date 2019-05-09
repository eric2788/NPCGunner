package com.ericlam.mc.npc.gunner.main;

import com.ericlam.managers.command.CommandManager;
import com.ericlam.mc.npc.gunner.command.NPCGunnerCommand;
import com.ericlam.mc.npc.gunner.command.subcommand.CheckAttackingCommand;
import com.ericlam.mc.npc.gunner.listeners.GunnerListener;
import com.ericlam.mc.npc.gunner.npcs.pathfind.RandomWalkRunnable;
import com.shampaggon.crackshot.CSDirector;
import com.shampaggon.crackshot.CSMinion;
import com.shampaggon.crackshot.CSUtility;
import me.DeeCaaD.CrackShotPlus.API;
import net.citizensnpcs.api.event.NPCSpawnEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

public class NPCGunner extends JavaPlugin implements Listener {

    private static ConfigManager configManager;

    private static CSDirector csAPI;

    private static CSMinion csMinion;

    private static CSUtility csUtility;

    private static Map<Integer,RandomWalkRunnable> randomWalking = new HashMap<>();

    public static Map<Integer, RandomWalkRunnable> getRandomWalking() {
        return randomWalking;
    }

    public static CSDirector getCsAPI() {
        return csAPI;
    }

    public static CSUtility getCsUtility() {
        return csUtility;
    }

    public static CSMinion getCsMinion() {
        return csMinion;
    }

    public static ConfigManager getConfigManager() {
        return configManager;
    }

    public static Object getConfigData(String path){
        return configManager.getConfig("config.yml").get(path);
    }

    private static NPCGunner plugin;

    public static NPCGunner getPlugin() {
        return plugin;
    }

    private ConcurrentLinkedDeque<Integer> randoms;

    @Override
    public void onEnable() {
        plugin = this;
        configManager = new ConfigManager(this,"config.yml");
        csUtility = new CSUtility();
        csAPI = csUtility.getHandle();
        csMinion = API.getCSMinion();
        this.getServer().getPluginManager().registerEvents(new GunnerListener(),this);
        this.getServer().getPluginManager().registerEvents(this,this);
        this.getCommand("npcg").setExecutor(new NPCGunnerCommand(this));
        CommandManager.getInstance().registerCommand(new CheckAttackingCommand(this));
        randoms = configManager.getConfig("config.yml").getStringList("random-walking-npcs").stream().map(Integer::parseInt).distinct().collect(Collectors.toCollection(ConcurrentLinkedDeque::new));
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    @EventHandler
    public void onNPCSpawn(NPCSpawnEvent e){
        NPC npc = e.getNPC();
        Bukkit.broadcastMessage(npc.getFullName()+" has spawn !");
        Bukkit.broadcastMessage(npc.getId()+" id");
        if (!randoms.contains(npc.getId())) return;
        int id = randoms.poll();
        RandomWalkRunnable runnable = new RandomWalkRunnable(npc);
        runnable.runTaskTimer(this,0L,30L);
        randomWalking.put(id,runnable);
    }
}
