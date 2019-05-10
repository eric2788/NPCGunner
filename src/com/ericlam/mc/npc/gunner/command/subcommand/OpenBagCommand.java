package com.ericlam.mc.npc.gunner.command.subcommand;

import com.ericlam.managers.command.SubCommand;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nullable;

public class OpenBagCommand extends SubCommand {

    public OpenBagCommand(Plugin plugin) {
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
        return "/npcg openbag";
    }

    @Nullable
    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public String getName() {
        return "openbag";
    }

    @Override
    public boolean runAsync() {
        return false;
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("not player!");
            return;
        }
        Player player = (Player) commandSender;
        NPC npc = CitizensAPI.getDefaultNPCSelector().getSelected(commandSender);
        if (npc == null) {
            commandSender.sendMessage("§c你沒有選擇npc");
            return;
        }
        if (!npc.isSpawned()) {
            commandSender.sendMessage("§c該 NPC 尚未重生！");
            return;
        }
        Entity entity = npc.getEntity();
        if (!(entity instanceof HumanEntity)) {
            commandSender.sendMessage("§c該 npc 不是玩家形態!");
            return;
        }

        HumanEntity npcPlayer = (HumanEntity) entity;
        player.openInventory(npcPlayer.getInventory());
    }
}
