package com.example.collegeproject.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.collegeproject.Database.APIInterface;
import com.example.collegeproject.Database.AppClient;
import com.example.collegeproject.Database.Registrationdatum;
import com.example.collegeproject.Database.ServerResponse;
import com.example.collegeproject.Database.UserData;
import com.example.collegeproject.R;
import com.github.barteksc.pdfviewer.PDFView;
import com.tuyenmonkey.mkloader.MKLoader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UploadResume extends AppCompatActivity implements View.OnClickListener {

    Button select_PDF, upload_PDF, next_activity_btn_2;
    ImageView main_pdf_image;
    PDFView pdfView;
    Toolbar toolbar_upload_pdf;
    Uri uri;
    String temp_user_id, resume_check, resume_user;
    ArrayList<Registrationdatum> editresumedatalist;
    String path, verify_id;
    File file;
    private static final int STORAGE_PERMISSION_CODE = 123;
    SharedPreferences sharedPreferences;
    public static final String mypreference = "mypref";
    public static final String USER_ID = "user_id";
    public static final String LOGGED_IN = "log_in";
    MKLoader upload_resume_loader;
    private long pressedTime;
    String isloggedin;
    String permission_denied;
    Uri profile_image_upload_resume;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_resume);
        checkWriteExternalPermission();
        initializeUI();
        toolbar_setup();
        Intent intent_profile_image = getIntent();
        profile_image_upload_resume = intent_profile_image.getData();
        sharedPref();
        Toast.makeText(UploadResume.this, "" + temp_user_id, Toast.LENGTH_SHORT).show();
        if (resume_check.equals("")) {
            main_pdf_image.setVisibility(View.VISIBLE);
        } else {
            loadData_edit();
        }
        select_PDF.setOnClickListener(this);
        upload_PDF.setOnClickListener(this);
        next_activity_btn_2.setOnClickListener(this);
    }

    public void initializeUI() {
        main_pdf_image = findViewById(R.id.main_pdf_image);
        upload_resume_loader = findViewById(R.id.upload_resume_loader);
        upload_resume_loader.setVisibility(View.INVISIBLE);
        pdfView = findViewById(R.id.pdfView);
        select_PDF = findViewById(R.id.select_pdf_btn);
        upload_PDF = findViewById(R.id.upload_pdf_btn);
        pdfView = findViewById(R.id.pdfView);
        toolbar_upload_pdf = findViewById(R.id.toolbar_actionbar_upload_resume);
        next_activity_btn_2 = findViewById(R.id.resume_to_master_login);
    }

    public void toolbar_setup() {
        toolbar_upload_pdf.setBackground(new ColorDrawable(ContextCompat.getColor(this, R.color.main_color)));
        toolbar_upload_pdf.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        toolbar_upload_pdf.setTitle("Upload Resume");
    }

    @Override
    public void onClick(View view) {
        if (view == select_PDF) {
            next_activity_btn_2.setVisibility(View.INVISIBLE);
            Intent intent = new Intent();
            intent.setType("application/pdf");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.setFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            startActivityForResult(Intent.createChooser(intent, "Select Pdf"), 50);
        }
        if (view == upload_PDF) {
            upload_resume_loader.setVisibility(View.VISIBLE);
            uploadFile();
        }
        if (view == next_activity_btn_2) {
            Intent intent_main = new Intent(UploadResume.this, MasterActivity.class);
            intent_main.setData(profile_image_upload_resume);
            startActivity(intent_main);
            sharedPreferences = getSharedPreferences(mypreference, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            isloggedin = "logged_in";
            editor.putString(LOGGED_IN, isloggedin);
            editor.commit();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 50) {
            try {
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    uri = data.getData();
                    path = getPath(this, uri);
                    showPdfFromUri(uri);
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                    assert cursor != null;
                    cursor.moveToFirst();
                    int clumnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    Log.e("PDFPath", String.valueOf(uri));
                    select_PDF.setVisibility(View.INVISIBLE);
                    upload_PDF.setVisibility(View.VISIBLE);
                    main_pdf_image.setVisibility(View.INVISIBLE);
                }
            } catch (Exception e) {
                Toast.makeText(UploadResume.this, "Please select from Internal Memory", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == 2296) {
            if (Environment.isExternalStorageManager()) {
                // perform action when allow permission success
            } else {
                Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showPdfFromUri(Uri uri) {
        pdfView.fromUri(uri)
                .defaultPage(0)
                .spacing(10)
                .load();
    }

    private void showPdfFromURL(InputStream inputStream) {
        pdfView.fromStream(inputStream)
                .defaultPage(0)
                .spacing(10)
                .load();
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void checkWriteExternalPermission() {
        int result = ContextCompat.checkSelfPermission(UploadResume.this, Manifest.permission.MANAGE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_DENIED) {
            requestPermission();
        }
    }

    private void uploadFile() {
        try {
            Log.e("PATH", path);
            file = new File(path);
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            RequestBody id1 = RequestBody.create(MediaType.parse("text/plain"), temp_user_id);

            APIInterface getResponse = AppClient.getclient().create(APIInterface.class);
            Call<ServerResponse> call = getResponse.uploadResumePDF(fileToUpload, id1);
//            Toast.makeText(UploadResume.this, "" + temp_user_id, Toast.LENGTH_SHORT).show();
            call.enqueue(new Callback<ServerResponse>() {
                @Override
                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                    ServerResponse server = response.body();
                    try {
                        if (server.isSuccess()) {
                            upload_PDF.setVisibility(View.INVISIBLE);
                            next_activity_btn_2.setVisibility(View.VISIBLE);
//                            Toast.makeText(UploadResume.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                            upload_resume_loader.setVisibility(View.INVISIBLE);
                        } else {
                            upload_resume_loader.setVisibility(View.INVISIBLE);
                            Log.d("Respon", server.toString());
                        }
                    } catch (Exception e) {
                        upload_resume_loader.setVisibility(View.INVISIBLE);
                        Log.e("Error", String.valueOf(e));
                    }
                }

                @Override
                public void onFailure(Call<ServerResponse> call, Throwable t) {
                    upload_resume_loader.setVisibility(View.INVISIBLE);
                    Log.e("error", String.valueOf(t));
                }
            });
        } catch (Exception e) {
            Log.e("ERROR", String.valueOf(e));
            Toast.makeText(UploadResume.this, "Please Select an PDF", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {
        //check here to KITKAT or new version
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {

            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }

            //DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }

            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(
            Context context, Uri uri, String selection,
            String[] selectionArgs
    ) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null
            );
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }


    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public void sharedPref() {
        sharedPreferences = getSharedPreferences(mypreference, MODE_PRIVATE);
        temp_user_id = sharedPreferences.getString(USER_ID, "");
        resume_check = sharedPreferences.getString("resume_check", "");
        verify_id = sharedPreferences.getString("verify", "");
    }


    @RequiresApi(api = Build.VERSION_CODES.R)
    private void requestPermission() {
        if (Environment.isExternalStorageManager()) {

        } else {
            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        }
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        //Checking the request code of our request
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void loadData_edit() {
        upload_resume_loader.setVisibility(View.VISIBLE);
        APIInterface apiInterface = AppClient.getclient().create(APIInterface.class);
        Call<UserData> registrationCall = apiInterface.find_user_id(temp_user_id);
        registrationCall.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                try {
                    editresumedatalist = (ArrayList<Registrationdatum>) response.body().getRegistrationdata();
                    resume_user = editresumedatalist.get(0).getUserResumePdf();
                    if (resume_user.equals("")) {
                    } else {
                        //String webViewUrl = "http://docs.google.com/gview?embedded=true&url=" + resume_user;
                        try {
                            new RetrivePDFfromUrl().execute(resume_user);
                        } catch (Exception e) {
                            Log.e("LOADEXP", String.valueOf(e));
                        }
                        upload_resume_loader.setVisibility(View.INVISIBLE);
                    }
                } catch (Exception e) {
                    Log.e("LOADERROR", String.valueOf(e));
                    upload_resume_loader.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                upload_resume_loader.setVisibility(View.INVISIBLE);
                Toast.makeText(UploadResume.this, "LOADERROR", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class RetrivePDFfromUrl extends AsyncTask<String, Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {
            // we are using inputstream
            // for getting out PDF.
            InputStream inputStream = null;
            try {
                // below is the step where we are
                // creating our connection.
                HttpURLConnection urlConnection = null;
                URL url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    // response is success.
                    // we are getting input stream from url
                    // and storing it in our variable.
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }

            } catch (IOException e) {
                // this is the method
                // to handle errors.
                e.printStackTrace();
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            // after the execution of our async
            // task we are loading our pdf in our pdf view.
            showPdfFromURL(inputStream);
            main_pdf_image.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        if (resume_user.equals("")) {
            if (pressedTime + 2000 > System.currentTimeMillis()) {
                super.onBackPressed();
                finish();
                sharedPreferences = getSharedPreferences(mypreference, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                isloggedin = "logged_out";
                editor.putString(LOGGED_IN, isloggedin);
                editor.commit();
                startActivity(new Intent(UploadResume.this, Login.class));
            } else {
                Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
            }
            pressedTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }
}