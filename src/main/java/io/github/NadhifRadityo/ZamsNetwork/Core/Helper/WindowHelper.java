package io.github.NadhifRadityo.ZamsNetwork.Core.Helper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.github.NadhifRadityo.ZamsNetwork.Core.OutOfGame.Object.FrameWindow;

public class WindowHelper {
	private List<FrameWindow> windowList = new ArrayList<FrameWindow>();
	private List<FrameWindow> openedWindow = new ArrayList<FrameWindow>();
	
	public List<FrameWindow> getAllWindow(){
		return windowList;
	}
	
	public void addFrameWindow(FrameWindow window) {
		if(!this.windowList.contains(window)) {
			this.windowList.add(window);
		}
	}
	
	public void removeFrameWindow(FrameWindow window) {
		if(this.windowList.contains(window)) {
			this.windowList.remove(window);
		}
	}
	
	public List<FrameWindow> getAllOpenedWindow(){
		return openedWindow;
	}
	
	public void addOpenedFrameWindow(FrameWindow window) {
		if(!this.openedWindow.contains(window)) {
			this.openedWindow.add(window);
		}
	}
	
	public void removeOpenedFrameWindow(FrameWindow window) {
		if(this.openedWindow.contains(window)) {
			this.openedWindow.remove(window);
		}
	}
	
	public void closeAllWindows() {
		if(this.openedWindow == null) {
			return;
		}
		System.out.println("Menutup window...");
		
		for (Iterator<FrameWindow> iterator = this.openedWindow.iterator(); iterator.hasNext(); ) {
		    FrameWindow value = iterator.next();
		    iterator.remove();
		    System.out.println("Menutup window: " + value.getTitle());
		    value.close();
		}
	}
}
