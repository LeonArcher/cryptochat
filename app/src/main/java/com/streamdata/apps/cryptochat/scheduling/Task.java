package com.streamdata.apps.cryptochat.scheduling;

import android.support.annotation.Nullable;

/**
 * Task interface:
 * after run completes one of getResult, getError functions should return not null object
 * before run only nulls are returned
 */
public interface Task<Result, Error> {
    void run();
    @Nullable Result getResult();
    @Nullable Error getError();
}
