package com.example.todolist;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

    public class OkHttpHelper {

//        private static final String TAG = "MyTag" ;

        // connects to the API and get the data from the API in json format
        public static String getData(String word) throws Exception{
            String url = "https://spell-check2.p.rapidapi.com/spellcheck?word="+word;

//            Log.d(TAG,"getData");

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("x-rapidapi-host", "spell-check2.p.rapidapi.com")
                    .addHeader("x-rapidapi-key", "e0ef2dfe88msh07f9e4740cace58p1aae74jsnc7f3fe35b211")
                    .build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
//                Log.d(TAG,"onResponse successful");
                return response.body().string();
            }else {
//                Log.d(TAG,"onResponse unsuccessful");
                throw new Exception("Error code: "+response.code());
            }
        }
}
