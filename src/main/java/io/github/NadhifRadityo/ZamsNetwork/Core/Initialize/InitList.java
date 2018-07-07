package io.github.NadhifRadityo.ZamsNetwork.Core.Initialize;

import java.util.ArrayList;
import java.util.List;

public class InitList {
	private List<initPlugin> initList = new ArrayList<initPlugin>();
	
	public void setInitList(List<initPlugin> init) {
		this.initList = init;
	}
	
	public void addInitList(initPlugin init) {
		if(!this.initList.contains(init)) {
			this.initList.add(init);
		}
	}
	
	public void removeInitList(initPlugin init) {
		if(this.initList.contains(init)) {
			this.initList.remove(init);
		}
	}
	
	public void removeInitList(int index) {
		if(this.initList.get(index) != null) {
			this.initList.remove(index);
		}
	}
	
	public List<initPlugin> getInitList(){
		return initList;
	}
	
	public boolean contains(initPlugin init) {
		return this.initList.contains(init);
	}
}
