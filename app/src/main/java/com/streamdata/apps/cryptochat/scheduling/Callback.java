package com.streamdata.apps.cryptochat.scheduling;

/**
 * Callback interface:
 * success and error possible events
 */
public interface Callback<T> {
    void onSuccess(T result);
    void onError(Exception ex);
    // TODO: add onCancel method for Callbacks
}
