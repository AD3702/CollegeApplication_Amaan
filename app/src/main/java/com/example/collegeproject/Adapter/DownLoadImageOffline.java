package com.example.collegeproject.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.View;

import com.example.collegeproject.Activity.Login;
import com.example.collegeproject.Activity.MainActivity;
import com.example.collegeproject.Activity.MasterActivity;
import com.example.collegeproject.Room.Offline_User_Data;
import com.example.collegeproject.Room.RoomDB;
import com.tuyenmonkey.mkloader.MKLoader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class DownLoadImageOffline extends AsyncTask<String, Void, Bitmap> {

    Context context;
    RoomDB roomDB;
    Offline_User_Data offline_user_data;
    MKLoader mkLoader;
    SharedPreferences sharedPreferences;
    public static final String mypreference = "mypref";
    public static final String USER_ID = "user_id";
    public static final String LOGGED_IN = "log_in";
    String isloggedin;
    private ProgressDialog progressDialog;

    public DownLoadImageOffline(Context context, RoomDB roomDB, Offline_User_Data offline_user_data, MKLoader mkLoader) {
        this.context = context;
        this.roomDB = roomDB;
        this.offline_user_data = offline_user_data;
        this.mkLoader = mkLoader;
    }

    /*
            doInBackground(Params... params)
                Override this method to perform a computation on a background thread.
         */
    protected Bitmap doInBackground(String... urls) {
        String urlOfImage = urls[0];
        Bitmap logo = null;
        try {
            InputStream is = new URL(urlOfImage).openStream();
                /*
                    decodeStream(InputStream is)
                        Decode an input stream into a bitmap.
                 */
            logo = BitmapFactory.decodeStream(is);
        } catch (Exception e) { // Catch the download exception
            e.printStackTrace();
        }
        return logo;
    }

    /*
        onPostExecute(Result result)
            Runs on the UI thread after doInBackground(Params...).
     */

    @Override
    protected void onPreExecute() {

        progressDialog = ProgressDialog.show(context,
                "Please Wait", "Organizing Data");
        progressDialog.show();

    }

    protected void onPostExecute(Bitmap result) {
        progressDialog.dismiss();
        Uri image_login_uri = getImageUri(result);
        Intent intent = new Intent(context, MasterActivity.class);
        intent.setData(image_login_uri);
        context.startActivity(intent);
        MainActivity.mainActivity.finish();
        Login.login_activity.finish();
        roomDB.userDao().delete(offline_user_data);
        roomDB.userDao().insert_user_data(offline_user_data);
        mkLoader.setVisibility(View.INVISIBLE);
        isloggedin = "logged_in";
        sharedPreferences = context.getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LOGGED_IN, isloggedin);
        editor.commit();
    }


    public Uri getImageUri(Bitmap inImage) {
        File tempDir = Environment.getExternalStorageDirectory();
        tempDir = new File(tempDir.getAbsolutePath() + "/.temp/");
        tempDir.mkdir();
        File tempFile = null;
        try {
            tempFile = File.createTempFile("title", ".jpg", tempDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        byte[] bitmapData = bytes.toByteArray();

        //write the bytes in file
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(tempFile);
            fos.write(bitmapData);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Uri.fromFile(tempFile);
    }
}
