package io.github.NadhifRadityo.ZamsNetwork.Core.CustomEvents;

import io.github.NadhifRadityo.ZamsNetwork.Main.Main;

public interface CustomEventsListener {
	public boolean initMain(Main plugins);
	public boolean isInit();
}
