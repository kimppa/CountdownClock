package org.vaadin.kim.countdownclock.client.ui;

import org.vaadin.kim.countdownclock.CountdownClock;
import org.vaadin.kim.countdownclock.client.ui.VCountdownClock.CountdownEndedListener;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.shared.ui.Connect;

@Connect(CountdownClock.class)
public class CountdownClockConnector extends AbstractComponentConnector
		implements CountdownEndedListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6954184408631921296L;

	private CountdownClockRpc rpc;

	public CountdownClockConnector() {
		rpc = RpcProxy.create(CountdownClockRpc.class, this);
	}

	@Override
	protected Widget createWidget() {
		VCountdownClock w = GWT.create(VCountdownClock.class);
		w.addListener(this);
		return w;
	}

	@Override
	public CountdownClockState getState() {
		return (CountdownClockState) super.getState();
	}

	@Override
	public VCountdownClock getWidget() {
		return (VCountdownClock) super.getWidget();
	}

	@Override
	public void onStateChanged(StateChangeEvent stateChangeEvent) {
		super.onStateChanged(stateChangeEvent);
		getWidget().setTime(getState().getCountdownTarget());
		getWidget().setTimeFormat(getState().getTimeFormat());
		getWidget().startClock();
	}

	public void countdownEnded() {
		rpc.countdownEnded();
	}

}
