package io.github.NadhifRadityo.ZamsNetwork.Core.Object;

public interface Destroyable {
	public boolean isDestroyed = false;
	public boolean autoDestroy = true;
	public boolean isDestroyed();
	public boolean isAutoDestroy();
	public void setAutoDestroy(boolean autoDestroy);
	public void checkAutoDestroy();
	public void destroy();
}
