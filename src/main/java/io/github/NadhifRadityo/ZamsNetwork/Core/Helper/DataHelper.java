package io.github.NadhifRadityo.ZamsNetwork.Core.Helper;

public class DataHelper {
	public boolean isEmptyArrayInt(int[] arr) {
		boolean empty = true;
		for (int value : arr) {
		  if (value != 0) {
		    empty = false;
		    break;
		  }
		}
		return empty;
	}
	public boolean isEmptyArrayString(String[] arr) {
		boolean empty = true;
		for (String value : arr) {
		  if (value != null) {
		    empty = false;
		    break;
		  }
		}
		return empty;
	}
}
