package com.vaadin.contrib.countdownclock;

import java.util.Calendar;

import com.vaadin.Application;
import com.vaadin.contrib.countdownclock.CountdownClock.EndEventListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.Notification;

public class CountdownclockApplication extends Application {

    private static final long serialVersionUID = -2474563921376269949L;

    @Override
    public void init() {
        final Window mainWindow = new Window("Countdownclock Application");
        setMainWindow(mainWindow);
        final VerticalLayout layout = new VerticalLayout();
        mainWindow.addComponent(layout);

        Label label = new Label(
                "Just specify the format of the count down and "
                        + "the date to which to count and you're set to go! "
                        + "Here is an example:");
        layout.addComponent(label);

        CountdownClock clock1 = new CountdownClock();
        Calendar c = Calendar.getInstance();
        c.set(2012, 12, 21, 0, 0, 0);
        clock1.setDate(c.getTime());
        clock1
                .setFormat("<span style='font: bold 13px Arial; margin: 10px'>"
                        + "The end of the world will come on December 21, 2012<br />"
                        + "Which is %d days, %h hours, %m minutes and %s seconds from now.</span>");
        clock1.setHeight("40px");

        layout.addComponent(clock1);

        Button button = new Button("Don't click on me", new ClickListener() {
            private static final long serialVersionUID = -3301865196296699922L;

            public void buttonClick(ClickEvent event) {
                event.getButton().setEnabled(false);
                CountdownClock clock = new CountdownClock();
                Calendar c = Calendar.getInstance();
                c.add(Calendar.SECOND, 10);
                clock.setDate(c.getTime());
                clock
                        .setFormat("<span style='font: bold 25px Arial; margin: 10px'>"
                                + "This page will self-destruct in %s.%ts seconds.</span>");
                // clock
                // .setFormat("<span style='font: bold 20px Arial; margin: 10px'>"
                // + "This page will self-destruct in %s second(s)</span>");
                clock.addListener(new EndEventListener() {
                    public void countDownEnded(CountdownClock clock) {
                        mainWindow
                                .showNotification(
                                        "Ok, implementing the page destruction was <br />"
                                                + "kinda hard, so could you please just imagine <br />"
                                                + "it happening?",
                                        Notification.TYPE_ERROR_MESSAGE);
                    }
                });
                layout.addComponent(clock);
            }
        });

        layout.addComponent(button);
    }

}
