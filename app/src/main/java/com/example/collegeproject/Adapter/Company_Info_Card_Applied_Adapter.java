package com.example.collegeproject.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeWarningDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.example.collegeproject.Activity.CompanyCard;
import com.example.collegeproject.Database.Applicationdatum;
import com.example.collegeproject.R;
import com.squareup.picasso.Picasso;
import com.tuyenmonkey.mkloader.MKLoader;

import java.util.ArrayList;

public class Company_Info_Card_Applied_Adapter extends RecyclerView.Adapter<Company_Info_Card_Applied_Adapter.ViewHolder> {

    public Context context;
    AwesomeWarningDialog awesomeWarningDialog_applied_adapter;
    ArrayList<Applicationdatum> applicationdatumArrayList, applicationdatumArrayList_1;
    Applicationdatum applicationdatum;
    String company_id, user_id, job_id;
    SharedPreferences sharedPreferences;
    public static final String mypreference = "mypref";
    MKLoader mkLoader;
    public static final String USER_ID = "user_id";

    public Company_Info_Card_Applied_Adapter(Context context, ArrayList<Applicationdatum> jobdatumArrayList, MKLoader mkLoader) {
        this.context = context;
        this.applicationdatumArrayList = jobdatumArrayList;
        this.mkLoader = mkLoader;
    }

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.company_info_card_apply_for, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.apply_btn.setVisibility(View.INVISIBLE);
        holder.applied_btn.setVisibility(View.VISIBLE);
        applicationdatum = applicationdatumArrayList.get(position);
        sharedpref();
        applicationdatum = applicationdatumArrayList.get(position);
        setdata(holder);
        holder.applied_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getonclickdata(position);
                displayAlertDialog();
            }
        });

        holder.company_info_applied_card_mainview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getonclickdata(position);
                Intent intent = new Intent(context, CompanyCard.class);
                intent.putExtra("company_id", company_id);
                intent.putExtra("job_id", job_id);
                intent.putExtra("check_apply_applied", "applied");
                context.startActivity(intent);
            }
        });
    }

    public void getonclickdata(int position) {
        Applicationdatum applicationdatum = applicationdatumArrayList.get(position);
        job_id = applicationdatum.getJobId();
        company_id = applicationdatum.getCompanyId();
    }

    public void sharedpref() {
        sharedPreferences = context.getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString(USER_ID, "");
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public Button applied_btn, apply_btn;
        public CardView company_info_applied_card_mainview;
        public ImageView company_logo;
        public TextView card_company_name, card_job_post, card_job_salary, card_job_location;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            applied_btn = itemView.findViewById(R.id.infocard_appliedbtn);
            apply_btn = itemView.findViewById(R.id.infocard_applybtn);
            company_info_applied_card_mainview = itemView.findViewById(R.id.company_info_apply_card_mainview);
            card_company_name = itemView.findViewById(R.id.card_company_name);
            card_job_post = itemView.findViewById(R.id.card_job_post);
            card_job_salary = itemView.findViewById(R.id.card_salary);
            card_job_location = itemView.findViewById(R.id.card_job_location);
            company_logo = itemView.findViewById(R.id.company_logo_image_1);
        }
    }

    public void setdata(ViewHolder holder) {
        try {
            holder.card_company_name.setText(applicationdatum.getCompanyName());
            holder.card_job_post.setText(applicationdatum.getJobPost());
            holder.card_job_salary.setText(applicationdatum.getJobSalary());
            String area = applicationdatum.getCompanyArea();
            String company_logo_string = applicationdatum.getCompanyLogo();
            Picasso.get().load(company_logo_string).into(holder.company_logo);
            mkLoader.setVisibility(View.INVISIBLE);
//            new DownloadImageTaskImageView(holder.company_logo, mkLoader).execute(company_logo_string);
            String location = applicationdatum.getCompanyLocation();
            String main_loc = area + ", " + location;
            holder.card_job_location.setText(main_loc);

        } catch (Exception e) {
            Log.e("ERROR", applicationdatum.getCompanyName());
        }

    }

    public void displayAlertDialog() {
        try {
            awesomeWarningDialog_applied_adapter = new AwesomeWarningDialog(context);
            awesomeWarningDialog_applied_adapter.setTitle("Error")
                    .setMessage("You cannot cancel your application once you have applied for a job")
                    .setColoredCircle(R.color.main_color)
                    .setDialogIconAndColor(R.drawable.ic_dialog_error, R.color.white)
                    .setCancelable(true)
                    .setButtonText("OK")
                    .setButtonBackgroundColor(R.color.red);
            awesomeWarningDialog_applied_adapter.setWarningButtonClick(new Closure() {
                @Override
                public void exec() {

                }
            })
                    .show();
        } catch (Exception e) {

        }
    }

    @Override
    public int getItemCount() {
        return applicationdatumArrayList.size();
    }

}
