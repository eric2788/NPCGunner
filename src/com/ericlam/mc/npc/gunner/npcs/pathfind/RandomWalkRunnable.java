package com.ericlam.mc.npc.gunner.npcs.pathfind;

import com.ericlam.utils.Utils;
import net.citizensnpcs.api.ai.TeleportStuckAction;
import net.citizensnpcs.api.ai.goals.MoveToGoal;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class RandomWalkRunnable extends BukkitRunnable {


    private final NPC npc;
    private final Random random;
    private boolean attacking;

    public void setAttacking(boolean attacking) {
        if (this.attacking == attacking) return;
        this.attacking = attacking;
        if (this.attacking) npc.getNavigator().cancelNavigation();
    }

    public boolean isAttacking() {
        return attacking;
    }

    public RandomWalkRunnable(NPC npc) {
        this.npc = npc;
        this.random = new Random();
    }

    @Override
    public void run() {
        if (!npc.isSpawned()) return;
        if (attacking) return;
        MoveToGoal goal = new MoveToGoal(npc,randomLocation());
        if (goal.shouldExecute()) goal.run();
        if (isWalled(npc.getEntity().getLocation())) TeleportStuckAction.INSTANCE.run(npc,npc.getNavigator());
    }

    private Location randomLocation(){
        int radius = 10;
        double x = Utils.randomWithRange(4, radius) * (random.nextBoolean() ? -1 : 1);
        double z = Utils.randomWithRange(4, radius) * (random.nextBoolean() ? -1 : 1);
        Entity entity = npc.getEntity();
        return new Location(entity.getWorld(),entity.getLocation().getX()+x,entity.getLocation().getY(),entity.getLocation().getZ()+z);
    }

    private boolean isWalled(Location blockLoc){
        World world = blockLoc.getWorld();
        double ox = blockLoc.getX();
        double oy = blockLoc.getY();
        double oz = blockLoc.getZ();
        double Bx = ox - 1.0D;
        double Bz = oz - 1.0D;
        double Fx = ox + 1.0D;
        double Fz = oz + 1.0D;
        boolean front = isNotAir(new Location(world, Fx, oy, oz));
        boolean back = isNotAir(new Location(world, Bx, oy, oz));
        boolean right = isNotAir(new Location(world, ox, oy, Fz));
        boolean left = isNotAir(new Location(world, ox, oy, Bz));
        return front && back && right && left;
    }

    private boolean isNotAir(Location loc) {
        return loc.getBlock() != null && loc.getBlock().getType() != Material.AIR;
    }
}
