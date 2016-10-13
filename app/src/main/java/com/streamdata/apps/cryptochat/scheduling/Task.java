package com.streamdata.apps.cryptochat.scheduling;

/**
 * Task interface:
 * after run completes one of getResult, getError functions should return not null object
 * before run only nulls are returned
 */
public interface Task<T> {
    T run() throws Exception;
}
