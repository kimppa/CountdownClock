package com.vaadin.contrib.countdownclock;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.vaadin.contrib.countdownclock.client.ui.VCountdownClock;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.ClientWidget;

@ClientWidget(VCountdownClock.class)
public class CountdownClock extends AbstractComponent {

    private static final long serialVersionUID = -4093579148150450057L;

    protected Date date;

    protected String format = "%dD %hH %mM %sS";

    protected List<EndEventListener> listeners = new ArrayList<EndEventListener>();

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void changeVariables(Object source, Map variables) {
        super.changeVariables(source, variables);
        if (variables.containsKey("end") && !isReadOnly()) {
            for (EndEventListener listener : listeners) {
                listener.countDownEnded(this);
            }
        }
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);
        target.startTag("countdown");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        long difference = calendar.getTimeInMillis()
                - Calendar.getInstance().getTimeInMillis();

        target.addAttribute("time", difference);
        target.addAttribute("format", format);
        target.endTag("countdown");
    }

    /**
     * Set the format for the clock. Available parameters:
     * 
     * %d - days %h - hours %m - minutes %s - seconds %ts - tenth of a seconds
     * 
     * For example "%d day(s) %h hour(s) and %m minutes" could produce the
     * string "2 day(s) 23 hour(s) and 5 minutes"
     * 
     * @param format
     */
    public void setFormat(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }

    public interface EndEventListener {
        public void countDownEnded(CountdownClock clock);
    }

    public void addListener(EndEventListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    public void removeListener(EndEventListener listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }

}
