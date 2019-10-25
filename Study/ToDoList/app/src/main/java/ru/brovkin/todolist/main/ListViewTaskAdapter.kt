package ru.brovkin.todolist.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.TextView
import ru.brovkin.todolist.R
import ru.brovkin.todolist.models.Task
import ru.brovkin.todolist.repositories.TaskRepository

class ListViewTaskAdapter : ArrayAdapter<Task> {
    private lateinit var taskRepository: TaskRepository

    constructor(taskRepository : TaskRepository, context : Context, tasks : List<Task>) : super(context, R.layout.row_list_view, tasks) {
        this.taskRepository = taskRepository
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var layoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var row = layoutInflater.inflate(R.layout.row_list_view, parent, false)
        var title = row.findViewById<TextView>(R.id.textView)
        var task = getItem(position)
        var btn = row.findViewById(R.id.btnItemTaskListVIew) as ImageButton

        btn.setOnClickListener(BtnClickTaskListener(task!!, taskRepository, this.getContext() as MainActivity))
        title.text = task.definition
        return row
    }
}