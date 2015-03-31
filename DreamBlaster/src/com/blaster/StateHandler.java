package com.blaster;

import java.util.HashMap;
import java.util.Map;

import com.blaster.data.StateId;
import com.blaster.state.ScriptState;

public class StateHandler {
	
	private Map<StateId, ScriptState> states;
	private ScriptState state;
	private StateId id;
	
	public StateHandler(ScriptState... nodes) {
		states = new HashMap<StateId, ScriptState>();
		
		for (ScriptState n : nodes) {
			submit(n);
		}
	}
	
	public void submit(ScriptState n) {
		states.put(n.getId(), n);
		if (state == null) {
			set(n.getId());
		}
	}
	
	public void set(StateId s) {
		if (s == StateId.NULL) {
			return;
		}
		id = s;
		state = states.get(s);
	}

}