package com.example.todolist.ViewModelLiveData;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.example.todolist.AppExecutor;
import com.example.todolist.database.TodoTaskEntity;
import org.jetbrains.annotations.NotNull;



public class AddTodoViewModel extends AndroidViewModel {

    private final AppRepository appRepository;
    // using mutable live data to observe data as well as to change the value of the data
    public MutableLiveData<TodoTaskEntity> taskLiveData = new MutableLiveData<>();


    public AddTodoViewModel(@NonNull @NotNull Application application) {
        super(application);

        // initializing instance of app repository class
        appRepository = AppRepository.getInstance(application.getApplicationContext());
    }

    // get the row or task specified by the id from the table
    public void getTaskById(int id) {
        AppExecutor.executorInstance.getSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                TodoTaskEntity taskById = appRepository.getTaskById(id);
                taskLiveData.postValue(taskById); // post value method to post value in mutable live data from background thread
            }
        });
    }


    // calls the addTask method of repository class and save the task in database
    public void addTask(TodoTaskEntity todoTaskEntity) {
        appRepository.addTask(todoTaskEntity);
    }

    // delete task
    // if taskLiveData is not null than task by id method has been already called which returns task specified by id
    // if user wants to delete this task than they will simply click the delete icon in the app bar
    public void deleteTask() {
        if (taskLiveData != null) {
            TodoTaskEntity task = taskLiveData.getValue();
            appRepository.deleteTask(task);
        }

    }
}
