package com.streamdata.apps.cryptochat.database;

import android.os.Handler;

import com.streamdata.apps.cryptochat.models.Message;
import com.streamdata.apps.cryptochat.scheduling.Callback;
import com.streamdata.apps.cryptochat.scheduling.TaskRunner;

import java.util.List;
import java.util.concurrent.Executors;

/**
 * Controller class for threaded access to database
 */
public class DBController {
    public static final String DB_CONTROLLER_LOG_TAG = "DBController";

    private TaskRunner<List<Message>> runner =
            new TaskRunner<>(Executors.newSingleThreadExecutor());

    private final Handler uiHandler;

    public DBController(Handler uiHandler) {
        this.uiHandler = uiHandler;
    }

    public void saveMessages(List<Message> messages, Callback<List<Message>> saveMessagesCallback) {

        runner.runTask(
                new SaveMessagesTask(messages),
                saveMessagesCallback,
                uiHandler
        );
    }

    public void loadMessages(int targetId, Callback<List<Message>> loadMessagesCallback) {

        runner.runTask(
                new GetTalkMessagesTask(targetId),
                loadMessagesCallback,
                uiHandler
        );
    }
}
