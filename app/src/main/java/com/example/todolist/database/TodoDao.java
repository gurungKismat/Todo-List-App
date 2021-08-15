package com.example.todolist.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

// Data access object interface to access and modify data

@Dao
public interface TodoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE) // Replace the data with current data if primary key is similar
    void insetTask(TodoTaskEntity todoTaskEntity);

    @Delete
    void deleteTask(TodoTaskEntity todoTaskEntity);

    @Query("SELECT * FROM todo_list")
    LiveData<List<TodoTaskEntity>> getAllTasks();

    @Query("SELECT * FROM  todo_list WHERE taskId = :id")
    TodoTaskEntity getTaskById(int id);

    @Query("DELETE FROM todo_list")
    void deleteAllNotes();
}
