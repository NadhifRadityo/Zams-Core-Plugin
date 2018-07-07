package io.github.NadhifRadityo.ZamsNetwork.Core.Helper;

import java.util.ArrayList;
import java.util.List;

import io.github.NadhifRadityo.ZamsNetwork.Core.Object.Input;

public class InputChatHelper {
	private List<Input> waitingInput = new ArrayList<Input>();
	
	public List<Input> getAllWaitingInput(){
		return waitingInput;
	}
	
	public void addWaitingInput(Input input) {
		if(!this.waitingInput.contains(input)) {
			this.waitingInput.add(input);
		}
	}
	public void removeWaitingInput(Input input) {
		if(this.waitingInput.contains(input)) {
			this.waitingInput.remove(input);
		}
	}
}
