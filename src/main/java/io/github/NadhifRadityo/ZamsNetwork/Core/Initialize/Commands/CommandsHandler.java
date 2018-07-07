package io.github.NadhifRadityo.ZamsNetwork.Core.Initialize.Commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import javax.annotation.Nullable;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandsHandler extends Command{
    
    private CommandExecutor exe = null;
    
    @Nullable
    protected CommandsHandler(String name, String usage, String description, String permission, String permissionMessage, String[] alliases, Map<Integer, String> tabComplete) {
        super(name);
        if(usage != null) {
        	this.usageMessage = usage;
        }
        if(description != null) {
        	this.description = description;
        }
        if(permission != null) {
        	this.setPermission(permission);
        }
        if(permissionMessage != null) {
        	this.setPermissionMessage(permissionMessage);
        }
        if(alliases != null) {
        	this.setAliases(new ArrayList<String>(Arrays.asList(alliases)));
        }
    }
    protected CommandsHandler(String name) {
        super(name);
    }

    public boolean execute(CommandSender sender, String commandLabel,String[] args) {
        if(exe != null){
            exe.onCommand(sender, this, commandLabel,args);
        }
        return false;
    }
    
    public void setExecutor(CommandExecutor exe){
        this.exe = exe;
    }
}