package ru.brovkin.todolist.main

import android.app.Notification
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.textfield.TextInputEditText

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import ru.brovkin.todolist.R
import ru.brovkin.todolist.models.Task
import ru.brovkin.todolist.repositories.TaskRepository
import ru.brovkin.todolist.repositories.TaskRepositoryFileImpl
import ru.brovkin.todolist.repositories.TaskRepositorySqliteImpl



class MainActivity : AppCompatActivity() {
    private lateinit var taskRepository : TaskRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        taskRepository = TaskRepositorySqliteImpl(this.applicationContext)
        this.RefreshListView()
    }

     fun btnAddTask_Click(view: View) {
        val ad = AlertDialog.Builder(this)
        val editText = TextInputEditText(this)
        ad.setTitle("Новая задача")
        ad.setView(editText)
        ad.setPositiveButton("Сохранить") { dialogInterface, i ->
            val definition = editText.text!!.toString()
            if (definition.isEmpty()) {
                Toast.makeText(this, "Нельзя создать пустую задачу",
                    Toast.LENGTH_LONG).show()
            } else {
                taskRepository.save(Task(definition))
                RefreshListView()
            }
        }
        val alertDialog = ad.create()
        alertDialog.show()
     }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        var id = item.itemId
        item.isChecked=true
        if (id == R.id.action_set_from_sql){
            taskRepository = TaskRepositorySqliteImpl(applicationContext)
        } else{
            taskRepository = TaskRepositoryFileImpl(applicationContext)
        }
        RefreshListView()

        return true
    }

    fun RefreshListView() {
        val tasks = taskRepository.findAll()
        val adapter = ListViewTaskAdapter(taskRepository, this, tasks)
        tasksList.adapter = adapter
    }


}
