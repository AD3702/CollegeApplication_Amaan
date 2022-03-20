package com.example.collegeproject.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.collegeproject.Adapter.DownLoadImageOffline;
import com.example.collegeproject.Database.APIInterface;
import com.example.collegeproject.Database.AppClient;
import com.example.collegeproject.Database.Registrationdatum;
import com.example.collegeproject.Database.UserData;
import com.example.collegeproject.R;
import com.example.collegeproject.Room.DBHelper;
import com.example.collegeproject.Room.DataBaseImageProvider;
import com.example.collegeproject.Room.ImageBitmapString;
import com.example.collegeproject.Room.Offline_User_Data;
import com.example.collegeproject.Room.RoomDB;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tuyenmonkey.mkloader.MKLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ERROR_IMAGE_OFFLINE";
    TextView forgot_pass;
    ImageView newuser;
    MKLoader login_loader;
    Button login_page_btn;
    ArrayList<Registrationdatum> loginRegistrationdata;
    public static Activity loginActivity;
    TextInputEditText login_email, login_password;
    LinearLayout main_layout;
    TextInputLayout email_layout, password_layout;
    String loginemailstring, loginpasswordstring, user_id, user_name, user_email, user_contact, user_address, user_area, user_location, user_english_speaking, user_date_of_birth, user_college_name, user_college_degree, user_college_semester, user_card_type, user_profile_image = null, user_resume, user_college_id_card, user_view_job_type, user_verify_id;
    SharedPreferences sharedPreferences;
    public static final String mypreference = "mypref";
    public static final String USER_ID = "user_id";
    public static final String LOGGED_IN = "log_in";
    public static final String REGISTER_EMAIL = "register_email";
    ImageView header_image;
    String verify_id, isloggedin;
    TextView main_login_text, sub_text;
    float v = 0;
    boolean connected = false;
    List<Offline_User_Data> offline_user_dataArrayList = new ArrayList<Offline_User_Data>();
    RoomDB roomDB;
    DataBaseImageProvider dataBaseImageProvider;
    Offline_User_Data offline_user_data;
    public static Login login_activity;
    public Bitmap offline_image_bitmap;
    private DBHelper dbHelper;
    Toolbar login_toolbar;
    Uri uri_on_login_image;
    ImageButton imageButton_google_login;
    GoogleSignInClient mGoogleSignInClient;
    String image_login_uridata;
    File file;
    Boolean storage_permission_allowed = false;
    private static final int STORAGE_PERMISSION_CODE = 123;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeUI();
        checkWriteExternalPermission();
//        toolbar_setup();
        animations();
