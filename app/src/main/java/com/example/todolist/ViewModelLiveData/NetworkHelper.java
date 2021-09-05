package com.example.todolist.ViewModelLiveData;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkHelper {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager =(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        try {
            if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
                return true;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
