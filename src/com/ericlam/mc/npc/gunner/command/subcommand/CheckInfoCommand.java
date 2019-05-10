package com.ericlam.mc.npc.gunner.command.subcommand;

import com.ericlam.managers.command.SubCommand;
import com.ericlam.mc.npc.gunner.main.NPCGunner;
import com.ericlam.mc.npc.gunner.npcs.pathfind.RandomWalkRunnable;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nullable;

public class CheckInfoCommand extends SubCommand {

    public CheckInfoCommand(Plugin plugin) {
        super(plugin);
    }

    @Override
    public String getParentCommand() {
        return "npcg";
    }

    @Override
    public int getArgs() {
        return 0;
    }

    @Override
    public String getHelpMessages() {
        return "/npcg info";
    }

    @Nullable
    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public boolean runAsync() {
        return false;
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        NPC npc = CitizensAPI.getDefaultNPCSelector().getSelected(commandSender);
        if (npc == null){
            commandSender.sendMessage("§c你沒有選擇npc");
            return;
        }
        if (!NPCGunner.getRandomWalking().containsKey(npc.getId())){
            commandSender.sendMessage("§c該npc不在隨機遊走列表內");
            return;
        }
        if (!npc.isSpawned()) {
            commandSender.sendMessage("§c該 NPC 尚未重生！");
            return;
        }
        RandomWalkRunnable runnable = NPCGunner.getRandomWalking().get(npc.getId());
        commandSender.sendMessage("§eNPC " + npc.getFullName() + ": ");
        commandSender.sendMessage("§eAttacking: " + runnable.isAttacking());
        commandSender.sendMessage("§ePaused: " + npc.getNavigator().isPaused());
        commandSender.sendMessage("§eNavigating: " + npc.getNavigator().isNavigating());
    }
}
