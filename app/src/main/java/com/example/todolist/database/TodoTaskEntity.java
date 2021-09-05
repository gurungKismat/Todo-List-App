package com.example.todolist.database;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "todo_list")
public class TodoTaskEntity {

    @PrimaryKey(autoGenerate = true)
    private int taskId;

    private String toDoTask;

    @ColumnInfo(name = "image", typeAffinity = ColumnInfo.BLOB)
    private byte[] imgInBytes;

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

    public byte[] getImgInBytes() {
        return imgInBytes;
    }

    public void setImgInBytes(byte[] imgInBytes) {
        this.imgInBytes = imgInBytes;
    }
}
