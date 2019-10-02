package com.todolist.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.todolist.db.TaskTableHelper;
import com.todolist.models.Task;

import java.util.List;

public class ListViewTaskAdapter extends ArrayAdapter<Task> {
    TaskTableHelper taskTableHelper;
    public ListViewTaskAdapter(TaskTableHelper taskTableHelper, @NonNull Context context, @NonNull List<Task> tasks) {
        super(context, R.layout.row_list_view, tasks);
        this.taskTableHelper = taskTableHelper;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(R.layout.row_list_view, parent, false);
        TextView title = row.findViewById(R.id.textView);
        Task task = getItem(position);
        ImageButton btn = (ImageButton)row.findViewById(R.id.btnItemTaskListVIew);

        btn.setOnClickListener(new BtnClickTaskListener(task,taskTableHelper,(MainActivity)this.getContext()));
        title.setText(task.getDefinition());
        return row;
    }
}
