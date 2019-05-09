package com.ericlam.mc.npc.gunner.listeners;

import com.ericlam.mc.npc.gunner.main.NPCGunner;
import com.ericlam.mc.npc.gunner.npcs.pathfind.RandomWalkRunnable;
import com.ericlam.mc.npc.gunner.npcs.schema.Attrubutes;
import com.shampaggon.crackshot.CSDirector;
import com.shampaggon.crackshot.CSMinion;
import com.shampaggon.crackshot.CSUtility;
import com.shampaggon.crackshot.events.WeaponReloadCompleteEvent;
import com.shampaggon.crackshot.events.WeaponReloadEvent;
import me.DeeCaaD.CrackShotPlus.Events.WeaponCustomBulletSpreadEvent;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.mcmonkey.sentinel.SentinelTrait;
import org.mcmonkey.sentinel.SentinelUtilities;
import org.mcmonkey.sentinel.events.SentinelAttackEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GunnerListener implements Listener {

    private CSDirector csDirector;
    private CSUtility csUtility;
    private CSMinion csMinion;
    private Set<NPC> reloading = new HashSet<>();
    private Map<Integer, RandomWalkRunnable> randomMap;
    private Map<NPC, Attrubutes> npcAtt = new HashMap<>();

    public GunnerListener(){
        csDirector = NPCGunner.getCsAPI();
        csUtility = NPCGunner.getCsUtility();
        csMinion = NPCGunner.getCsMinion();
        randomMap = NPCGunner.getRandomWalking();
    }

    @EventHandler
    public void onNPCNoAmmo(SentinelAttackEvent e){
        Entity entity = e.getNPC().getEntity();
        if (!(entity instanceof Player)) return;
        Player player = (Player) entity;
        PlayerInventory inventory = player.getInventory();
        Attrubutes attr = npcAtt.computeIfAbsent(e.getNPC(),(k)->new Attrubutes());
        if (attr.addVision()){
            e.setCancelled(true);
            e.getNPC().getNavigator().cancelNavigation();
            Bukkit.broadcastMessage("Vision is now "+attr.getVision()+"/15");
            Bukkit.getScheduler().runTaskLater(NPCGunner.getPlugin(),()->{
                if (attr.getAtkcount() == 0){
                    attr.reduceVision();
                    Bukkit.broadcastMessage("Vision is now "+attr.getVision()+"/15");
                }else{
                    attr.setVision(15);
                }
            },100L);
            return;
        }
        if (randomMap.containsKey(e.getNPC().getId())) {
            RandomWalkRunnable runnable = randomMap.get(e.getNPC().getId());
            if (runnable.isAttacking()){
                attr.addAtkCount();
            }else{
                runnable.setAttacking(true);
                SentinelTrait trait = SentinelUtilities.tryGetSentinel(player);
                trait.realistic = false;
                trait.runUpdate();
            }
            Bukkit.broadcastMessage("Attacking set to true");
            Bukkit.getScheduler().runTaskLater(NPCGunner.getPlugin(),()->{
                if (attr.reduceAtkCount()){
                    runnable.setAttacking(false);
                    attr.setVision(0);
                    Bukkit.broadcastMessage("Attacking set to false");
                    SentinelTrait trait = SentinelUtilities.tryGetSentinel(player);
                    trait.realistic = true;
                    trait.runUpdate();
                }
            },100L);
        }
        ItemStack gun = inventory.getItemInMainHand();
        String title = csUtility.getWeaponTitle(gun);
        if (title == null) return;
        int ammo = csDirector.getAmmoBetweenBrackets(player,title,gun);
        if (ammo != 0) return;
        e.setCancelled(true);
        final String ammoInfo = csDirector.getString(title + ".Ammo.Ammo_Item_ID");
        if (!csMinion.containsItemStack(player,ammoInfo,1,title) && (boolean)NPCGunner.getConfigData("auto-gen-ammo-for-npc")){
            ItemStack ammoItem = csMinion.parseItemStack(ammoInfo);
            ammoItem.setAmount(64);
            player.getInventory().addItem(ammoItem);
            Bukkit.broadcastMessage(e.getNPC().getFullName()+" NPC not enough ammo !! adding some to him...");
        }
        if (reloading.contains(e.getNPC())) return;
        Bukkit.broadcastMessage(e.getNPC().getFullName()+" NPC is reloading !!");
        csDirector.reloadAnimation(player,title,true);
    }

    @EventHandler
    public void onNPCReload(WeaponReloadEvent e){
        NPC npc = CitizensAPI.getNPCRegistry().getNPC(e.getPlayer());
        if (npc == null) return;
        reloading.add(npc);
        npc.getNavigator().setPaused(true);
    }

    @EventHandler
    public void onNPCReloadComplete(WeaponReloadCompleteEvent e){
        NPC npc = CitizensAPI.getNPCRegistry().getNPC(e.getPlayer());
        if (npc == null) return;
        reloading.remove(npc);
        npc.getNavigator().setPaused(false);

    }

    @EventHandler
    public void onBulletSpread(WeaponCustomBulletSpreadEvent e){
        NPC npc = CitizensAPI.getNPCRegistry().getNPC(e.getPlayer());
        if (npc == null) return;
        Bukkit.broadcastMessage(npc.getFullName()+" NPC is shooting !!");
        e.setZXSpread(0);
        e.setYSpread(0);
    }

}
