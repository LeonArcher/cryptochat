package com.streamdata.apps.cryptochat.scheduling;

import android.os.Handler;

import java.util.concurrent.ExecutorService;

/**
 * Simple Task runner class
 */
public class TaskRunner<Result, Error> {

    private final ExecutorService executor;

    public TaskRunner(ExecutorService executor) {
        this.executor = executor;
    }

    void runTask(Task<Result, Error> task, Callback<Result, Error> callback,
                 Handler callbackHandler) {

        // execute task on executor
        executor.execute(new TaskRunnable<>(task));

        // get result and error (one is always null)
        Result result = task.getResult();
        Error error = task.getError();

        // send to the execution thread via Handler
        callbackHandler.post(new CallbackRunnable<>(callback, result, error));
    }

    // proxy class for running Task as Runnable
    private static class TaskRunnable<Result, Error> implements Runnable {

        private final Task<Result, Error> task;

        public TaskRunnable(Task<Result, Error> task) {
            this.task = task;
        }

        @Override
        public void run() {
            task.run();
        }
    }

    // proxy class for running Callback through Handler as Runnable
    private static class CallbackRunnable<Result, Error> implements Runnable {

        private final Callback<Result, Error> callback;
        private final Result result;
        private final Error error;

        public CallbackRunnable(Callback<Result, Error> callback, Result result, Error error) {
            this.callback = callback;
            this.result = result;
            this.error = error;
        }

        @Override
        public void run() {
            callback.call(result, error);
        }
    }
}
