package com.streamdata.apps.cryptochat.scheduling;

import android.support.annotation.Nullable;

/**
 * Callback interface:
 * result and error should always be checked for null
 */
public interface Callback<Result, Error> {
    void call(@Nullable Result result, @Nullable Error error);
}
