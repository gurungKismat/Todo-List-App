package com.example.todolist.database;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import java.io.ByteArrayOutputStream;

public class DataConverter {

    // converts the bitmap to byte array type data

    public static byte[] convertToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
        return outputStream.toByteArray();
    }


    // converts the byte array to bitmap

    public static Bitmap convertToBitmap(byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);  // offset means beginning of the parsing and length is ending of the parsing
    }
}
