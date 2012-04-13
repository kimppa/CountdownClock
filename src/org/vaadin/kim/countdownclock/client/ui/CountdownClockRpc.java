package org.vaadin.kim.countdownclock.client.ui;

import com.vaadin.terminal.gwt.client.communication.ServerRpc;

public interface CountdownClockRpc extends ServerRpc {
	
	public void countdownEnded();

}
