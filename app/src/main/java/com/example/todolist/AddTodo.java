package com.example.todolist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todolist.ViewModelLiveData.AddTodoViewModel;
import com.example.todolist.ViewModelLiveData.NetworkHelper;
import com.example.todolist.database.DataConverter;
import com.example.todolist.database.TodoTaskEntity;
import com.example.todolist.intentservice.MyIntentService;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class AddTodo extends AppCompatActivity {

    private final static String EDIT_TEXT_DATA = "edit_text_data_key";
    private static final String TAG = "MyTag";
    public static final String WORD_KEY = "word_key";
    private EditText editTextTask;
    private Button checkSpellingBtn;
    private FloatingActionButton fabAddTask;
    private AddTodoViewModel addTodoViewModel;
    private Bitmap bitmap;
    private TextView suggestedWords;
    private TextView suggestTitle;
    private ProgressDialog progressDialog;
    private int taskId;
    private boolean isEditTExtEmpty;
    private boolean isImageUploaded = false;
    private boolean showDeleteMenuItem = false;
    private int items;
    private int countItems;
    private int countIsCorrect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtodo);

        editTextTask = findViewById(R.id.editTextAddTask);
        checkSpellingBtn = findViewById(R.id.check_spell_btn);

        suggestedWords = findViewById(R.id.suggestedTextView);
        suggestTitle = findViewById(R.id.suggestTitle);

        checkSpellingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkSpelling();
            }
        });


        initFabAddTask();

        //sending bundle object as a parameter which can contain edittext data
        initAddTodoViewModel(savedInstanceState);
    }


    // initializing view model for adding task
    private void initAddTodoViewModel(Bundle savedInstanceState) {
        addTodoViewModel = new ViewModelProvider(this).get(AddTodoViewModel.class);
        addTodoViewModel.taskLiveData.observe(this, new Observer<TodoTaskEntity>() {
            @Override
            public void onChanged(TodoTaskEntity todoTaskEntity) {
                Log.d("task", "setting edit text");
                taskId = todoTaskEntity.getTaskId();

                // check if the bundle has data or not
                if (savedInstanceState == null) {
                    editTextTask.setText(todoTaskEntity.getToDoTask());
                } else {
                    String data = savedInstanceState.getString(EDIT_TEXT_DATA);
                    editTextTask.setText(data);
                }

            }
        });

        // to check if addtodo activity is launched from floating action button or from recycler view click
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(RecyclerAdapter.ROW_ID)) {
            taskId = intent.getIntExtra(RecyclerAdapter.ROW_ID, -1);
            showDeleteMenuItem = true;
            if (taskId != -1) {
                Log.d("task", "getting task by id called " + String.valueOf(taskId));
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
                if (!isEditTExtEmpty && isImageUploaded) {
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
            todoTaskEntity.setTaskId(taskId);
            if (bitmap != null) {
                todoTaskEntity.setImgInBytes(DataConverter.convertToByteArray(bitmap));
                // calling adding view model class to add new task in database
                addTodoViewModel.addTask(todoTaskEntity);
                isEditTExtEmpty = false;
            } else {
                Toast.makeText(this, "Please upload image", Toast.LENGTH_SHORT).show();
            }

        } else {
            isEditTExtEmpty = true;
            Toast.makeText(this, "Notes can not be empty!!", Toast.LENGTH_SHORT).show();
        }
    }

    // inflating menu in add to do activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.update_task_menu, menu);
//        MenuItem uploadImgItem = menu.findItem(R.id.upload_img);
        MenuItem deleteNoteItem = menu.findItem(R.id.delete_single_task); // getting the reference of menu item from menu
        // only show delete icon if the note is already saved in database
        if (!showDeleteMenuItem) {
            deleteNoteItem.setVisible(false);
            return true;
        }
        return super.onCreateOptionsMenu(menu);

    }

    // if delete icon in add to do activity is clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete_single_task) {
            addTodoViewModel.deleteTask();
            finish();
            return true;
        }

        if (item.getItemId() == R.id.upload_thumbnail) {
//            Toast.makeText(this,"Image Uploaded",Toast.LENGTH_SHORT).show();
            chooseImg();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    // method that allows to choose image from gallery
    private void chooseImg() {
        ImagePicker.with(this)
                .galleryOnly()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start();
    }

    // using library to fetch images from gallery or directory
    // this method will be called after image is selected from gallery
    // receive result in the the intent after selecting the image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                Toast.makeText(this, "Image Selected", Toast.LENGTH_SHORT).show();
                isImageUploaded = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    // to preserve data when configuration change takes place
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        String noteToBeSave = editTextTask.getText().toString();
        outState.putString(EDIT_TEXT_DATA, noteToBeSave);
    }

    // if spell checking button is clicked this will be invoked
    // start intent service that checks the typo error with the help of API
    private void checkSpelling() {

        if (NetworkHelper.isNetworkAvailable(this)) {
            suggestedWords.setText("");
            suggestTitle.setText("");
            String task = editTextTask.getText().toString();

            String[] words = task.split(" ");
             items = words.length;
//             Log.d("tag","items length "+items);
             countItems = 0;

            progressDialog = ProgressDialog.show(this,"Checking Spelling","Loading...",true);

            for (String singleValue : words) {
                Intent intent = new Intent(this, MyIntentService.class);
                intent.putExtra(WORD_KEY, singleValue);
                startService(intent);
                countItems++;
//                Log.d("tag","countItems: "+countItems);
            }
        }else {
            Toast.makeText(this,"Internet is not available",Toast.LENGTH_SHORT).show();
        }


    }

    // receive data from the my intent service class
    public BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            Log.d(TAG, "onReceiver broadcast");
            Json2Pojo result = intent.getParcelableExtra(MyIntentService.DATA_KEY);
            boolean isCorrect = result.getCorrect();

//            Log.d("tag","countIsCorrect: "+countIsCorrect);
            if (!isCorrect) {
                suggestTitle.setText(getString(R.string.suggested_word));
                suggestTitle.setVisibility(View.VISIBLE);
                countIsCorrect = 0;
                List<String> suggestions = result.getSuggestions();
                for (String suggest : suggestions) {
                    suggestedWords.append(suggest + "   ");
                }
            }else {
                countIsCorrect++;
            }

            if (countItems == items) {
                progressDialog.dismiss();
            }

            if (countIsCorrect == items) {
               showToast();
               countIsCorrect = 0;
            }

        }
    };

    //  registering local broadcast manager to receive data from service
    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mReceiver, new IntentFilter(MyIntentService.DATA));
    }

    // unregistering local broad cast manager if the activiy becomes invisible
    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mReceiver);
    }

    public void showToast() {
        Toast.makeText(this,"Spelling is correct",Toast.LENGTH_SHORT).show();
    }
}