//        MainActivity.mainActivity.finish();
        google_signIn();
        forgot_pass.setOnClickListener(this);
        newuser.setOnClickListener(this);
        login_page_btn.setOnClickListener(this);
        imageButton_google_login.setOnClickListener(this);
        roomDB = RoomDB.getInstance(this.getApplicationContext());
    }

    public void google_signIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(Login.this, gso);
    }

    public void connectivity() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        } else {
            Toast.makeText(Login.this, "Not Connected to the Internet", Toast.LENGTH_SHORT).show();
            connected = false;
        }
    }

    public void initializeUI() {
        login_activity = this;
        imageButton_google_login = findViewById(R.id.login_with_google);
        dbHelper = new DBHelper(this);
        offline_user_data = new Offline_User_Data();
        login_loader = findViewById(R.id.login_loader);
        login_loader.setVisibility(View.INVISIBLE);
        newuser = findViewById(R.id.newuser);
        forgot_pass = findViewById(R.id.forgot_password);
        login_email = findViewById(R.id.login_email);
        login_password = findViewById(R.id.login_password);
        login_page_btn = findViewById(R.id.login_page_btn);
        email_layout = findViewById(R.id.login_email_layout);
        password_layout = findViewById(R.id.login_password_layout);
        main_layout = findViewById(R.id.main_lin_layout);
        main_login_text = findViewById(R.id.main_login_text);
        sub_text = findViewById(R.id.sub_text);
        header_image = findViewById(R.id.header_image);
        loginActivity = this;
        login_toolbar = findViewById(R.id.toolbar_actionbar_login_main);
    }

    public void toolbar_setup() {
        login_toolbar.setBackground(new ColorDrawable(ContextCompat.getColor(this, R.color.main_color)));
        login_toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        login_toolbar.setTitle("Login");
        /*setSupportActionBar(login_toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }*/
        login_toolbar.setTitleTextAppearance(Login.this, R.style.ToolBarTextSans);
    }

    private void animations() {

        email_layout.setTranslationX(400);
        password_layout.setTranslationX(400);
        forgot_pass.setTranslationX(400);
        newuser.setTranslationX(400);
        login_page_btn.setTranslationX(400);
        main_login_text.setTranslationX(400);
        sub_text.setTranslationX(400);
//        header_image.setTranslationY(400);

        email_layout.setAlpha(v);
        password_layout.setAlpha(v);
        forgot_pass.setAlpha(v);
        newuser.setAlpha(v);
        login_page_btn.setAlpha(v);
//        header_image.setAlpha(v);
        main_login_text.setAlpha(v);
        sub_text.setAlpha(v);

        sub_text.animate().translationX(0).alpha(1).setDuration(400).setStartDelay(100).start();
        main_login_text.animate().translationX(0).alpha(1).setDuration(400).setStartDelay(100).start();
        email_layout.animate().translationX(0).alpha(1).setDuration(500).setStartDelay(200).start();
        password_layout.animate().translationX(0).alpha(1).setDuration(500).setStartDelay(250).start();
        forgot_pass.animate().translationX(0).alpha(1).setDuration(500).setStartDelay(350).start();
        newuser.animate().translationX(0).alpha(1).setDuration(500).setStartDelay(350).start();
        login_page_btn.animate().translationX(0).alpha(1).setDuration(500).setStartDelay(400).start();
//        header_image.animate().translationY(0).alpha(1).setDuration(500).setStartDelay(400).start();

    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    public void onClick(View view) {
        if (storage_permission_allowed == true) {
            if (view == newuser) {
                Intent intent = new Intent(Login.this, Register.class);
                overridePendingTransition(R.anim.slide_in_right, R.anim.stay);
                startActivity(intent);
                finish();
            }

            if (view == imageButton_google_login) {
                login_page_btn.setOnClickListener(null);
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 100);
            }

            if (view == login_page_btn) {
                connectivity();
                if (connected == true) {
                    onclicklogin();
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        startActivity(new Intent(android.provider.Settings.Panel.ACTION_INTERNET_CONNECTIVITY));
                    }
//                startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
                }
            }
            if (view == forgot_pass) {
                startActivity(new Intent(Login.this, ForgotPassword.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.stay);
            }
        } else {
            checkWriteExternalPermission();
        }
    }

    private void onclicklogin() {
        loginemailstring = login_email.getText().toString();
        loginpasswordstring = login_password.getText().toString();
        if (loginpasswordstring.equals("") || loginemailstring.equals("")) {
            if (loginemailstring.equals("")) {
                Toast.makeText(Login.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
            }
            if (loginpasswordstring.equals("")) {
                Toast.makeText(Login.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
            }
        } else {
            login_loader.setVisibility(View.VISIBLE);
            logindata();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            login_page_btn.setOnClickListener(Login.this);
            Task<GoogleSignInAccount> googleSignInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(googleSignInAccountTask);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            login_loader.setVisibility(View.VISIBLE);
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            GoogleSignInAccount account1 = GoogleSignIn.getLastSignedInAccount(Login.this);
            user_email = account1.getEmail();
            uri_on_login_image = account1.getPhotoUrl();
            load_from_email();
            // Signed in successfully, show authenticated UI.
//            updateUI(account);
        } catch (ApiException e) {
            login_loader.setVisibility(View.INVISIBLE);
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e(TAG, "signInResult:failed code=" + e.getStatusCode());
//            updateUI(null);
        }
    }

    public void load_from_email() {
        login_loader.setVisibility(View.VISIBLE);
        APIInterface apiInterface = AppClient.getclient().create(APIInterface.class);
        Call<UserData> registerCall = apiInterface.user_show();
        registerCall.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                try {
                    loginRegistrationdata = (ArrayList<Registrationdatum>) response.body().getRegistrationdata();
                    int len = loginRegistrationdata.size();
                    int i;
                    int temp_check_email = 0;
                    for (i = 0; i < len; i++) {
                        if (loginRegistrationdata.get(i).getUserEmail().equals(user_email)) {
                            temp_check_email++;
                        }
                    }
                    if (temp_check_email != 0) {
                        loginemailstring = user_email;
                        APIInterface apiInterface1 = AppClient.getclient().create(APIInterface.class);
                        Call<UserData> userDataCall_email = apiInterface1.find_email(user_email);
                        userDataCall_email.enqueue(new Callback<UserData>() {
                            @Override
                            public void onResponse(Call<UserData> call, Response<UserData> response) {

                                ArrayList<Registrationdatum> registrationdatumArrayList_email = (ArrayList<Registrationdatum>) response.body().getRegistrationdata();
                                loginpasswordstring = registrationdatumArrayList_email.get(0).getUserPassword();
                                login_email.setText(loginemailstring);
                                login_password.setText(loginpasswordstring);
                                logindata();
                            }

                            @Override
                            public void onFailure(Call<UserData> call, Throwable t) {
                            }
                        });
                    } else {
                        sharedPreferences = getSharedPreferences(mypreference, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(REGISTER_EMAIL, user_email);
                        editor.commit();
                        startActivity(new Intent(Login.this, Register.class));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.stay);
                        finish();
                    }
                } catch (Exception e) {
                    Log.e("ERROR", String.valueOf(e));
                    login_loader.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                login_loader.setVisibility(View.INVISIBLE);
                Log.e("Error_Failed", String.valueOf(t));
                Toast.makeText(Login.this, "Failed Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void logindata() {

        APIInterface apiInterface = AppClient.getclient().create(APIInterface.class);
        Call<UserData> userDataCall = apiInterface.login(loginemailstring, loginpasswordstring);
        userDataCall.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                try {
                    loginRegistrationdata = (ArrayList<Registrationdatum>) response.body().getRegistrationdata();
                    user_id = loginRegistrationdata.get(0).getUserId();
                    user_name = loginRegistrationdata.get(0).getUserName();
                    user_email = loginRegistrationdata.get(0).getUserEmail();
                    user_contact = loginRegistrationdata.get(0).getUserContact();
                    user_address = loginRegistrationdata.get(0).getUserAddress();
                    user_area = loginRegistrationdata.get(0).getUserArea();
                    user_location = loginRegistrationdata.get(0).getUserLocation();
                    user_english_speaking = loginRegistrationdata.get(0).getUserEnglishSpeaking();
                    user_date_of_birth = loginRegistrationdata.get(0).getUserDateOfBirth();
                    user_college_name = loginRegistrationdata.get(0).getUserCollegeName();
                    user_college_degree = loginRegistrationdata.get(0).getUserCollegeDegree();
                    user_college_semester = loginRegistrationdata.get(0).getUserSemester();
                    user_card_type = loginRegistrationdata.get(0).getUserCardType();
                    user_profile_image = loginRegistrationdata.get(0).getUserProfileImage();
                    user_resume = loginRegistrationdata.get(0).getUserResumePdf();
                    user_college_id_card = (String) loginRegistrationdata.get(0).getUserCollegeIdCard();
                    user_view_job_type = loginRegistrationdata.get(0).getUserViewJobType();
                    verify_id = loginRegistrationdata.get(0).getUserVerificationId();
                    offline_user_data.setUser_id(Integer.parseInt(user_id));
                    offline_user_data.setUser_name(user_name);
                    offline_user_data.setUser_email(user_email);
                    offline_user_data.setUser_contact(user_contact);
                    offline_user_data.setUser_address(user_address);
                    offline_user_data.setUser_area(user_area);
                    offline_user_data.setUser_location(user_location);
                    offline_user_data.setUser_english_speaking(user_english_speaking);
                    offline_user_data.setUser_date_of_birth(user_date_of_birth);
                    offline_user_data.setUser_college_name(user_college_name);
                    offline_user_data.setUser_college_degree(user_college_degree);
                    offline_user_data.setUser_semester(user_college_semester);
                    offline_user_data.setUser_card_type(Integer.parseInt(user_card_type));
                    offline_user_data.setUser_profile_image(user_profile_image);
                    offline_user_data.setUser_resume_pdf(user_resume);
                    offline_user_data.setUser_college_id_card(user_college_id_card);
                    offline_user_data.setUser_view_job_type(user_view_job_type);
                    offline_user_data.setUser_verification_id(verify_id);
                    sharedpref();
                    successonlogin();
                    login_loader.setVisibility(View.INVISIBLE);
                    login_page_btn.setOnClickListener(Login.this);
                    /*UserDao userDao = roomDB.userDao();
                    List<Offline_User_Data> offline_user_data1 = userDao.getlistData();
                    Log.e("LIST", String.valueOf(offline_user_data1));*/

                } catch (Exception e) {
                    Toast.makeText(Login.this, "Error", Toast.LENGTH_SHORT).show();
                    Log.e("ERROR_LOGIN", String.valueOf(e));
                    login_loader.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {

            }
        });

    }

    private void successonlogin() {

        if (user_resume.equals("")) {
            Intent main_intent = new Intent(Login.this, UploadResume.class);
            if (user_profile_image.equals("")) {
                main_intent.setData(uri_on_login_image);
            }
            MainActivity.mainActivity.finish();
            isloggedin = "upload_resume";
            sharedPreferences = getSharedPreferences(mypreference, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(LOGGED_IN, isloggedin);
            editor.putString(USER_ID, user_id);
            editor.commit();
//                Toast.makeText(Login.this, "" + user_id, Toast.LENGTH_SHORT).show();
            startActivity(main_intent);
            roomDB.userDao().delete(offline_user_data);
            roomDB.userDao().insert_user_data(offline_user_data);
            MainActivity.mainActivity.finish();
            finish();
        } else {
            if (user_profile_image.equals("")) {
                APIInterface apiInterface = AppClient.getclient().create(APIInterface.class);
                Toast.makeText(loginActivity, "" + uri_on_login_image, Toast.LENGTH_SHORT).show();
                Call<UserData> userDataCall = apiInterface.upload_image_when_empty(user_id, String.valueOf(uri_on_login_image));
                userDataCall.enqueue(new Callback<UserData>() {
                    @Override
                    public void onResponse(Call<UserData> call, Response<UserData> response) {

                    }

                    @Override
                    public void onFailure(Call<UserData> call, Throwable t) {
                        Toast.makeText(Login.this, "Error_Image", Toast.LENGTH_SHORT).show();
                        login_loader.setVisibility(View.INVISIBLE);
                    }
                });
                Toast.makeText(loginActivity, "" + uri_on_login_image, Toast.LENGTH_SHORT).show();
                new DownLoadImageOffline(Login.this, roomDB, offline_user_data, login_loader).execute(String.valueOf(uri_on_login_image));
                sharedpref();
            } else {
                new DownLoadImageOffline(Login.this, roomDB, offline_user_data, login_loader).execute(user_profile_image);
                sharedpref();
            }
//                saveImageInDB();
//                new ImageDownloader().execute(user_profile_image);

                /*else {
                    login_loader.setVisibility(View.INVISIBLE);
                    Intent main_intent = new Intent(Login.this, Verification.class);
                    Toast.makeText(Login.this, "" + user_id, Toast.LENGTH_SHORT).show();
                    startActivity(main_intent);
                }*/
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    public void checkWriteExternalPermission() {
        int result = ContextCompat.checkSelfPermission(Login.this, Manifest.permission.MANAGE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_DENIED) {
            requestPermission();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void requestPermission() {
        if (Environment.isExternalStorageManager()) {
            storage_permission_allowed = true;
        } else {
            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        }
    }

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
                storage_permission_allowed = true;

            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
                storage_permission_allowed = false;
            }
        }
    }

    public void sharedpref() {
        sharedPreferences = getSharedPreferences(mypreference, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_ID, user_id);
        editor.putString("resume_check", user_resume);
        editor.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MainActivity.mainActivity.finish();
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }


    /*public void saveImageInRoom() {
        ImagesData imagesData = new ImagesData();
        imagesData.setImages(offline_image_store);
        dataBaseImageProvider = DataBaseImageProvider.getDbConnection(Login.this.getApplicationContext());
//        Toast.makeText(loginActivity, "" + offline_image_store, Toast.LENGTH_SHORT).show();
//        Toast.makeText(loginActivity, "" + ImageBitmapString.getStringFromBitmap(offline_image_store), Toast.LENGTH_SHORT).show();
    }
*/
    boolean saveImageInDB() {
        try {
            dbHelper.open();
            new DownLoadImageOffline(Login.this, roomDB, offline_user_data, login_loader).execute(user_profile_image);
            byte[] inputData = ImageBitmapString.getStringFromBitmap(offline_image_bitmap);
            Toast.makeText(loginActivity, "" + inputData, Toast.LENGTH_SHORT).show();
            dbHelper.insertImage(inputData);
            dbHelper.close();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "<saveImageInDB> Error : " + e.getLocalizedMessage());
            dbHelper.close();
            return false;
        }

    }
/*
    private byte[] getLogoImage(String url) {
        byte[] photo;
        try {
            URL imageUrl = new URL(url);
            URLConnection ucon = imageUrl.openConnection();
            System.out.println("11111");
            InputStream is = ucon.getInputStream();
            System.out.println("12121");

            BufferedInputStream bis = new BufferedInputStream(is);
            System.out.println("22222");

            ByteArrayBuffer baf = new ByteArrayBuffer(500);
            int current = 0;
            System.out.println("23333");

            while ((current = bis.read()) != -1) {
                baf.append((byte) current);

            }
            photo = baf.toByteArray();
            System.out.println("photo length" + photo);


        } catch (Exception e) {
            Log.d("ImageManager", "Error: " + e.toString());
        }
        return accImage;
    }

    private class ImageDownloader extends AsyncTask<String, Void, Void> {

        private ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(String... param) {

//            sqlitedatabase_obj.delete(DataBaseHelper.IMG_table, null, null);
//            logoImage = getLogoImage(param[0]);

            saveImageInDB();
            return null;
        }

        @Override
        protected void onPreExecute() {

            progressDialog = ProgressDialog.show(Login.this,
                    "Please Wait", "Organizing Data");

        }

        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();
            Intent intent = new Intent(Login.this, MasterActivity.class);
            startActivity(intent);
            MainActivity.mainActivity.finish();
            finish();
            roomDB.userDao().delete(offline_user_data);
            roomDB.userDao().insert_user_data(offline_user_data);
            login_loader.setVisibility(View.INVISIBLE);
            isloggedin = "logged_in";
            sharedPreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(LOGGED_IN, isloggedin);
            editor.commit();
        }

    }*/
}





























































































