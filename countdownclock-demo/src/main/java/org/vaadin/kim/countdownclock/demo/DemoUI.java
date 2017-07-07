package org.vaadin.kim.countdownclock.demo;

import java.util.Calendar;

import javax.servlet.annotation.WebServlet;

import org.vaadin.kim.countdownclock.CountdownClock;
import org.vaadin.kim.countdownclock.CountdownClock.EndEventListener;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class DemoUI extends UI {

	private static final long serialVersionUID = -2474563921376269949L;

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = DemoUI.class)
    public static class Servlet extends VaadinServlet {}
	
	@Override
	protected void init(VaadinRequest request) {
		final VerticalLayout layout = new VerticalLayout();
		setContent(layout);

		Label label = new Label(
				"Just specify the format of the count down and "
						+ "the date to which to count and you're set to go! "
						+ "Here is an example:");
		layout.addComponent(label);

		CountdownClock clock1 = new CountdownClock();
		Calendar c = Calendar.getInstance();
		c.set(c.get(Calendar.YEAR) + 1, 0, 1, 0, 0, 0);
		clock1.setDate(c.getTime());
		clock1.setFormat("<span style='font: bold 13px Arial; margin: 10px'>"
				+ "Time until new year: %d days, %h hours, %m minutes and %s seconds</span>");
		clock1.setHeight("40px");

		layout.addComponent(clock1);
		final CountdownClock clock = new CountdownClock();

		Button button = new Button("Don't click on me", new ClickListener() {
			private static final long serialVersionUID = -3301865196296699922L;

			public void buttonClick(ClickEvent event) {
				event.getButton().setEnabled(false);
				Calendar c = Calendar.getInstance();
				c.add(Calendar.SECOND, 10);
				clock.setDate(c.getTime());
				clock.setFormat("<span style='font: bold 25px Arial; margin: 10px'>"
						+ "This page will self-destruct in %s.%ts seconds.</span>");

				clock.addEndEventListener(new EndEventListener() {
					public void countDownEnded(CountdownClock clock) {
						Notification
								.show("Ok, implementing the page destruction was"
										+ "kinda hard, so could you please just imagine"
										+ " it happening?", Notification.Type.ERROR_MESSAGE);
					}
				});
				layout.addComponent(clock);
			}
		});

		layout.addComponent(button);
	}

}
