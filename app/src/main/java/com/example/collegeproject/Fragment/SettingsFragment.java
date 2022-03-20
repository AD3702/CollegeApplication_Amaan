package com.example.collegeproject.Fragment;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.collegeproject.Activity.ContactUs;
import com.example.collegeproject.Activity.EditUserProfile;
import com.example.collegeproject.Activity.Feedback;
import com.example.collegeproject.Activity.FollowUS;
import com.example.collegeproject.Activity.MainActivity;
import com.example.collegeproject.Activity.MasterActivity;
import com.example.collegeproject.Activity.Security;
import com.example.collegeproject.Activity.UserCard;
import com.example.collegeproject.Adapter.Download_Image_On_Settings_Update;
import com.example.collegeproject.Database.APIInterface;
import com.example.collegeproject.Database.AppClient;
import com.example.collegeproject.Database.Registrationdatum;
import com.example.collegeproject.Database.ServerResponse;
import com.example.collegeproject.Database.UserData;
import com.example.collegeproject.R;
import com.example.collegeproject.Room.DBHelper;
import com.example.collegeproject.Room.DataBaseImageProvider;
import com.example.collegeproject.Room.ImagesData;
import com.example.collegeproject.Room.Offline_User_Data;
import com.example.collegeproject.Room.RoomDB;
import com.example.collegeproject.Room.Utils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.tuyenmonkey.mkloader.MKLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsFragment extends Fragment implements View.OnClickListener {

    public static final String mypreference = "mypref";
    public static final String USER_ID = "user_id";
    public static final String LOGGED_IN = "log_in";
    public static final String IMAGE_UPLOAD_CHECK = "image_upload_check";
    public static final String IMAGE_UPLOAD_URI = "image_upload_URI";
    public static final String SECURITY_SWITCH = "security_switch";
    public static SettingsFragment settingsFragment;
    public CircleImageView settingsprofileimage;
    public Uri image_uri_from_login;
    public String settingsprofileimage_string;
    Toolbar toolbar_settings;
    public ImageView imageView_settings_aplha;
    LinearLayout settings_main, feedback_settings, edit_profile_settings, logout, change_password_settings, settings_share, settings_contact_us, settings_follow_us;
    CardView user_info_main_card_settings;
    TextView settings_name, settings_education;
    ArrayList<Registrationdatum> loginRegistrationdata;
    String settings_Name, settings_Education, userID, user_college_degree, user_college_semester, user_image_set;
    SharedPreferences sharedPreferences;
    String isloggedin;
    boolean security_enabled;
    MKLoader settings_image_loader, settings_loader;
    Boolean connected;
    RoomDB roomDB;
    List<Offline_User_Data> offline_user_data_settings_reset;
    List<Offline_User_Data> offline_user_data;
    List<ImagesData> imagesDataList;
    List<ImagesData> imagesDataList_reset;
    DBHelper dbHelper;
    String uridata;
    File file;
    String check_if_new_image_upload;
    Fragment fragment;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        initializeUI(view);
        toolbar_setup();
        sharedPref();
        Intent image_intent_login = getActivity().getIntent();
        if (check_if_new_image_upload.equals("not_uploaded")) {
            image_uri_from_login = image_intent_login.getData();
        } else {
            sharedPreferences = getActivity().getSharedPreferences(mypreference, Context.MODE_PRIVATE);
            image_uri_from_login = Uri.parse(sharedPreferences.getString(IMAGE_UPLOAD_URI, ""));
        }
        loadOfflineData();
        return view;
    }

    public void setclickable_settings() {
        settingsprofileimage.setOnClickListener(this);
        feedback_settings.setOnClickListener(this);
        edit_profile_settings.setOnClickListener(this);
        user_info_main_card_settings.setOnClickListener(this);
        change_password_settings.setOnClickListener(this);
        logout.setOnClickListener(this);
        settings_share.setOnClickListener(this);
        settings_contact_us.setOnClickListener(this);
        settings_follow_us.setOnClickListener(this);

    }

    public void setclickable_settings_false() {
        settingsprofileimage.setOnClickListener(null);
        feedback_settings.setOnClickListener(null);
        edit_profile_settings.setOnClickListener(null);
        user_info_main_card_settings.setOnClickListener(null);
        change_password_settings.setOnClickListener(null);
        logout.setOnClickListener(null);
        settings_share.setOnClickListener(null);
        settings_contact_us.setOnClickListener(null);
        settings_follow_us.setOnClickListener(null);

    }

    public void loadOfflineData() {
        try {
            roomDB = RoomDB.getInstance(getActivity().getApplicationContext());
            offline_user_data = roomDB.userDao().getlistData();
            settings_Name = offline_user_data.get(0).getUser_name();
            user_college_degree = offline_user_data.get(0).getUser_college_degree();
            user_college_semester = offline_user_data.get(0).getUser_semester();
            settingsprofileimage_string = offline_user_data.get(0).getUser_profile_image();
            /*user_image_set = offline_user_data.get(0).getUser_profile_image();*/
            imageView_settings_aplha.setVisibility(View.INVISIBLE);
            setOfflineuserDetails();
        } catch (Exception e) {
            Log.e("OFFLINE ERROR", String.valueOf(e));
        }
    }

    void loadImageFromDB() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    dbHelper.open();
                    final byte[] retreiveImageFromDB_byte = dbHelper.retreiveImageFromDB();
                    if (retreiveImageFromDB_byte == null) {
                        Toast.makeText(getActivity(), "null", Toast.LENGTH_SHORT).show();
                    }
                    dbHelper.close();
                    // Show Image from DB in ImageView
                    settingsprofileimage.post(new Runnable() {
                        @Override
                        public void run() {
                            settingsprofileimage.setImageBitmap(Utils.getImage(retreiveImageFromDB_byte));
                        }
                    });
                } catch (Exception e) {
                    Log.e("TAG", "<loadImageFromDB> Error : " + e.getLocalizedMessage());
                    dbHelper.close();
                }
            }
        }).start();
    }

    public void initializeUI(View view) {
        settingsFragment = this;
        settings_image_loader = view.findViewById(R.id.setting_image_spinner);
        settings_loader = view.findViewById(R.id.settings_loader);
        offline_user_data_settings_reset = new ArrayList<>();
        offline_user_data = new ArrayList<>();
        settings_main = view.findViewById(R.id.linear_layout_settings_main);
        imageView_settings_aplha = view.findViewById(R.id.aplha_settings_image);
        settings_contact_us = view.findViewById(R.id.settings_contact_us);
        settings_share = view.findViewById(R.id.settings_share);
        settings_follow_us = view.findViewById(R.id.settings_follow_us);
        change_password_settings = view.findViewById(R.id.security_settings);
        loginRegistrationdata = new ArrayList<>();
        settingsprofileimage = view.findViewById(R.id.setting_profile_image);
        settings_name = view.findViewById(R.id.settings_name);
        settings_education = view.findViewById(R.id.settings_education);
        toolbar_settings = view.findViewById(R.id.toolbar_actionbar);
        feedback_settings = view.findViewById(R.id.feedback_settings);
        edit_profile_settings = view.findViewById(R.id.edit_profile_settings);
        user_info_main_card_settings = view.findViewById(R.id.user_info_main_card_settings);
        logout = view.findViewById(R.id.logout);
        dbHelper = new DBHelper(getActivity());
    }

    public void toolbar_setup() {
        toolbar_settings.setBackground(new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.main_color)));
        toolbar_settings.setTitleTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        toolbar_settings.setTitle("Settings");
    }

    @Override
    public void onClick(View view) {
        if (view == feedback_settings) {
            Intent intent_settings_feedback = new Intent(getActivity(), Feedback.class);
            startActivity(intent_settings_feedback);
        }
        if (view == edit_profile_settings) {
            Intent intent_settings_edit_card = new Intent(getActivity(), UserCard.class);
//            Toast.makeText(getActivity(), "" + image_uri_from_login, Toast.LENGTH_SHORT).show();
            intent_settings_edit_card.setData(image_uri_from_login);
            startActivity(intent_settings_edit_card);
        }
        if (view == user_info_main_card_settings) {
            Intent intent_settings_main_profile = new Intent(getActivity(), EditUserProfile.class);
//            Toast.makeText(getActivity(), "" + image_uri_from_login, Toast.LENGTH_SHORT).show();
            intent_settings_main_profile.setData(image_uri_from_login);
            startActivity(intent_settings_main_profile);
        }
        if (view == logout) {
            settings_loader.setVisibility(View.VISIBLE);
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
            mGoogleSignInClient.signOut().addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    settings_loader.setVisibility(View.INVISIBLE);
                    MasterActivity.masteractivity.finish();
                    startActivity(new Intent(getActivity(), MainActivity.class));
                    isloggedin = "logged_out";
                    security_enabled = false;
                    sharedPreferences = getActivity().getSharedPreferences(mypreference, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(LOGGED_IN, isloggedin);
                    editor.putBoolean(SECURITY_SWITCH, security_enabled);
                    editor.commit();
                    RoomDB roomDB = RoomDB.getInstance(getActivity());
                    offline_user_data_settings_reset = roomDB.userDao().getlistData();
                    roomDB.userDao().reset(offline_user_data_settings_reset);
                    DataBaseImageProvider dataBaseImageProvider = DataBaseImageProvider.getDbConnection(getActivity());
                    imagesDataList_reset = dataBaseImageProvider.userImageDao().getAllImage();
                    dataBaseImageProvider.userImageDao().reset(imagesDataList_reset);
                }
            });
        }
        if (view == change_password_settings) {
            startActivity(new Intent(getActivity(), Security.class));
        }
        if (view == settings_contact_us) {
            startActivity(new Intent(getActivity(), ContactUs.class));
        }
        if (view == settings_follow_us) {
            startActivity(new Intent(getActivity(), FollowUS.class));
        }
        if (view == settings_share) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            /*This will be the actual content you wish you share.*/
            String shareBody = "HELLO THIS IS A USER RESUME APP DOWNLOAD THIS APPLICATION FROM PLAY STORE";
            /*The type of the content is text, obviously.*/
            intent.setType("text/plain");
            /*Applying information Subject and Body.*/
            intent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(intent, ""));
        }
        if (view == settingsprofileimage) {
            startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 50);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 50) {
            try {
                Uri uri = data.getData();
                uridata = uri.toString();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(uri, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();
                int clumnIndex = cursor.getColumnIndex(filePathColumn[0]);
                uridata = cursor.getString(clumnIndex);
                Log.e("IMAGEPATH", uridata);
                settingsprofileimage.setImageURI(uri);
                uploadFile();
            } catch (Exception e) {
                Log.e("Error", String.valueOf(e));
            }
        }
    }


    /*@Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }*/


    private void uploadFile() {
        Map<String, RequestBody> map = new HashMap<>();
        settings_loader.setVisibility(View.VISIBLE);
        try {
            file = new File(uridata);
            setclickable_settings_false();
            imageView_settings_aplha.setVisibility(View.VISIBLE);
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            RequestBody id1 = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(userID));


            APIInterface getResponse = AppClient.getclient().create(APIInterface.class);
            Call<ServerResponse> call = getResponse.uploadFile(fileToUpload, id1);
            call.enqueue(new Callback<ServerResponse>() {
                @Override
                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                    ServerResponse server = response.body();
                    try {
                        if (server.isSuccess()) {
                            APIInterface apiInterface = AppClient.getclient().create(APIInterface.class);
                            Call<UserData> userDataCall = apiInterface.find_user_id(userID);
                            userDataCall.enqueue(new Callback<UserData>() {
                                @Override
                                public void onResponse(Call<UserData> call, Response<UserData> response) {
                                    ArrayList<Registrationdatum> registrationdatumArrayList = (ArrayList<Registrationdatum>) response.body().getRegistrationdata();
                                    settingsprofileimage_string = registrationdatumArrayList.get(0).getUserProfileImage();
                                    Offline_User_Data offline_user_data_1 = offline_user_data.get(0);
                                    offline_user_data_1.setUser_profile_image(settingsprofileimage_string);
                                    roomDB.userDao().update_edit_prof(offline_user_data_1);
                                    new Download_Image_On_Settings_Update(getActivity(), settings_loader).execute(settingsprofileimage_string);
                                    sharedPreferences = getActivity().getSharedPreferences(mypreference, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(IMAGE_UPLOAD_CHECK, "uploaded");
                                    editor.commit();
                                    SettingsFragment settingsFragment = new SettingsFragment();
                                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                                    ft.replace(R.id.nav_host_fragment, settingsFragment);
                                    ft.commit();
                                }

                                @Override
                                public void onFailure(Call<UserData> call, Throwable t) {

                                }
                            });
                        } else {
                            Log.d("Respon", server.toString());
                        }
                    } catch (Exception e) {
                        Log.e("Error", String.valueOf(e));
                    }
                    // Toast.makeText(ImageAddActivity.this, "Success", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ServerResponse> call, Throwable t) {
                    Log.e("error", String.valueOf(t));
                }
            });
        } catch (Exception e) {

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

    /*public void connectivity() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            loadData();
            connected = true;
        } else {
            Toast.makeText(getActivity(), "Not Connected to the Internet", Toast.LENGTH_SHORT).show();
            connected = false;
        }
    }*/

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
        sharedPreferences = this.getActivity().getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        userID = sharedPreferences.getString(USER_ID, "");
        check_if_new_image_upload = sharedPreferences.getString(IMAGE_UPLOAD_CHECK, "");
        /*SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LOGGED_IN, isloggedin);
        editor.commit();*/
    }

    /*public void loadData() {
        APIInterface apiInterface = AppClient.getclient().create(APIInterface.class);
        Call<UserData> registrationCall = apiInterface.find_user_id(userID);
        registrationCall.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                try {
                    loginRegistrationdata = (ArrayList<Registrationdatum>) response.body().getRegistrationdata();
                    settings_Name = loginRegistrationdata.get(0).getUserName();
                    user_college_degree = loginRegistrationdata.get(0).getUserCollegeDegree();
                    user_college_semester = loginRegistrationdata.get(0).getUserSemester();
                    user_image_set = loginRegistrationdata.get(0).getUserProfileImage();
                    setUserDetails();
                } catch (Exception e) {

                }

            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                settings_loader.setVisibility(View.INVISIBLE);
                Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                Log.e("SETTING_ERROR", String.valueOf(t));
            }
        });
    }

    public void setUserDetails() {
        settings_Education = user_college_degree + " ( " + user_college_semester + " )";
        settings_education.setText(settings_Education);
        settings_name.setText(settings_Name);
        settings_loader.setVisibility(View.INVISIBLE);
        imageView_settings_aplha.setVisibility(View.INVISIBLE);
        setclickable_settings();
//        new DownLoadSettingsImageTask(settingsprofileimage, settings_loader, imageView_settings_aplha).execute(user_image_set);
    }*/

    public void setOfflineuserDetails() {
        settings_Education = user_college_degree + " ( " + user_college_semester + " )";
        settings_education.setText(settings_Education);
        settings_name.setText(settings_Name);
//        new DownLoadImageTask(settingsprofileimage, settings_image_loader).execute(settingsprofileimage_string);
//        saveImageInDB();
        settingsprofileimage.setImageURI(image_uri_from_login);
        settings_image_loader.setVisibility(View.INVISIBLE);
        setclickable_settings();
    }

   /* boolean saveImageInDB() {
        try {
            Bitmap temp_bitmap;
            if (settingsprofileimage.getDrawable() instanceof BitmapDrawable) {
                temp_bitmap = ((BitmapDrawable) settingsprofileimage.getDrawable()).getBitmap();
            } else {
                Drawable d = settingsprofileimage.getDrawable();
                temp_bitmap = Bitmap.createBitmap(d.getIntrinsicWidth(), d.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(temp_bitmap);
                d.draw(canvas);
            }
            dbHelper.open();
            byte[] inputData = ImageBitmapString.getStringFromBitmap(temp_bitmap);
            dbHelper.insertImage(inputData);
            dbHelper.close();
            return true;
        } catch (Exception e) {
            Log.e("TAG", "<saveImageInDB> Error : " + e.getLocalizedMessage());
            dbHelper.close();
            return false;
        }

    }*/

}