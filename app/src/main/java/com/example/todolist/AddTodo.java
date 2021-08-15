package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.todolist.ViewModelLiveData.AddTodoViewModel;
import com.example.todolist.database.TodoDatabase;
import com.example.todolist.database.TodoTaskEntity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddTodo extends AppCompatActivity {
    private EditText editTextTask;
    private FloatingActionButton fabAddTask;
    private AddTodoViewModel addTodoViewModel;
    private boolean showMenu = false;
    private boolean isEditTExtEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo);

        editTextTask = findViewById(R.id.editTextAddTask);
        initFabAddTask();
        initAddTodoViewModel();
    }

    // initializing view model for adding task
    private void initAddTodoViewModel() {
        addTodoViewModel = new ViewModelProvider(this).get(AddTodoViewModel.class);
        addTodoViewModel.taskLiveData.observe(this, new Observer<TodoTaskEntity>() {
            @Override
            public void onChanged(TodoTaskEntity todoTaskEntity) {
                Log.d("task","setting edit text");
                editTextTask.setText(todoTaskEntity.getToDoTask());
            }
        });

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(RecyclerAdapter.ROW_ID)) {
            int taskId = intent.getIntExtra(RecyclerAdapter.ROW_ID,-1);
            showMenu = true;
            if (taskId != -1) {
                Log.d("task","getting task by id called "+String.valueOf(taskId));
                addTodoViewModel.getTaskById(taskId);
            }
        }
    }


    // initializing the floating action button
    private void initFabAddTask() {
        fabAddTask = findViewById(R.id.floatingActionBtnAddTask);
        fabAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTaskToDb();
                if (!isEditTExtEmpty) {
                    finish();  // destroying the activity once the new task is added
                }
            }
        });
    }

    // inserting into database
    private void addTaskToDb() {
        String newTask = editTextTask.getText().toString().trim();
        if (!newTask.isEmpty()) {
            TodoTaskEntity todoTaskEntity = new TodoTaskEntity(newTask);
            // calling adding view model class to add new task in database
            addTodoViewModel.addTask(todoTaskEntity);
            isEditTExtEmpty = false;
        }else {
            isEditTExtEmpty = true;
            Toast.makeText(this, "Type something!!", Toast.LENGTH_SHORT).show();
        }
    }

    // inflating menu in add to do activiy
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (showMenu) {
            getMenuInflater().inflate(R.menu.update_task_menu, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    // if delete icon in ad to do activity is clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete_single_task) {
            addTodoViewModel.deleteTask();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}