package io.github.NadhifRadityo.ZamsNetwork.Core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

import io.github.NadhifRadityo.ZamsNetwork.Core.CustomEvents.CustomEventsListener;
import io.github.NadhifRadityo.ZamsNetwork.Core.CustomEvents.GetCustomEvents;
import io.github.NadhifRadityo.ZamsNetwork.Core.CustomEvents.GetEventsHandlerClass;
import io.github.NadhifRadityo.ZamsNetwork.Main.Main;

public class Events {
	private Main Plugin;
	private boolean isInit = false;
	private Map<String, GetEventsHandlerClass> EventsList;

	public void Initialize(Main Plugins) {
		this.Plugin = Plugins;
		this.isInit = true;
	}
	
	public void setEvents(Map<String, GetEventsHandlerClass> events) {
		this.EventsList = events;
	}
	
	@SuppressWarnings("rawtypes")
	public void registerEvents() {
		if(this.isInit == false) {
			this.Plugin.Helper.Throw("Events Class not initialize");
		}
		Iterator it = this.EventsList.entrySet().iterator();
	    while (it.hasNext()) {
	    	Map.Entry pair = (Map.Entry)it.next();
	    	it.remove();
	    	
			CustomEventsListener EventClass = (CustomEventsListener) pair.getValue();
			if(EventClass != null)
			
			EventClass.initMain(this.Plugin);
			if(EventClass.isInit() == false) {
				this.Plugin.Helper.Throw("Class '" + EventClass.getClass().getName() + "' not initialize");
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	public void eventCaller(String className, String methodName, Object data) {

		Iterator it = this.EventsList.entrySet().iterator();
	    while (it.hasNext()) {
	    	Map.Entry pair = (Map.Entry)it.next();
	    	it.remove();
	    	
			GetEventsHandlerClass EventClass = (GetEventsHandlerClass) pair.getValue();
			if(EventClass.getClass().getName().equals(className));
			
			Method[] methods = EventClass.getClass().getMethods();
			for(Method method : methods) {
				if(method.isAnnotationPresent(GetCustomEvents.class)) {
					if(method.getName().equals(methodName)) {
						try {
							method.invoke(EventClass, data);
						} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
	    }
	}
	
	public void sendEvent(String className, String methodName, Object events) {
		Reflections reflections = new Reflections("io.github.NadhifRadityo.ZamsNetwork");    
		Set<Class<? extends CustomEventsListener>> classes = reflections.getSubTypesOf(CustomEventsListener.class);
		classes.forEach((s) -> {
		     System.out.println(s);
		});
//		GetEventsClass getEventsClass = this.Plugin.Helper.getEventsClass(EventsClass);
	}
	public static void main(String[] args) {
		System.out.println("Started..");
		Reflections reflections = new Reflections("io.github.NadhifRadityo.ZamsNetwork.Core");    
		Set<Class<? extends CustomEventsListener>> classes = reflections.getSubTypesOf(CustomEventsListener.class);
		classes.forEach((s) -> {
		     System.out.println(s);
		});
		System.out.println("Ended..");
	}
}
