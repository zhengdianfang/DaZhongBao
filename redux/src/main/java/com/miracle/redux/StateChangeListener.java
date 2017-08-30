package com.miracle.redux;


/**
 * Listener which will be notified each time state changes.
 * <p>
 */
public interface StateChangeListener<S> {
    void onStateChanged(S state);
}
