package com.casino.josh.casino_java.Helpers;

/**
 * Created by josh on 11/30/18.
 */

public class BooleanVariable {

    private boolean bool = false;
    private ChangeListener listener;

    /**
     * Get the value contained within the wrapper class.
     * @return boolean
     */
    public boolean getValue() {
        return bool;
    }

    /**
     *
     * @param boo
     */
    public void setBool(boolean boo) {
        this.bool = boo;
        if (listener != null)
            listener.onChange();

        // revert the flag after the event has been triggered.
        this.bool = !bool;
    }

    /**
     * Returns a ChangeListner object.
     * @return ChangeListener
     */
    public ChangeListener getListener() {
        return listener;
    }

    /**
     * Set the value of the ChangeListener object within the class.
     * @param listener
     */
    public void setListener(ChangeListener listener) {
        this.listener = listener;
    }

    /**
     * Interface for the custom listener we will be
     */
    public interface ChangeListener {
        void onChange();
    }
}