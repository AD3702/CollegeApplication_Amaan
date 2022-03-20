package com.example.collegeproject.Activity;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.collegeproject.Database.APIInterface;
import com.example.collegeproject.Database.AppClient;
import com.example.collegeproject.Database.ServerResponse;
import com.example.collegeproject.R;
import com.example.collegeproject.Room.DataBaseImageProvider;
import com.example.collegeproject.Room.ImageBitmapString;
import com.example.collegeproject.Room.ImagesData;
import com.tuyenmonkey.mkloader.MKLoader;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageAddActivity extends AppCompatActivity {

    Button upload_image, next_activity_btn;
    CircleImageView img_select;
    Toolbar toolbar_upload_image;
    String temp_user_id, verify_id;
    String uridata, value, user_resume_check;
    File file;
    MKLoader mkLoader_image_upload;
    SharedPreferences sharedPreferences;
    public static final String mypreference = "mypref";
    public static final String USER_ID = "user_id";
    public static final String LOGGED_IN = "log_in";
    private static final int STORAGE_PERMISSION_CODE = 123;
    public static final String USER_PDF_FILE = "resume_check";
    private long pressedTime;
    public Bitmap offline_image_store_image_add;
    Uri uri;
    String next_resume_main;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_add);
        initializeUI();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            checkWriteExternalPermission();
        }
        toolbar_setup();
        sharedPref();
        SharedPreferences sharedPreferences = getSharedPreferences("myKey", MODE_PRIVATE);
        value = sharedPreferences.getString("value", null);

        img_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 50);
            }
        });
        upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mkLoader_image_upload.setVisibility(View.VISIBLE);
                uploadFile();
            }
        });

        next_activity_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_next_activity;
                saveImageInRoom();
//                Toast.makeText(ImageAddActivity.this, "" + user_resume_check, Toast.LENGTH_SHORT).show();
                if (user_resume_check.equals("")) {
                    intent_next_activity = new Intent(ImageAddActivity.this, UploadResume.class);
                    startActivity(intent_next_activity);
                    next_resume_main = "upload_resume";
                } else {
                    intent_next_activity = new Intent(ImageAddActivity.this, MasterActivity.class);
                    startActivity(intent_next_activity);
                    next_resume_main = "logged_in";
                    /* else {
                        intent_next_activity = new Intent(ImageAddActivity.this, Verification.class);
                    }*/
                }
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void checkWriteExternalPermission() {
        int result = ContextCompat.checkSelfPermission(ImageAddActivity.this, Manifest.permission.MANAGE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_DENIED) {
            requestPermission();
        }
    }

    public void initializeUI() {
        img_select = findViewById(R.id.select_photo_main);
        upload_image = findViewById(R.id.upload_Image);
        next_activity_btn = findViewById(R.id.image_to_resume);
        toolbar_upload_image = findViewById(R.id.toolbar_actionbar_upload_image);
        mkLoader_image_upload = findViewById(R.id.upload_Image_loader);
        mkLoader_image_upload.setVisibility(View.INVISIBLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 50) {
            try {
                uri = data.getData();
                offline_image_store_image_add = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                uridata = uri.toString();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();
                int clumnIndex = cursor.getColumnIndex(filePathColumn[0]);
                uridata = cursor.getString(clumnIndex);
                Log.e("IMAGEPATH", uridata);
                img_select.setImageURI(uri);
            } catch (Exception e) {
                Log.e("Error", String.valueOf(e));
            }
        }
        if (requestCode == 2296) {
            if (Environment.isExternalStorageManager()) {

            } else {
                Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void toolbar_setup() {
        toolbar_upload_image.setBackground(new ColorDrawable(ContextCompat.getColor(this, R.color.main_color)));
        toolbar_upload_image.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        toolbar_upload_image.setTitle("Upload Image");
    }

    public void saveImageInRoom() {
        ImagesData imagesData = new ImagesData();
        imagesData.setImages(ImageBitmapString.getStringFromBitmap(offline_image_store_image_add));
//        Toast.makeText(loginActivity, "" + offline_image_store, Toast.LENGTH_SHORT).show();
//        Toast.makeText(loginActivity, "" + ImageBitmapString.getStringFromBitmap(offline_image_store), Toast.LENGTH_SHORT).show();
        DataBaseImageProvider.getDbConnection(getApplicationContext()).userImageDao().insert_image(imagesData);
    }

    private void uploadFile() {
        Map<String, RequestBody> map = new HashMap<>();
        try {
            file = new File(uridata);
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            RequestBody id1 = RequestBody.create(MediaType.parse("text/plain"), temp_user_id);


            APIInterface getResponse = AppClient.getclient().create(APIInterface.class);
            Call<ServerResponse> call = getResponse.uploadFile(fileToUpload, id1);
//            Toast.makeText(ImageAddActivity.this, "" + temp_user_id, Toast.LENGTH_SHORT).show();
            call.enqueue(new Callback<ServerResponse>() {
                @Override
                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                    ServerResponse server = response.body();
                    try {
                        if (server.isSuccess()) {
                            upload_image.setVisibility(View.INVISIBLE);
                            next_activity_btn.setVisibility(View.VISIBLE);
                            Toast.makeText(ImageAddActivity.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                            mkLoader_image_upload.setVisibility(View.INVISIBLE);
                        } else {
                            mkLoader_image_upload.setVisibility(View.INVISIBLE);
                            Toast.makeText(ImageAddActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            Log.d("Respon", server.toString());
                        }
                    } catch (Exception e) {
                        mkLoader_image_upload.setVisibility(View.INVISIBLE);
                        Log.e("Error", String.valueOf(e));
                    }
                    // Toast.makeText(ImageAddActivity.this, "Success", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ServerResponse> call, Throwable t) {
                    mkLoader_image_upload.setVisibility(View.INVISIBLE);
                    Toast.makeText(ImageAddActivity.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
                    Log.e("error", String.valueOf(t));
                }
            });
        } catch (Exception e) {
            mkLoader_image_upload.setVisibility(View.INVISIBLE);
            Toast.makeText(ImageAddActivity.this, "Please Select an Image", Toast.LENGTH_SHORT).show();
        }
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
        user_resume_check = sharedPreferences.getString(USER_PDF_FILE, "");
        verify_id = sharedPreferences.getString("verify", "");
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LOGGED_IN, next_resume_main);
        editor.commit();
    }

    @Override
    public void onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
            startActivity(new Intent(ImageAddActivity.this, Login.class));
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }
}