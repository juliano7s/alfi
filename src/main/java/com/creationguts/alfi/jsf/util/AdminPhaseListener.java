package com.creationguts.alfi.jsf.util;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

public class AdminPhaseListener implements PhaseListener {

	
	@Override
	public void afterPhase(PhaseEvent event) {
		//System.out.println("START PHASE " + event.getPhaseId());
	}

	@Override
	public void beforePhase(PhaseEvent event) {
		//System.out.println("END PHASE " + event.getPhaseId());
	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.ANY_PHASE;
	}

	private static final long serialVersionUID = -7795839603158312478L;
}
