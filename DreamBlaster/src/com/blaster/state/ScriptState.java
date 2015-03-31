package com.blaster.state;

import java.util.HashMap;
import java.util.Map;

import com.blaster.DreamBlaster;
import com.blaster.data.StateId;
import com.blaster.data.TransitionId;

public abstract class ScriptState {
	private Map<TransitionId, StateId> transitions;
	protected StateId id = StateId.NULL;
	protected DreamBlaster ctx;

	public ScriptState(DreamBlaster ctx) {
		this.ctx = ctx;
		transitions = new HashMap<TransitionId, StateId>();
	}
	
	public StateId getId() {
		return id;
	}
	
	public StateId getState(TransitionId transition) {
		if (transitions.containsKey(transition)) {
			return transitions.get(transition);
		}
		return StateId.NULL;
	}
	
	public void addTransition(TransitionId transition, StateId state) {
		if (state == null) {
			return;
		}
		transitions.put(transition, state);
	}
	
	public abstract TransitionId execute();
	public abstract boolean activate();

}