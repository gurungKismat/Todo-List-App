package com.example.todolist.intentservice;

import android.app.IntentService;
import android.content.Intent;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.example.todolist.AddTodo;
import com.example.todolist.Json2Pojo;
import com.example.todolist.OkHttpHelper;
import com.google.gson.Gson;

public class MyIntentService extends IntentService {

    public static final String DATA = "data";
    public static final String DATA_KEY = "data_key";
    private String data;
//    private static final String TAG = "MyTag" ;

    public MyIntentService() {
        super("MyIntentService");
    }

    // this method will be called after intent service is started
    // handles the API call and send data to the addtodo activity

    @Override
    protected void onHandleIntent(Intent intent) {
//        Log.d(TAG,"onHandleIntent");
        String word = intent.getStringExtra(AddTodo.WORD_KEY);

        try {
            data = OkHttpHelper.getData(word);
//            Log.d(TAG,"data value: "+data.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (data != null) {
            Gson gson = new Gson();
            Json2Pojo result = gson.fromJson(data, Json2Pojo.class); // parsing json format to specific pojo class format

            // result can be sent through intent because the pojo class has implemented Parcelable interface
            Intent intent1 = new Intent(DATA);
            intent1.putExtra(DATA_KEY,result);
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent1);
        }
    }

}