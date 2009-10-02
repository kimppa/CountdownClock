package com.vaadin.contrib.countdownclock;

import java.util.Calendar;

import com.vaadin.Application;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;

public class CountdownclockApplication extends Application {

    private static final long serialVersionUID = -2474563921376269949L;

    @Override
    public void init() {
        Window mainWindow = new Window("Countdownclock Application");
        Label label = new Label(
                "Just specify the format of the count down and "
                        + "the date to which to count and you're set to go! "
                        + "Here is an example:");
        mainWindow.addComponent(label);

        CountdownClock clock1 = new CountdownClock();
        Calendar c = Calendar.getInstance();
        c.set(2012, 12, 21);
        clock1.setDate(c.getTime());
        clock1
                .setFormat("<span style='font: bold 13px Arial; margin: 10px'>"
                        + "The end of the world will come on December 21, 2012<br />"
                        + "Which is %d days, %h hours, %m minutes and %s seconds from now.</span>");

        mainWindow.addComponent(clock1);
        setMainWindow(mainWindow);
    }

}
