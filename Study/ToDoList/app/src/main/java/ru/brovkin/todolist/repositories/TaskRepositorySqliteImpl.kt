package ru.brovkin.todolist.repositories

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ru.brovkin.todolist.models.Task
import ru.brovkin.todolist.repositories.TaskRepositorySqliteImpl.DataBaseInfo.COLUMN_DEFINITION
import ru.brovkin.todolist.repositories.TaskRepositorySqliteImpl.DataBaseInfo.TABLE_NAME
import java.util.*

class TaskRepositorySqliteImpl(context: Context) :  SQLiteOpenHelper(context,DataBaseInfo.DATABASE_NAME,null,DataBaseInfo.DB_VERSION), TaskRepository {

    object DataBaseInfo{
        val DATABASE_NAME = "todolist.db"
        val DB_VERSION = 1 // версия базы данных
        val TABLE_NAME = "task"
        val COLUMN_ID = "id"
        val COLUMN_DEFINITION = "definition"

        val SQL_CREATE_TASK_TABLE = "create table if not exists " + DataBaseInfo.TABLE_NAME + "(" +
                DataBaseInfo.COLUMN_ID + " INTEGER primary key autoincrement, " +
                DataBaseInfo.COLUMN_DEFINITION + " TEXT)"
        val SQL_DROP_DATABASE = "drop table $DataBaseInfo.DATABASE_NAME"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(DataBaseInfo.SQL_CREATE_TASK_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(DataBaseInfo.SQL_DROP_DATABASE)
        onCreate(db)
    }

    override fun find(id: Int?): Task? {
        var task: Task? = null
        val db = this.readableDatabase
        val cursorTask = db.query(
            DataBaseInfo.TABLE_NAME, arrayOf(DataBaseInfo.COLUMN_ID, DataBaseInfo.COLUMN_DEFINITION),
            "$DataBaseInfo.COLUMN_DEFINITION = $id", null, null, null, null
        )
        if (cursorTask.moveToNext()) {
            val _id = cursorTask.getInt(1)
            val _string = cursorTask.getString(2)

            task = Task(_id, _string)
        }
        cursorTask.close()
        db.close()
        return task
    }

    override fun save(model: Task) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_DEFINITION, model.definition)
        db.insert(DataBaseInfo.TABLE_NAME, null, contentValues)
        db.close()
    }

    override fun update(model: Task) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_DEFINITION, model.definition)
        db.update(TABLE_NAME, contentValues, "${DataBaseInfo.COLUMN_ID} = ?", arrayOf(model.id.toString()))
        db.close()
    }

    override fun delete(model: Task) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "${DataBaseInfo.COLUMN_ID} = ?", arrayOf(model.id.toString()))
        db.close()
    }

    override fun findAll(): List<Task> {
        val tasks = LinkedList<Task>()
        val db = this.readableDatabase
        val cursorTasks = db.query(
            TABLE_NAME,
            arrayOf(DataBaseInfo.COLUMN_ID, COLUMN_DEFINITION),
            null,
            null,
            null,
            null,
            null
        )
        while (cursorTasks.moveToNext()) {
            val id = cursorTasks.getInt(0)
            val definition = cursorTasks.getString(1)
            val task = Task(id, definition)
            tasks.add(task)
        }
        cursorTasks.close()
        db.close()
        return tasks
    }
}