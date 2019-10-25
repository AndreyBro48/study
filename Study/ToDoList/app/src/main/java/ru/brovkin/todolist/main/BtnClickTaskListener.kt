package ru.brovkin.todolist.main

import android.view.View
import ru.brovkin.todolist.models.Task
import ru.brovkin.todolist.repositories.TaskRepository

class BtnClickTaskListener : View.OnClickListener{
    private lateinit var  task : Task
    private lateinit var taskRepository: TaskRepository
    private lateinit var mainActivity: MainActivity

    constructor(task: Task, taskRepository: TaskRepository, mainActivity: MainActivity) {
        this.task = task
        this.taskRepository = taskRepository
        this.mainActivity = mainActivity
    }

    override fun onClick(view: View) {
        taskRepository.delete(task)
        mainActivity.RefreshListView()
    }
}