package io.github.NadhifRadityo.ZamsNetwork.Core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import io.github.NadhifRadityo.ZamsNetwork.Core.CustomEvents.GetEventsHandlerClass;
import io.github.NadhifRadityo.ZamsNetwork.Main.Main;

public class Initialize {
	private Main Plugin;
	boolean isInit = false;
	public Map<String, Object[]> initList;
	public GetCommandExecutorClass[] ClassList;
	
	public void init(Main plugins) {
		this.Plugin = plugins;
		this.isInit = true;
		this.Plugin.Commands.Initialize(this.Plugin);
	}
	
	public void setInit(Map<String, Object[]> inits) {
		this.initList = inits;
		this.ClassList = new GetCommandExecutorClass[this.initList.size()];
	}
	
//	public void initAll() {
//		if(this.isInit == false) {
//			this.Plugin.Helper.Throw("Class Initialize not Initialize!");
//		}
//		int index = 0;
//		for(String[] init : this.initList) {
//			String Command = init[0];
//			String ClassName;
//			if(Command.split("")[0].contains("/")) {
//				ClassName = init[1];
//			} else {
//				ClassName = init[0];
//			}
//			GetCommandExecutorClass getClass = this.Plugin.Helper.getCommandExecutorClass(ClassName);
//			getClass.initMain(this.Plugin);
//			
//			if(getClass.isInit() == false) {
//				this.Plugin.Helper.Throw("Class: '" + getClass.getClass().getName() + "' Belum di Init! IsInit: " + getClass.isInit());
//			}
//			
//			this.ClassList[index] = getClass;
//			index++;
//		}
//		
//		this.Plugin.Commands.Initialize(this.Plugin);
//		this.Plugin.Commands.addCommands(this.initList, this.ClassList);
//		this.Plugin.Commands.RegisterEvents();
//	}
	
	@SuppressWarnings("rawtypes")
	public void initAll() {
		Map<String, GetCommandExecutorClass> CommandList = new HashMap<String, GetCommandExecutorClass>();
		Map<String, GetEventsHandlerClass> EventsList = new HashMap<String, GetEventsHandlerClass>();
		
		Iterator it = this.initList.entrySet().iterator();
	    while (it.hasNext()) {
	    	Map.Entry pair = (Map.Entry)it.next();
			
			String command = pair.getKey() != null ? pair.getKey().toString() : null;
			Object[] contents = (Object[]) pair.getValue();
			
			String CommandClass = contents[0] != null ? contents[0].toString() : null;
			String EventsClass = contents[1] != null ? contents[1].toString() : null;
			
			if(CommandClass != null && command != null) {
				GetCommandExecutorClass getCommandClass = this.Plugin.Helper.getCommandExecutorClass(CommandClass);
				if(!getCommandClass.initMain(this.Plugin) || !getCommandClass.isInit()) {
					this.Plugin.Helper.Throw("Class: '" + getCommandClass.getClass().getName() + "' Belum di Init! IsInit: " + getCommandClass.isInit());
				}
				CommandList.put(command, getCommandClass);
			}
			if(EventsClass != null) {
				GetEventsHandlerClass getEventsClass = this.Plugin.Helper.getEventsClass(EventsClass);
				if(!getEventsClass.initMain(this.Plugin) || !getEventsClass.isInit()) {
					this.Plugin.Helper.Throw("Class: '" + getEventsClass.getClass().getName() + "' Belum di Init! IsInit: " + getEventsClass.isInit());
				}
				EventsList.put(command, getEventsClass);
			}
		}
		
		this.Plugin.Commands.Initialize(this.Plugin);
		this.Plugin.Commands.setCommands(CommandList);
		this.Plugin.Commands.RegisterEvents();
		
		this.Plugin.Events.Initialize(this.Plugin);
		this.Plugin.Events.setEvents(EventsList);
		this.Plugin.Events.registerEvents();
	}
}
