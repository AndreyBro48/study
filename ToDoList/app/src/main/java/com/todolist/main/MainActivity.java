package com.todolist.main;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.TimeAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.todolist.db.TaskTableHelper;
import com.todolist.models.Task;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TaskTableHelper taskTableHelper;
    private ListView taskList;
    private Context mainContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskTableHelper = new TaskTableHelper(this.getApplicationContext());
        taskList = (ListView)findViewById(R.id.tasksList);
        mainContext = MainActivity.this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        RefreshListView();
    }
    public void RefreshListView()
    {
        List<Task> tasks = taskTableHelper.findAll();
        ListViewTaskAdapter adapter = new ListViewTaskAdapter(taskTableHelper,this, tasks);
        taskList.setAdapter(adapter);
    }

    public void btnAddTask_Click(View view) throws InterruptedException {
        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        TextInputEditText editText = new TextInputEditText(this);
        ad.setTitle("Новая задача");
        ad.setView(editText);
        ad.setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String definition = editText.getText().toString();
                if (definition.isEmpty()) {
                    Toast.makeText(mainContext,"Нельзя создать пустую задачу",
                            Toast.LENGTH_LONG).show();
                }
                else
                {
                    taskTableHelper.save(new Task(definition));
                    RefreshListView();
                }
            }
        }).setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog alertDialog = ad.create();
        alertDialog.show();
    }
}
