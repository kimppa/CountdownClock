package org.vaadin.kim.countdownclock.client.ui;

import com.vaadin.terminal.gwt.client.ComponentState;

public class CountdownClockState extends ComponentState {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2111850091485279585L;

	/**
	 * Defines the format in which the countdown clock should show the remaining
	 * time
	 */
	private String timeFormat;

	/**
	 * Number of milliseconds to count
	 */
	private long countdownTarget;

	public String getTimeFormat() {
		return timeFormat;
	}

	public void setTimeFormat(String timeFormat) {
		this.timeFormat = timeFormat;
	}

	public long getCountdownTarget() {
		return countdownTarget;
	}

	public void setCountdownTarget(long countdownTarget) {
		this.countdownTarget = countdownTarget;
	}

}
