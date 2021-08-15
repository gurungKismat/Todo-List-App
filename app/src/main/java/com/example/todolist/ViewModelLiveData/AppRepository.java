package com.example.todolist.ViewModelLiveData;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.todolist.AppExecutor;
import com.example.todolist.database.TodoDatabase;
import com.example.todolist.database.TodoTaskEntity;

import java.util.List;
import java.util.concurrent.Executor;

public class AppRepository {

    TodoDatabase databaseInstance;
    public LiveData<List<TodoTaskEntity>> todoTaskList;
    public static AppRepository appRepositoryInstance;
    private Executor executor;


    private AppRepository(Context context) {
        databaseInstance = TodoDatabase.getInstance(context);
        executor = AppExecutor.getInstance().getSingleThreadExecutor();
        todoTaskList = getAllTask();
    }

    private LiveData<List<TodoTaskEntity>> getAllTask() {
        String tname = Thread.currentThread().getName();
        Log.d("thread","theread name "+tname);
        return databaseInstance.todoDao().getAllTasks();
    }

    public static AppRepository getInstance(Context context) {
        appRepositoryInstance = new AppRepository(context);
        return appRepositoryInstance;
    }


    public TodoTaskEntity getTaskById(int id) {
        return databaseInstance.todoDao().getTaskById(id);
    }

    public void addTask(TodoTaskEntity todoTaskEntity) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                databaseInstance.todoDao().insetTask(todoTaskEntity);
            }
        });
    }


    // run on background thread which deletes task from database
    public void deleteTask(TodoTaskEntity task) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                databaseInstance.todoDao().deleteTask(task);
            }
        });

    }

    // delete all notes
    public void deleteAllNotes() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                databaseInstance.todoDao().deleteAllNotes();
            }
        });
    }
}
