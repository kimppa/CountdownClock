package org.vaadin.kim.countdownclock.client.ui;

import com.vaadin.shared.communication.ServerRpc;

public interface CountdownClockRpc extends ServerRpc {
	
	public void countdownEnded();

}
