package io.github.NadhifRadityo.ZamsNetwork.Core.Initialize.Commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandAttribute {

	private String usage;
	private String description;
	private String permission;
	private String permissionMessage;
	private String[] alliases;
	private Map<Integer, String> tabComplete;
	
	public CommandAttribute(String usage, String description, String permission, String permissionMessage, String[] alliases, Map<Integer, String> tabComplete) {
		this.usage = usage;
		this.description = description;
		this.permission = permission;
		this.permissionMessage = permission;
		this.alliases = alliases;
		this.tabComplete = tabComplete;
	}
	
	public CommandAttribute() {
		
	}
	
	public CommandAttribute setUsage(String usage) {
		this.usage = usage;
		return this;
	}
	public CommandAttribute setDescription(String desc) {
		this.description = desc;
		return this;
	}
	public CommandAttribute setPermission(String permission) {
		this.permission = permission;
		return this;
	}
	public CommandAttribute setPermissionMessage(String permMessage) {
		this.permissionMessage = permMessage;
		return this;
	}
	public CommandAttribute setAlliases(String[] alliases) {
		this.alliases = alliases;
		return this;
	}
	public CommandAttribute setTabComplete(Map<Integer, String> tabComplete) {
		this.tabComplete = tabComplete;
		return this;
	}
	
	public CommandAttribute addAlliases(String add) {
		if(this.alliases == null) {
			this.alliases = new String[] {};
		}
		
		List<String> alliasesList = new ArrayList<String>(Arrays.asList(this.alliases));
		alliasesList.add(add);
		this.alliases = alliasesList.toArray(new String[alliasesList.size()]);
		return this;
	}
	
	public CommandAttribute addTabComplete(int index, String tabComplete) {
		if(this.tabComplete == null) {
			this.tabComplete = new HashMap<Integer, String>();
		}
		
		this.tabComplete.put(index, tabComplete);
		return this;
	}
	
	public String getUsage() {
		return usage;
	}
	public String getDescription() {
		return description;
	}
	public String getPermission() {
		return permission;
	}
	public String getPermissionMessage() {
		return permissionMessage;
	}
	public String[] getAlliases() {
		return alliases;
	}
	public Map<Integer, String> getTabComplete(){
		return tabComplete;
	}
}
