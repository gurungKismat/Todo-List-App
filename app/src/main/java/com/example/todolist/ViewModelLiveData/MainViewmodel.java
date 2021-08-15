package com.example.todolist.ViewModelLiveData;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.todolist.database.TodoTaskEntity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MainViewmodel extends AndroidViewModel {

    public LiveData<List<TodoTaskEntity>>  todoTaskEntityList;
    AppRepository appRepositoryInstance;

    public MainViewmodel(@NonNull @NotNull Application application) {
        super(application);

        appRepositoryInstance = AppRepository.getInstance(application.getApplicationContext());
        todoTaskEntityList = appRepositoryInstance.todoTaskList;
    }

    // delete all notes from database
    public void deleteAllNotes() {
        appRepositoryInstance.deleteAllNotes();
    }
}
