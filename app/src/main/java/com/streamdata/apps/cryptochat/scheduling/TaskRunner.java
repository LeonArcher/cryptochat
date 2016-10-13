package com.streamdata.apps.cryptochat.scheduling;

import android.os.Handler;

import java.util.concurrent.ExecutorService;

/**
 * Simple Task runner class
 */
public class TaskRunner<T> {

    private final ExecutorService executor;

    public TaskRunner(ExecutorService executor) {
        this.executor = executor;
    }

    void runTask(Task<T> task, Callback<T> callback,
                 Handler callbackHandler) {

        // execute task on executor
        executor.execute(new TaskRunnable<>(task, callback, callbackHandler));
    }

    // proxy class for running Task on Executor as Runnable with Callback via Handler
    private static class TaskRunnable<T> implements Runnable {

        private final Task<T> task;
        private final Callback<T> callback;
        private final Handler callbackHandler;

        public TaskRunnable(Task<T> task, Callback<T> callback, Handler callbackHandler) {
            this.task = task;
            this.callback = callback;
            this.callbackHandler = callbackHandler;
        }

        @Override
        public void run() {
            try {
                T result = task.run();
                callbackHandler.post(new CallbackOnSuccessRunnable<>(callback, result));

            } catch (Exception ex) {
                callbackHandler.post(new CallbackOnErrorRunnable<>(callback, ex));
            }
        }
    }

    // proxy class for running Callback's method onSuccess as Runnable
    private static class CallbackOnSuccessRunnable<T> implements Runnable {

        private final Callback<T> callback;
        private final T result;

        public CallbackOnSuccessRunnable(Callback<T> callback, T result) {
            this.callback = callback;
            this.result = result;
        }

        @Override
        public void run() {
            callback.onSuccess(result);
        }
    }

    // proxy class for running Callback's method onError as Runnable
    private static class CallbackOnErrorRunnable<T> implements Runnable {

        private final Callback<T> callback;
        private final Exception exception;

        public CallbackOnErrorRunnable(Callback<T> callback, Exception exception) {
            this.callback = callback;
            this.exception = exception;
        }

        @Override
        public void run() {
            callback.onError(exception);
        }
    }
}
