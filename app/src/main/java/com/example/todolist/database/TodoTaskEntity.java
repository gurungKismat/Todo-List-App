package com.example.todolist.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "todo_list")
public class TodoTaskEntity {

    @PrimaryKey(autoGenerate = true)
    private int taskId;

    private String toDoTask;


    public TodoTaskEntity(String toDoTask) {
        this.toDoTask = toDoTask;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getToDoTask() {
        return toDoTask;
    }

    public void setToDoTask(String toDoTask) {
        this.toDoTask = toDoTask;
    }
}
