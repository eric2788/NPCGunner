package com.ericlam.mc.npc.gunner.command;

import com.ericlam.managers.command.CommandHandle;
import com.ericlam.managers.command.exception.ArgTooShortException;
import com.ericlam.managers.command.exception.CommandNotFoundException;
import com.ericlam.managers.command.exception.NoPermissionException;
import com.ericlam.mc.npc.gunner.main.NPCGunner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.List;

public class NPCGunnerCommand implements CommandExecutor, TabCompleter {

    private NPCGunner gunner;

    public NPCGunnerCommand(NPCGunner gunner) {
        this.gunner = gunner;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        try {
            return CommandHandle.handle(commandSender,command,strings,gunner);
        } catch (ArgTooShortException e) {
            commandSender.sendMessage("§c參數太短");
        } catch (CommandNotFoundException e) {
            commandSender.sendMessage("§c找不到指令 /"+e.getMessage());
        } catch (NoPermissionException e) {
            commandSender.sendMessage("§c沒有以下權限");
            commandSender.sendMessage(e.getPermissions());
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return CommandHandle.tapComplete(commandSender,command,strings,gunner);
    }
}
