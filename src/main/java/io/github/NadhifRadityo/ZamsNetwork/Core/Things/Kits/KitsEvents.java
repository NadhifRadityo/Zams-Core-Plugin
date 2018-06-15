package io.github.NadhifRadityo.ZamsNetwork.Core.Things.Kits;

import io.github.NadhifRadityo.ZamsNetwork.Core.CustomEvents.CustomEventsListener;
import io.github.NadhifRadityo.ZamsNetwork.Main.Main;

public class KitsEvents implements CustomEventsListener{
	private Main Plugin;
	private boolean isInit = false;
	
	@Override
	public boolean initMain(Main plugins) {
		this.Plugin = plugins;
		this.isInit = true;
		return this.isInit;
	}

	@Override
	public boolean isInit() {
		return this.isInit;
	}
	
}
