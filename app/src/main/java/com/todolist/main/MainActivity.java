package com.todolist.main;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.TimeAnimator;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.todolist.db.TaskTableHelper;
import com.todolist.models.Task;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TaskTableHelper taskTableHelper;
    private ListView taskList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskTableHelper = new TaskTableHelper(this.getApplicationContext());
        taskList = (ListView)findViewById(R.id.tasksList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        RefreshListView();
    }
    private void RefreshListView()
    {
        List<Task> tasks = taskTableHelper.findAll();
        ListViewTaskAdapter adapter = new ListViewTaskAdapter(this, tasks);
        taskList.setAdapter(adapter);
    }

}
