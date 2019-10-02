package com.todolist.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.cursoradapter.widget.SimpleCursorAdapter;

import com.todolist.models.Task;

import java.util.LinkedList;
import java.util.List;

public class TaskTableHelper extends SQLiteOpenHelper implements CrudDao<Task>{
    //иноформация по БД
    private static final String DATABASE_NAME = "todolist.db"; // название бд
    private static final int DB_VERSION = 1; // версия базы данных
    // названия столбцов
    public final String TABLE_NAME = "task";
    public final String COLUMN_ID = "_id";
    public final String COLUMN_DEFINITION = "definition";
    //запросы сохдания и удаления таблицы
    private final String SQL_CREATE_TASK_TABLE="create table if not exists " + TABLE_NAME + "(" +
                    COLUMN_ID + " INTEGER primary key autoincrement, " +
                    COLUMN_DEFINITION + " TEXT)";
    private final String SQL_DROP_TABLE="drop table " + DATABASE_NAME;

    public TaskTableHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TASK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
        db.execSQL(SQL_DROP_TABLE);
        onCreate(db);
    }

    @Override
    public Task find(Integer id) {
        Task task = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorTask =  db.query(TABLE_NAME,new String[]{COLUMN_ID,COLUMN_DEFINITION},
                COLUMN_DEFINITION + " = " + id,null,null,null,null);
        if (cursorTask.moveToNext())
        {
            Integer _id = cursorTask.getInt(1);
            String _string = cursorTask.getString(2);
            task = new Task(_id,_string);
        }
        cursorTask.close();
        db.close();
        return task;
    }

    @Override
    public void save(Task model) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_DEFINITION, model.getDefinition());
        db.insert(TABLE_NAME,null, contentValues);
        db.close();
    }

    @Override
    public void update(Task model) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_DEFINITION, model.getDefinition());
        db.update(TABLE_NAME, contentValues, COLUMN_ID + " = ?", new String[] {model.getId().toString()});
        db.close();
    }

    @Override
    public void delete(Task model) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[] {model.getId().toString()});
        db.close();
    }

    @Override
    public List<Task> findAll() {
        List<Task> tasks = new LinkedList<Task>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorTasks = db.query(TABLE_NAME,new String[]{COLUMN_ID,COLUMN_DEFINITION},
                null,null,null,null,null);
        while (cursorTasks.moveToNext())
        {
            Integer id = cursorTasks.getInt(0);
            String definition = cursorTasks.getString(1);
            Task task = new Task(id,definition);
            tasks.add(task);
        }
        cursorTasks.close();
        db.close();
        return tasks;
    }
}