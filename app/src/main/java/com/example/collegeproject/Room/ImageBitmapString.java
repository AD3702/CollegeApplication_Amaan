package com.example.collegeproject.Room;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.room.TypeConverter;

import java.io.ByteArrayOutputStream;

public class ImageBitmapString {
    /*@TypeConverter
    public static String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        String temp = android.util.Base64.encodeToString(bytes, Base64.DEFAULT);
        if (temp == null) {
            return null;
        }
        return temp;
    }*/

    @TypeConverter
    public static byte[] getStringFromBitmap(Bitmap bitmapPicture) {
        ByteArrayOutputStream byteArrayBitMapStream = new ByteArrayOutputStream();
        byte[] b = byteArrayBitMapStream.toByteArray();
        return b;
    }

    @TypeConverter
    public static Bitmap getBitmapFromStr(byte[] arr) {
        return BitmapFactory.decodeByteArray(arr, 0, arr.length);
    }
}
