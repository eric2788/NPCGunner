package com.ericlam.mc.npc.gunner.command.subcommand;

import com.ericlam.managers.command.SubCommand;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nullable;

public class TphereAsyncCommand extends SubCommand {

    public TphereAsyncCommand(Plugin plugin) {
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
        return "/npcg tphere";
    }

    @Nullable
    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public String getName() {
        return "tphere";
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
        npc.getEntity().teleportAsync(player.getLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
    }
}
