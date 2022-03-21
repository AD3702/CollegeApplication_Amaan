package com.example.collegeproject.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeWarningDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.example.collegeproject.Activity.CompanyCard;
import com.example.collegeproject.Database.APIInterface;
import com.example.collegeproject.Database.AppClient;
import com.example.collegeproject.Database.ApplicationData;
import com.example.collegeproject.Database.Applicationdatum;
import com.example.collegeproject.Fragment.Apply_For_Fragment;
import com.example.collegeproject.Fragment.HomeFragment;
import com.example.collegeproject.R;
import com.squareup.picasso.Picasso;
import com.tuyenmonkey.mkloader.MKLoader;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Company_Info_Card_Apply_For_Adapter extends RecyclerView.Adapter<Company_Info_Card_Apply_For_Adapter.MyViewHolder> {

    public Context context;
    public Fragment fragment;
    AwesomeWarningDialog awesomeWarningDialog_apply_for_adapter_error;
    AwesomeSuccessDialog awesomeSuccessDialog_apply_for_adapter_success;
    ArrayList<Applicationdatum> applicationdatumArrayList;
    String company_id, user_id, job_id;
    SharedPreferences sharedPreferences;
    public static final String mypreference = "mypref";
    public static final String USER_ID = "user_id";
    Applicationdatum applicationdatum;
    MKLoader mkLoader;

    public Company_Info_Card_Apply_For_Adapter(Context context, ArrayList<Applicationdatum> applicationdatumArrayList, MKLoader mkLoader) {
        this.context = context;
        this.applicationdatumArrayList = applicationdatumArrayList;
        this.mkLoader = mkLoader;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.company_info_card_apply_for, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        applicationdatum = applicationdatumArrayList.get(position);
        String company_logo_string = applicationdatum.getCompanyLogo();
        Picasso.get().load(company_logo_string).into(holder.company_logo);
        holder.mkLoader_applyfor.setVisibility(View.INVISIBLE);
//        new DownloadImageTaskImageView(holder.company_logo, holder.mkLoader_applyfor).execute(company_logo_string);
        holder.apply_for.setVisibility(View.VISIBLE);
        holder.applied_infobtn.setVisibility(View.INVISIBLE);
        sharedpref();
        setdata(holder);
        holder.apply_for.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getonclickdata(position);
                displaySuccessAlertDialog(holder);
            }
        });
        holder.applied_infobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayErrorAlertDialog();
            }
        });
        holder.company_info_apply_for_card_mainview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getonclickdata(position);
                Toast.makeText(context, "" + position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, CompanyCard.class);
                intent.putExtra("company_id", company_id);
                intent.putExtra("job_id", job_id);
                intent.putExtra("check_apply_applied", "apply");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        holder.linearLayout_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void getonclickdata(int position) {
        Applicationdatum applicationdatum = applicationdatumArrayList.get(position);
        job_id = applicationdatum.getJobId();
        company_id = applicationdatum.getCompanyId();
    }

    @Override
    public int getItemCount() {
        return applicationdatumArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public Button apply_for, applied_infobtn;
        public CardView company_info_apply_for_card_mainview;
        ImageView company_logo;
        public MKLoader mkLoader_applyfor;
        public LinearLayout linearLayout_location;
        public TextView card_company_name, card_job_post, card_job_salary, card_job_location;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            applied_infobtn = itemView.findViewById(R.id.infocard_appliedbtn);
            apply_for = itemView.findViewById(R.id.infocard_applybtn);
            company_info_apply_for_card_mainview = itemView.findViewById(R.id.company_info_apply_card_mainview);
            card_company_name = itemView.findViewById(R.id.card_company_name);
            card_job_post = itemView.findViewById(R.id.card_job_post);
            card_job_salary = itemView.findViewById(R.id.card_salary);
            card_job_location = itemView.findViewById(R.id.card_job_location);
            company_logo = itemView.findViewById(R.id.company_logo_image_1);
            mkLoader_applyfor = itemView.findViewById(R.id.image_loader_apply);
            linearLayout_location = itemView.findViewById(R.id.location_apply_for_layout);
            mkLoader_applyfor.setVisibility(View.VISIBLE);

        }
    }

    public String get_date() {
        int mYear, mMonth, mDay;
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mMonth = mMonth + 1;
        String curr_date = mYear + "-" + (mMonth) + "-" + mDay;
        return curr_date;
    }

    public void sharedpref() {
        sharedPreferences = context.getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString(USER_ID, "");
    }

    public void appy_for_job_data() {
        Apply_For_Fragment.mkLoader.setVisibility(View.VISIBLE);
        APIInterface apiInterface = AppClient.getclient().create(APIInterface.class);
        String current_date = get_date();
        Call<ApplicationData> applicationDataCall = apiInterface.apply_for_job(company_id, user_id, job_id, current_date);
        applicationDataCall.enqueue(new Callback<ApplicationData>() {
            @Override
            public void onResponse(Call<ApplicationData> call, Response<ApplicationData> response) {
                Apply_For_Fragment.mkLoader.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<ApplicationData> call, Throwable t) {

            }
        });
    }

    public void displaySuccessAlertDialog(Company_Info_Card_Apply_For_Adapter.MyViewHolder holder) {
        try {
            awesomeSuccessDialog_apply_for_adapter_success = new AwesomeSuccessDialog(context);
            awesomeSuccessDialog_apply_for_adapter_success.setTitle("Confirmation" + company_id + "_" + job_id)
                    .setMessage("Are you sure you want to apply?")
                    .setColoredCircle(R.color.main_color)
                    .setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white)
                    .setCancelable(true)
                    .setPositiveButtonText("YES")
                    .setPositiveButtonbackgroundColor(R.color.dialogSuccessBackgroundColor)
                    .setPositiveButtonTextColor(R.color.white)
                    .setNegativeButtonText("No")
                    .setNegativeButtonbackgroundColor(R.color.dialogSuccessBackgroundColor)
                    .setNegativeButtonTextColor(R.color.white)
                    .setPositiveButtonClick(new Closure() {
                        @Override
                        public void exec() {
                            appy_for_job_data();
                            holder.apply_for.setVisibility(View.INVISIBLE);
                            holder.applied_infobtn.setVisibility(View.VISIBLE);
                            fragment = new Apply_For_Fragment();
                            FragmentManager fm = ((FragmentActivity) context).getSupportFragmentManager();
                            FragmentTransaction ft = fm.beginTransaction();
                            ft.replace(HomeFragment.frame_id_int, fragment);
                            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                            ft.commit();
                        }
                    })
                    .setNegativeButtonClick(new Closure() {
                        @Override
                        public void exec() {
                            //click
                        }
                    })
                    .show();
        } catch (Exception e) {

        }
    }

    public void displayErrorAlertDialog() {
        try {
            awesomeWarningDialog_apply_for_adapter_error = new AwesomeWarningDialog(context);
            awesomeWarningDialog_apply_for_adapter_error.setTitle("Error" + company_id + "_" + job_id)
                    .setMessage("You cannot cancel your application once you have applied for a job")
                    .setColoredCircle(R.color.main_color)
                    .setDialogIconAndColor(R.drawable.ic_dialog_error, R.color.white)
                    .setCancelable(true)
                    .setButtonText("OK")
                    .setButtonBackgroundColor(R.color.red);
            awesomeWarningDialog_apply_for_adapter_error.setWarningButtonClick(new Closure() {
                @Override
                public void exec() {

                }
            })
                    .show();
        } catch (Exception e) {

        }
    }

    public void setdata(Company_Info_Card_Apply_For_Adapter.MyViewHolder holder) {
        holder.card_company_name.setText(applicationdatum.getCompanyName());
        holder.card_job_post.setText(applicationdatum.getJobPost());
        holder.card_job_salary.setText(applicationdatum.getJobSalary());
        String area = applicationdatum.getCompanyArea();
        String location = applicationdatum.getCompanyLocation();
        String main_loc = area + ", " + location;
        holder.card_job_location.setText(main_loc);
        company_id = applicationdatum.getCompanyId();
        job_id = applicationdatum.getJobId();
    }
}
