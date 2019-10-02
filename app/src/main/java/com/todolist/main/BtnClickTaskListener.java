package com.todolist.main;

import android.view.View;

import com.todolist.db.TaskTableHelper;
import com.todolist.models.Task;

public class BtnClickTaskListener implements View.OnClickListener {
    private Task task;
    private TaskTableHelper taskTableHelper;
    private MainActivity mainActivity;


    public BtnClickTaskListener(Task task, TaskTableHelper taskTableHelper, MainActivity mainActivity) {
        this.task = task;
        this.taskTableHelper = taskTableHelper;
        this.mainActivity = mainActivity;
    }

    @Override
    public void onClick(View view) {
        taskTableHelper.delete(task);
        mainActivity.RefreshListView();
    }
}
