package ru.brovkin.todolist.repositories

import android.content.Context
import android.content.Context.MODE_WORLD_READABLE
import android.os.Bundle;
import android.util.Log
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import ru.brovkin.todolist.models.Task
import java.io.*
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import android.system.Os.mkdir
import java.nio.file.Files.exists
import android.os.Environment.getExternalStorageDirectory
import com.google.gson.reflect.TypeToken






class TaskRepositoryFileImpl : TaskRepository {

    val gson = Gson()
    val FILE_NAME = "tasks.txt"
    var tasks : LinkedList<Task> = LinkedList()
    private lateinit var context : Context


    constructor(context : Context){
        this.context = context
    }


    override fun find(id: Int?): Task? {
        return tasks.find { task -> task.id == id }
    }

    override fun save(model: Task) {
        if (model.id == 0){
            if (tasks.count()==0){
                model.id = 1
                tasks.add(model)
            } else {
                var max = tasks.maxBy { task -> task.id }!!.id
                model.id = max + 1
            }
        }
        if (!tasks.contains(model)){
            tasks.add(model)
        }
        saveAll()
    }

    override fun update(model: Task) {
        save(model)
    }

    override fun delete(model: Task) {
        tasks.remove(model)
        saveAll()
    }

    override fun findAll(): List<Task> {
        try {
            var br = BufferedReader(
                InputStreamReader(
                    context.openFileInput(FILE_NAME)
                )
            )
            var str = br.readText()
            val listType = object : TypeToken<LinkedList<Task>>() { }.type
            tasks = gson.fromJson(str, listType)
            br.close()
        }
        catch (e : Exception) {
            tasks = LinkedList()
        }
        return tasks
    }

    fun saveAll(){
        try {
            var bw = OutputStreamWriter(context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE))
            bw.write(gson.toJson(tasks))
            bw.close()
        }
        catch (e : Exception) {
            tasks = LinkedList()
        }
    }
}

