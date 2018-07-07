package io.github.NadhifRadityo.ZamsNetwork.Core.Initialize;

import java.util.List;
import java.util.Map;

import io.github.NadhifRadityo.ZamsNetwork.Core.Initialize.Commands.CommandAttribute;

public class Init {
	private String command;
	private String mainClassName;
	private String[] listenerClasses;
	private String configName;
	private CommandAttribute commandAttribute;
	
	public Init(String command, String mainClass, String[] listenerClasses, String configName) {
		this.command = command;
		this.mainClassName = mainClass;
		this.listenerClasses = listenerClasses;
		this.configName = configName;
	}
	public Init(String command, String mainClass) {
		this.command = command;
		this.mainClassName = mainClass;
		this.listenerClasses = null;
		this.configName = null;
	}
	public Init(String command, String mainClass, String configName) {
		this.command = command;
		this.mainClassName = mainClass;
		this.listenerClasses = null;
		this.configName = configName;
	}
	
	public Init(String command, String mainClass, List<String> listenerClasses, String configName) {
		this(command, mainClass, listenerClasses.toArray(new String[listenerClasses.size()]), configName);
	}

	public Init(String command, String mainClass, String[] listenerClasses) {
		this(command, mainClass, listenerClasses, null);
	}
	
	public Init setCommandAttribute(CommandAttribute attr) {
		this.commandAttribute = attr;
		return this;
	}
	public Init setCommandAttribute(String usage, String description, String permission, String permissionMessage, String[] alliases, Map<Integer, String> tabComplete) {
		this.commandAttribute = new CommandAttribute(usage, description, permission, permissionMessage, alliases, tabComplete);
		return this;
	}
	
	public String getCommand() {
		return command;
	}
	public String getMainClassName() {
		return mainClassName;
	}
	public String[] getListeners() {
		return listenerClasses;
	}
	public String getConfig() {
		return configName;
	}
	public CommandAttribute getCommandAttribute() {
		return commandAttribute;
	}
	
}
