package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.todolist.ViewModelLiveData.MainViewmodel;
import com.example.todolist.database.TodoDatabase;
import com.example.todolist.database.TodoTaskEntity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private FloatingActionButton fabBtn;
    private List<TodoTaskEntity> taskEntities = new ArrayList<>();
    private MainViewmodel mainViewmodel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("task", "onCreate called");


        initViewModel();
        initRecyclerView();
        initFabMain();

    }

    // initializing view model of main activity
    private void initViewModel() {
        mainViewmodel = new ViewModelProvider(this).get(MainViewmodel.class);
        mainViewmodel.todoTaskEntityList.observe(this, new Observer<List<TodoTaskEntity>>() {
            @Override
            public void onChanged(List<TodoTaskEntity> todoTaskEntities) {

                taskEntities = todoTaskEntities;

                // check if data is displaying in recycler view for the first time or not
                if (adapter == null) {
                    Log.d("task", "Initializing data in adapter for first time...");
                    adapter = new RecyclerAdapter(MainActivity.this, taskEntities);
                    recyclerView.setAdapter(adapter);
                } else {
                    Log.d("task", "Notifying about data changed to adapter...");
                    adapter.setList(taskEntities);

                }

            }
        });
    }

    // initializing recycler view
    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.hasFixedSize();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);


        // notes will be deleted if the user swipe it to left or right direction
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {
                AppExecutor.getInstance().getSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        int pos = viewHolder.getAdapterPosition();
                        TodoTaskEntity task = taskEntities.get(pos);
                        TodoDatabase.getInstance(MainActivity.this).todoDao().deleteTask(task);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });
                    }
                });
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    // initializing floating action button
    private void initFabMain() {
        fabBtn = findViewById(R.id.floatingActionButtonMain);
        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddTodo.class);
                startActivity(intent);
            }
        });
    }


    // Inflating menu in main activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    // if menu item is clicked this method will be invoked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete_menu_icon) {
            mainViewmodel.deleteAllNotes();
            Toast.makeText(this, "All notes deleted", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item); // returning default value if item selected is not handled
    }
}