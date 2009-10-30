package com.vaadin.contrib.countdownclock.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VCountdownClock extends Widget implements Paintable {

    /** Set the tagname used to statically resolve widget from UIDL. */
    public static final String TAGNAME = "countdownclock";

    /** Set the CSS class name to allow styling. */
    public static final String CLASSNAME = "v-" + TAGNAME;

    /** Component identifier in UIDL communications. */
    protected String uidlId;

    /** Reference to the server connection object. */
    protected ApplicationConnection client;

    protected int time = 0;

    protected Counter counter = new Counter();

    protected List<TimeString> formatStrings = new ArrayList<TimeString>();

    protected String formatPrefix = "";

    protected List<TimeType> formatsPresent = new ArrayList<TimeType>();

    // seconds, minutes, hours, day
    protected int oneDay = 1000 * 60 * 60 * 24;
    // seconds, minutes, hours
    protected int anHour = 1000 * 60 * 60;
    // seconds, minutes
    protected int aMinute = 1000 * 60;
    // second
    protected int aSecond = 1000;

    protected int timerInterval = 1000;

    /**
     * The constructor should first call super() to initialize the component and
     * then handle any initialization relevant to Vaadin.
     */
    public VCountdownClock() {
        setElement(Document.get().createDivElement());
        // This method call of the Paintable interface sets the component
        // style name in DOM tree
        setStyleName(CLASSNAME);
    }

    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        // This call should be made first. Ensure correct implementation,
        // and let the containing layout manage caption, etc.
        if (client.updateComponent(this, uidl, true)) {
            return;
        }

        // Save reference to server connection object to be able to send
        // user interaction later
        this.client = client;

        // Save the UIDL identifier for the component
        uidlId = uidl.getId();

        UIDL child = uidl.getChildUIDL(0);

        String format = child.getStringAttribute("format");
        time = child.getIntAttribute("time");

        formatsPresent.clear();
        formatStrings.clear();
        formatPrefix = "";

        // Create the format
        while (format.length() > 0) {
            int pos = format.indexOf("%");
            if (pos >= 0) {
                String before = pos > 0 ? format.substring(0, pos) : "";

                format = format.substring(pos);
                String type = format.substring(0, 2);
                int removeChars = 2;
                if (type.equals("%d")) {
                    TimeString ts = new TimeString(TimeType.DAYS);
                    formatStrings.add(ts);
                    formatsPresent.add(TimeType.DAYS);
                } else if (type.equals("%h")) {
                    TimeString ts = new TimeString(TimeType.HOURS);
                    formatStrings.add(ts);
                    formatsPresent.add(TimeType.HOURS);
                } else if (type.equals("%m")) {
                    TimeString ts = new TimeString(TimeType.MINUTES);
                    formatStrings.add(ts);
                    formatsPresent.add(TimeType.MINUTES);
                } else if (type.equals("%s")) {
                    TimeString ts = new TimeString(TimeType.SECONDS);
                    formatStrings.add(ts);
                    formatsPresent.add(TimeType.SECONDS);
                } else if (format.substring(0, 3).equals("%ts")) {
                    TimeString ts = new TimeString(TimeType.TENTH_OF_A_SECONDS);
                    formatStrings.add(ts);
                    formatsPresent.add(TimeType.TENTH_OF_A_SECONDS);
                    removeChars = 3;
                } else {
                    before += type;
                }

                if (formatStrings.size() <= 1) {
                    formatPrefix = before;
                } else {
                    formatStrings.get(formatStrings.size() - 2).setPostfix(
                            before);
                }

                format = format.substring(removeChars);
            } else {
                if (formatStrings.size() < 1) {
                    formatPrefix = format;
                } else {
                    formatStrings.get(formatStrings.size() - 1).setPostfix(
                            format);
                }
                format = "";
            }
        }

        if (formatsPresent.contains(TimeType.TENTH_OF_A_SECONDS)) {
            timerInterval = 100;
        } else if (formatsPresent.contains(TimeType.SECONDS)) {
            timerInterval = aSecond;
        } else if (formatsPresent.contains(TimeType.MINUTES)) {
            timerInterval = aMinute;
        } else if (formatsPresent.contains(TimeType.HOURS)) {
            timerInterval = anHour;
        } else if (formatsPresent.contains(TimeType.DAYS)) {
            timerInterval = oneDay;
        }
        counter.scheduleRepeating(timerInterval);
        counter.run();
    }

    protected void updateLabel() {
        String str = "";
        if (formatPrefix != null) {
            str += formatPrefix;
        }

        for (TimeString ts : formatStrings) {
            str += ts.getValue(time);
        }

        getElement().setInnerHTML(str);
    }

    protected class Counter extends Timer {
        @Override
        public void run() {
            time -= timerInterval;
            if (time <= 0) {
                cancel();
                client.updateVariable(uidlId, "end", true, true);
                time = 0;
            }
            updateLabel();
        }
    }

    protected class TimeString {

        protected String postfix = null;

        protected TimeType type = null;

        public TimeString(TimeType type) {
            this.type = type;
        }

        public String getString(int milliseconds) {
            return null;
        }

        public void setPostfix(String postfix) {
            this.postfix = postfix;
        }

        public String getPostfix() {
            return postfix;
        }

        public String getValue(int milliseconds) {
            if (type.equals(TimeType.DAYS)) {
                return getDays(milliseconds) + postfix;
            } else if (type.equals(TimeType.HOURS)) {
                // Check if a day exists in the format, in that case remove all
                // full days from the time
                if (formatsPresent.contains(TimeType.DAYS)) {
                    milliseconds -= getDays(milliseconds) * oneDay;
                }
                return getHours(milliseconds) + postfix;
            } else if (type.equals(TimeType.MINUTES)) {
                // Check if a day exists in the format, in that case remove all
                // full days from the time
                if (formatsPresent.contains(TimeType.DAYS)) {
                    milliseconds -= getDays(milliseconds) * oneDay;
                }
                if (formatsPresent.contains(TimeType.HOURS)) {
                    milliseconds -= getHours(milliseconds) * anHour;
                }
                return getMinutes(milliseconds) + postfix;
            } else if (type.equals(TimeType.SECONDS)) {
                // Check if a day exists in the format, in that case remove all
                // full days from the time
                if (formatsPresent.contains(TimeType.DAYS)) {
                    milliseconds -= getDays(milliseconds) * oneDay;
                }
                if (formatsPresent.contains(TimeType.HOURS)) {
                    milliseconds -= getHours(milliseconds) * anHour;
                }
                if (formatsPresent.contains(TimeType.MINUTES)) {
                    milliseconds -= getMinutes(milliseconds) * aMinute;
                }
                return getSeconds(milliseconds) + postfix;
            } else if (type.equals(TimeType.TENTH_OF_A_SECONDS)) {
                // Check if a day exists in the format, in that case remove all
                // full days from the time
                if (formatsPresent.contains(TimeType.DAYS)) {
                    milliseconds -= getDays(milliseconds) * oneDay;
                }
                if (formatsPresent.contains(TimeType.HOURS)) {
                    milliseconds -= getHours(milliseconds) * anHour;
                }
                if (formatsPresent.contains(TimeType.MINUTES)) {
                    milliseconds -= getMinutes(milliseconds) * aMinute;
                }
                if (formatsPresent.contains(TimeType.SECONDS)) {
                    milliseconds -= getSeconds(milliseconds) * aSecond;
                }
                return Math.round(milliseconds / 100) + postfix;
            } else {
                return "";
            }

        }

        @Override
        public String toString() {
            return type.name();
        }

        protected int getDays(int milliseconds) {
            return (int) Math.floor(milliseconds / oneDay);
        }

        protected int getHours(int milliseconds) {
            return (int) Math.floor(milliseconds / anHour);
        }

        protected int getMinutes(int milliseconds) {
            return (int) Math.floor(milliseconds / aMinute);
        }

        protected int getSeconds(int milliseconds) {
            return (int) Math.floor(milliseconds / aSecond);
        }
    }

    protected enum TimeType {
        DAYS, HOURS, MINUTES, SECONDS, TENTH_OF_A_SECONDS
    };
}
