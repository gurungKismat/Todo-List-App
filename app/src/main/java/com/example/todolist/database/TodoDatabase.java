package com.example.todolist.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {TodoTaskEntity.class},version = 1,exportSchema = false)
public abstract class TodoDatabase extends RoomDatabase {

    // returns Data Access Object to access and modify data in room database
    public abstract TodoDao todoDao();

    public static TodoDatabase dbInstance;
    private static final String DATABASE_NAME = "todo_database"; // Initializing Database name

    // using singleton pattern to initialize database instance
    public static TodoDatabase getInstance(Context context) {
        if (dbInstance == null) {
            // this keyword helps to makes thread safe
            synchronized (TodoDatabase.class) {
                // double checking if database instance is already initialized or not to make it thread safe
                if (dbInstance == null) {
                    // using database builder to build database instance
                    dbInstance = Room.databaseBuilder(context,TodoDatabase.class,DATABASE_NAME).build();
                }
            }
        }
        return dbInstance;
    }

}
