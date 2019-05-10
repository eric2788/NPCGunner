package com.ericlam.mc.npc.gunner.npcs.grenade;

import com.ericlam.mc.npc.gunner.main.NPCGunner;
import com.ericlam.mc.npc.gunner.main.Utils;
import com.shampaggon.crackshot.CSDirector;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.mcmonkey.sentinel.SentinelIntegration;
import org.mcmonkey.sentinel.SentinelTrait;

public class SentinelGrenade extends SentinelIntegration {
    @Override
    public boolean tryAttack(SentinelTrait st, LivingEntity ent) {
        if (!(st.getLivingEntity() instanceof Player)) {
            return false;
        } else {
            CSDirector direc = NPCGunner.getCsAPI();
            String node = direc.returnParentNode((Player) st.getLivingEntity());
            if (node == null || !Utils.isExplosiveWeapon(node)) {
                return false;
            } else {
                Vector faceAcc = ent.getEyeLocation().toVector().subtract(st.getLivingEntity().getEyeLocation().toVector());
                if (faceAcc.lengthSquared() > 0.0D) {
                    faceAcc = faceAcc.normalize();
                }

                faceAcc = st.fixForAcc(faceAcc);
                Location eyeloc = st.getLivingEntity().getEyeLocation();
                eyeloc.setPitch(eyeloc.getPitch() - 3);
                st.faceLocation(st.getLivingEntity().getEyeLocation().clone().add(faceAcc.multiply(10)));
                ItemStack itm = ((Player) st.getLivingEntity()).getInventory().getItemInMainHand();
                direc.csminion.weaponInteraction((Player) st.getLivingEntity(), node, false);
                ((Player) st.getLivingEntity()).getInventory().setItemInMainHand(itm);
                if (st.rangedChase) {
                    st.attackHelper.rechase();
                }
                return true;
            }
        }
    }
}
