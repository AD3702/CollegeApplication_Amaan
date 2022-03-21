package com.example.collegeproject.Activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
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
import com.example.collegeproject.Room.DBHelper;
import com.example.collegeproject.Room.DataBaseImageProvider;
import com.example.collegeproject.Room.ImagesData;
import com.example.collegeproject.Room.Offline_User_Data;
import com.example.collegeproject.Room.RoomDB;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tuyenmonkey.mkloader.MKLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
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

public class EditUserProfile extends AppCompatActivity implements View.OnClickListener {


    Toolbar toolbar;
    Button edit_profile_main_btn, save_btn;
    MKLoader edit_loader;
    List<String> semester = new ArrayList<>();
    private int mYear, mMonth, mDay;
    DatePickerDialog datePickerDialog;
    LinearLayout edit_prof_date_of_birth_linear;
    String get_user_id;
    SharedPreferences sharedPreferences;
    public static final String mypreference = "mypref";
    public static final String USER_ID = "user_id";
    ArrayList<Registrationdatum> editprofdatalist_online;
    String edit_education;
    int user_id_edit;
    CircleImageView edit_profile_image;
    String uridata;
    int temp_check_bitmap = 0;
    File file;
    String edit_name, edit_user_image_profile, edit_education_1, edit_education_2, edit_english_speak, edit_address, edit_area, edit_location, edit_date_of_birth, edit_contact, edit_email;
    EditText edit_profile_name, edit_profile_degree, edit_profile_english, edit_profile_address, edit_profile_area, edit_profile_location, edit_profile_date_of_birth, edit_profile_contact, edit_profile_email;
    RoomDB roomDB_edit_user;
    TextView edit_profile_semester;
    List<Offline_User_Data> editprofdatalist;
    DataBaseImageProvider dataBaseImageProvider;
    List<ImagesData> imagesDataList;
    DBHelper dbHelper;
    private byte[] accImage;
    MKLoader edit_loader_image;
    boolean setImage_Icon = false;
    Uri uri_image_edit_user_prof;
    LinearLayout user_education_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);
        initializeUI();
        toolbar_setup();
        dbHelper = new DBHelper(this);
        Intent image_intent_get_user_profile_image = getIntent();
        uri_image_edit_user_prof = image_intent_get_user_profile_image.getData();
        sharedPref();
        edituser_profile_offline();
        edit_profile_main_btn.setOnClickListener(this);
        save_btn.setOnClickListener(this);
        edit_profile_image.setOnClickListener(this);
        user_education_layout = findViewById(R.id.user_education_layout);
        /*else{
            Intent full_screen_intent = new Intent(EditUserProfile.this,ViewFullImage.class);
            full_screen_intent.setData(getImageUri(EditUserProfile.this,))
        }*/
    }

    public void setclickable_edit_false() {
        edit_prof_date_of_birth_linear.setOnClickListener(null);
//        edit_profile_semester.setOnClickListener(null);
        user_education_layout.setOnClickListener(null);
    }

    public void setclickable_edit() {
        edit_prof_date_of_birth_linear.setOnClickListener(this);
        user_education_layout.setOnClickListener(this);
    }

    public void initializeUI() {
        edit_loader = findViewById(R.id.edit_details_loader);
        editprofdatalist_online = new ArrayList<>();
        toolbar = findViewById(R.id.toolbar_actionbar_edit_profile_main);
        edit_profile_name = findViewById(R.id.edit_profile_name);
        edit_profile_degree = findViewById(R.id.edit_profile_education_degree);
        edit_profile_semester = findViewById(R.id.edit_profile_education_semester);
        edit_profile_english = findViewById(R.id.edit_profile_english_level);
        edit_profile_address = findViewById(R.id.edit_profile_address);
        edit_profile_date_of_birth = findViewById(R.id.edit_profile_date);
        edit_profile_area = findViewById(R.id.edit_profile_area);
        edit_profile_location = findViewById(R.id.edit_profile_location);
        edit_profile_contact = findViewById(R.id.edit_profile_contact);
        edit_profile_email = findViewById(R.id.edit_profile_email);
        edit_loader_image = findViewById(R.id.edit_user_loader_image);
        edit_profile_main_btn = findViewById(R.id.edit_profile_btn_main);
        save_btn = findViewById(R.id.edit_profile_btn_save);
        edit_prof_date_of_birth_linear = findViewById(R.id.date_of_birth_linear);
        edit_profile_image = findViewById(R.id.edit_profile_image);
    }

    public void toolbar_setup() {
        toolbar.setBackground(new ColorDrawable(ContextCompat.getColor(this, R.color.main_color)));
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        toolbar.setTitle("Edit Profile");
    }

    @Override
    public void onClick(View view) {
        if (view == edit_profile_main_btn) {
            setImage_Icon = true;
            setclickable_edit();
            edit_profile_main_btn.setVisibility(View.INVISIBLE);
            save_btn.setVisibility(View.VISIBLE);
            enableedittextset();
        }
        if (view == save_btn) {
            setImage_Icon = false;
            edit_profile_main_btn.setVisibility(View.VISIBLE);
            edit_loader.setVisibility(View.VISIBLE);
            getdetails_post();
            setclickable_edit_false();
            save_btn.setVisibility(View.INVISIBLE);
            Toast.makeText(EditUserProfile.this, "" + edit_date_of_birth, Toast.LENGTH_SHORT).show();
            disableedittextset();
        }
        if (view == edit_prof_date_of_birth_linear) {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                    edit_date_of_birth = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                    edit_profile_date_of_birth.setText(edit_date_of_birth);
                }
            }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (view == edit_profile_image) {
//            startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 50);
            Dialog mDialog = new Dialog(this, R.style.Theme_With_Action_Bar);
            mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mDialog.setContentView(R.layout.dialog_fullscreen);
            ImageView full_screen_image = mDialog.findViewById(R.id.full_screen_image);
            FloatingActionButton save_image = mDialog.findViewById(R.id.save_image_gallery);
            save_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bitmap bitmap;
                    if (edit_profile_image.getDrawable() instanceof BitmapDrawable) {
                        bitmap = ((BitmapDrawable) edit_profile_image.getDrawable()).getBitmap();
                    } else {
                        Drawable d = edit_profile_image.getDrawable();
                        bitmap = Bitmap.createBitmap(d.getIntrinsicWidth(), d.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                        Canvas canvas = new Canvas(bitmap);
                        d.draw(canvas);
                    }
                    saveImage(bitmap);
                }
            });
            if (uri_image_edit_user_prof == null) {
                full_screen_image.setImageResource(R.drawable.ic_person_white);
            } else {
                full_screen_image.setImageURI(uri_image_edit_user_prof);
            }
            mDialog.show();
        }
        if (view == user_education_layout) {
            android.widget.PopupMenu popupMenu = new android.widget.PopupMenu(EditUserProfile.this, user_education_layout);
            popupMenu.getMenuInflater().inflate(R.menu.semester_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Toast.makeText(EditUserProfile.this, "" + item.getTitle(), Toast.LENGTH_SHORT).show();
                    if (item.getItemId() == R.id.semester_1) {
                        edit_education_2 = String.valueOf(item.getTitle());
                        edit_profile_semester.setText(" ( " + edit_education_2 + " )");
                    }
                    if (item.getItemId() == R.id.semester_2) {
                        edit_education_2 = String.valueOf(item.getTitle());
                        edit_profile_semester.setText(" ( " + edit_education_2 + " )");
                    }
                    if (item.getItemId() == R.id.semester_3) {
                        edit_education_2 = String.valueOf(item.getTitle());
                        edit_profile_semester.setText(" ( " + edit_education_2 + " )");
                    }
                    if (item.getItemId() == R.id.semester_4) {
                        edit_education_2 = String.valueOf(item.getTitle());
                        edit_profile_semester.setText(" ( " + edit_education_2 + " )");
                    }
                    if (item.getItemId() == R.id.semester_5) {
                        edit_education_2 = String.valueOf(item.getTitle());
                        edit_profile_semester.setText(" ( " + edit_education_2 + " )");
                    }
                    if (item.getItemId() == R.id.semester_6) {
                        edit_education_2 = String.valueOf(item.getTitle());
                        edit_profile_semester.setText(" ( " + edit_education_2 + " )");
                    }
                    if (item.getItemId() == R.id.semester_7) {
                        edit_education_2 = String.valueOf(item.getTitle());
                        edit_profile_semester.setText(" ( " + edit_education_2 + " )");
                    }
                    if (item.getItemId() == R.id.semester_8) {
                        edit_education_2 = String.valueOf(item.getTitle());
                        edit_profile_semester.setText(" ( " + edit_education_2 + " )");
                    }
                    if (item.getItemId() == R.id.semester_passed_out) {
                        edit_profile_semester.setText("");
                    }
                    return true;
                }
            });
            popupMenu.show();
        }
    }

    private void saveImage(Bitmap bitmap) {
        if (android.os.Build.VERSION.SDK_INT >= 29) {
            ContentValues values = contentValues();
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/" + getString(R.string.app_name));
            values.put(MediaStore.Images.Media.IS_PENDING, true);

            Uri uri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            if (uri != null) {
                try {
                    saveImageToStream(bitmap, EditUserProfile.this.getContentResolver().openOutputStream(uri));
                    values.put(MediaStore.Images.Media.IS_PENDING, false);
                    this.getContentResolver().update(uri, values, null, null);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        } else {
            File directory = new File(Environment.getExternalStorageDirectory().toString() + '/' + getString(R.string.app_name));

            if (!directory.exists()) {
                directory.mkdirs();
            }
            String fileName = System.currentTimeMillis() + ".png";
            File file = new File(directory, fileName);
            try {
                saveImageToStream(bitmap, new FileOutputStream(file));
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
                this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    private ContentValues contentValues() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        }
        return values;
    }

    private void saveImageToStream(Bitmap bitmap, OutputStream outputStream) {
        if (outputStream != null) {
            try {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                outputStream.close();
                Toast.makeText(EditUserProfile.this, "Image Saved", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void enableedittextset() {
        edit_profile_name.setEnabled(true);
        edit_profile_english.setEnabled(true);
        edit_profile_address.setEnabled(true);
        edit_profile_contact.setEnabled(true);
        edit_profile_email.setEnabled(true);
        edit_profile_area.setEnabled(true);
        edit_profile_location.setEnabled(true);
        temp_check_bitmap = 1;
    }

    public void disableedittextset() {
        edit_profile_name.setEnabled(false);
        edit_profile_english.setEnabled(false);
        edit_profile_address.setEnabled(false);
        edit_profile_contact.setEnabled(false);
        edit_profile_email.setEnabled(false);
        edit_profile_area.setEnabled(false);
        edit_profile_location.setEnabled(false);
        temp_check_bitmap = 0;
    }

    /*public void loadData_edit() {
        APIInterface apiInterface = AppClient.getclient().create(APIInterface.class);
        Call<UserData> registrationCall = apiInterface.find_user_id(get_user_id);
        registrationCall.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                try {
                    editprofdatalist = (ArrayList<Registrationdatum>) response.body().getRegistrationdata();
                    edit_name = editprofdatalist.get(0).getUserName();
                    edit_education_1 = editprofdatalist.get(0).getUserCollegeDegree();
                    edit_education_2 = editprofdatalist.get(0).getUserSemester();
                    edit_english_speak = editprofdatalist.get(0).getUserEnglishSpeaking();
                    edit_address = editprofdatalist.get(0).getUserAddress();
                    edit_area = editprofdatalist.get(0).getUserArea();
                    edit_location = editprofdatalist.get(0).getUserLocation();
                    edit_contact = editprofdatalist.get(0).getUserContact();
                    edit_email = editprofdatalist.get(0).getUserEmail();
                    edit_user_image_profile = editprofdatalist.get(0).getUserProfileImage();
                    setdetails_pre();
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {

            }
        });
    }*/

    public void sharedPref() {
        sharedPreferences = getSharedPreferences(mypreference, MODE_PRIVATE);
        get_user_id = sharedPreferences.getString(USER_ID, "");
        user_id_edit = Integer.parseInt(get_user_id);
    }

    public void setdetails_pre() {
        edit_profile_name.setText(edit_name);
        edit_profile_degree.setText(edit_education_1);
        if (edit_education_2.equals("Passed Out")) {
            edit_profile_semester.setText("");
        } else {
            edit_profile_semester.setText(" ( " + edit_education_2 + " )");
        }
        edit_profile_english.setText(edit_english_speak);
        edit_profile_address.setText(edit_address);
        edit_profile_contact.setText(edit_contact);
        edit_profile_email.setText(edit_email);
        edit_profile_area.setText(edit_area);
        edit_profile_location.setText(edit_location);
        edit_profile_date_of_birth.setText(edit_date_of_birth);
//        Toast.makeText(EditUserProfile.this, "" + uri_image_edit_user_prof, Toast.LENGTH_SHORT).show();
        edit_profile_image.setImageURI(uri_image_edit_user_prof);
        edit_loader_image.setVisibility(View.INVISIBLE);
//        new DownLoadImageTask(edit_profile_image, edit_loader_image).execute(edit_user_image_profile);
        Log.e("TAG", edit_user_image_profile);
        edit_loader.setVisibility(View.INVISIBLE);
        /*dataBaseImageProvider = DataBaseImageProvider.getDbConnection(EditUserProfile.this.getApplicationContext());
        imagesDataList = dataBaseImageProvider.userImageDao().getAllImage();*/
        //ImagesData imagesData = imagesDataList.get(0);
        /*Bitmap set_image = ImageBitmapString.getBitmapFromStr(imagesData.getImages());
        Toast.makeText(EditUserProfile.this, "" + imagesData.getImages(), Toast.LENGTH_SHORT).show();
        */
//        edit_profile_image.setImageBitmap(bitmapImage);
//        loadImageFromDB();
    }

    /*void loadImageFromDB() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    dbHelper.open();
                    final byte[] retreiveImageFromDB_byte = dbHelper.retreiveImageFromDB();
                    if (retreiveImageFromDB_byte == null) {
                        Toast.makeText(EditUserProfile.this, "null", Toast.LENGTH_SHORT).show();
                    }
                    dbHelper.close();
                    // Show Image from DB in ImageView
                    edit_profile_image.post(new Runnable() {
                        @Override
                        public void run() {
                            edit_profile_image.setImageBitmap(Utils.getImage(retreiveImageFromDB_byte));
                        }
                    });
                } catch (Exception e) {
                    Log.e("TAG", "<loadImageFromDB> Error : " + e.getLocalizedMessage());
                    dbHelper.close();
                }
            }
        }).start();
    }*/

    public void getdetails_post() {
        edit_date_of_birth = edit_profile_date_of_birth.getText().toString();
        edit_name = edit_profile_name.getText().toString();
        edit_english_speak = edit_profile_english.getText().toString();
        edit_address = edit_profile_address.getText().toString();
        edit_contact = edit_profile_contact.getText().toString();
        edit_email = edit_profile_email.getText().toString();
        edit_area = edit_profile_area.getText().toString();
        edit_location = edit_profile_location.getText().toString();
        setData_edit();
    }


    public void setdetails_post() {
        edit_profile_name.setText(edit_name);
        edit_profile_english.setText(edit_english_speak);
        edit_profile_address.setText(edit_address);
        edit_profile_contact.setText(edit_contact);
        edit_profile_email.setText(edit_email);
        edit_profile_area.setText(edit_area);
        edit_profile_location.setText(edit_location);
        edit_profile_english.setText(edit_english_speak);
//        new DownLoadImageTask(edit_profile_image, edit_loader).execute(edit_user_image_profile);
    }

    public void setData_edit() {
        APIInterface apiInterface = AppClient.getclient().create(APIInterface.class);
        Call<UserData> registrationCall = apiInterface.update_profile(edit_name, edit_email, edit_contact, edit_address, edit_area, edit_location, edit_date_of_birth, edit_english_speak, get_user_id);
        registrationCall.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                try {
                    Offline_User_Data offline_user_data = editprofdatalist.get(0);
                    offline_user_data.setUser_name(edit_name);
                    offline_user_data.setUser_email(edit_email);
                    offline_user_data.setUser_contact(edit_contact);
                    offline_user_data.setUser_address(edit_address);
                    offline_user_data.setUser_area(edit_area);
                    offline_user_data.setUser_location(edit_location);
                    offline_user_data.setUser_english_speaking(edit_english_speak);
                    offline_user_data.setUser_date_of_birth(edit_date_of_birth);
                    offline_user_data.setUser_profile_image(edit_user_image_profile);
                    roomDB_edit_user.userDao().update_edit_prof(offline_user_data);
                    /*roomDB_edit_user.userDao().delete(offline_user_data);
                    roomDB_edit_user.userDao().insert_user_data(offline_user_data);*/
                    Toast.makeText(EditUserProfile.this, "" + editprofdatalist.get(0).getUser_address(), Toast.LENGTH_SHORT).show();
                    setdetails_post();

                } catch (Exception e) {

                    Log.e("ERROR", String.valueOf(e));
                }
                edit_loader.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                Toast.makeText(EditUserProfile.this, "Error", Toast.LENGTH_SHORT).show();
                edit_loader.setVisibility(View.INVISIBLE);
                Log.e("Error", String.valueOf(t));
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 50) {
            try {
                Uri uri = data.getData();
                uridata = uri.toString();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();
                int clumnIndex = cursor.getColumnIndex(filePathColumn[0]);
                uridata = cursor.getString(clumnIndex);
                Log.e("IMAGEPATH", uridata);
                edit_profile_image.setImageURI(uri);
                uploadFile();
            } catch (Exception e) {
                Log.e("Error", String.valueOf(e));
            }
        }
    }

    private void uploadFile() {
        Map<String, RequestBody> map = new HashMap<>();
        edit_loader.setVisibility(View.VISIBLE);
        try {
            file = new File(uridata);
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            RequestBody id1 = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(user_id_edit));


            APIInterface getResponse = AppClient.getclient().create(APIInterface.class);
            Call<ServerResponse> call = getResponse.uploadFile(fileToUpload, id1);
            call.enqueue(new Callback<ServerResponse>() {
                @Override
                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                    ServerResponse server = response.body();
                    try {
                        if (server.isSuccess()) {
                            APIInterface apiInterface = AppClient.getclient().create(APIInterface.class);
                            Call<UserData> userDataCall = apiInterface.find_user_id(get_user_id);
                            userDataCall.enqueue(new Callback<UserData>() {
                                @Override
                                public void onResponse(Call<UserData> call, Response<UserData> response) {
                                    ArrayList<Registrationdatum> registrationdatumArrayList = (ArrayList<Registrationdatum>) response.body().getRegistrationdata();
                                    edit_loader.setVisibility(View.INVISIBLE);
                                    edit_user_image_profile = registrationdatumArrayList.get(0).getUserProfileImage();
                                    Offline_User_Data offline_user_data = editprofdatalist.get(0);
                                    offline_user_data.setUser_profile_image(edit_user_image_profile);
                                    roomDB_edit_user.userDao().update_edit_prof(offline_user_data);
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

    public void edituser_profile_offline() {
        try {
            roomDB_edit_user = RoomDB.getInstance(this.getApplicationContext());
            editprofdatalist = roomDB_edit_user.userDao().getlistData();
            edit_name = editprofdatalist.get(0).getUser_name();
            edit_education_1 = editprofdatalist.get(0).getUser_college_degree();
            edit_education_2 = editprofdatalist.get(0).getUser_semester();
            edit_english_speak = editprofdatalist.get(0).getUser_english_speaking();
            edit_address = editprofdatalist.get(0).getUser_address();
            edit_area = editprofdatalist.get(0).getUser_area();
            edit_location = editprofdatalist.get(0).getUser_location();
            edit_contact = editprofdatalist.get(0).getUser_contact();
            edit_email = editprofdatalist.get(0).getUser_email();
            edit_date_of_birth = editprofdatalist.get(0).getUser_date_of_birth();
            edit_user_image_profile = editprofdatalist.get(0).getUser_profile_image();
            setdetails_pre();

        } catch (Exception e) {
            Log.e("OFFLINE ERROR", String.valueOf(e));
        }
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        startActivity(new Intent(EditUserProfile.this, MasterActivity.class));
//        MasterActivity.masteractivity.finish();
//        finish();
//        MasterActivity.masteractivity.oneditprofbackpressed();
//    }
}