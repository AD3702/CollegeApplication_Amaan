package com.example.collegeproject.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeWarningDialog;
import com.example.collegeproject.Activity.CompanyCard;
import com.example.collegeproject.Database.Applicationdatum;
import com.example.collegeproject.R;
import com.squareup.picasso.Picasso;
import com.tuyenmonkey.mkloader.MKLoader;

import java.util.ArrayList;

public class Total_Jobs_Adapter extends RecyclerView.Adapter<Total_Jobs_Adapter.ViewHolder> {

    public Context context;
    AwesomeWarningDialog awesomeWarningDialog_applied_adapter;
    ArrayList<Applicationdatum> applicationdatumArrayList, applicationdatumArrayList_1;
    Applicationdatum applicationdatum;
    String company_id, user_id, job_id;
    SharedPreferences sharedPreferences;
    public static final String mypreference = "mypref";
    public static final String USER_ID = "user_id";
    MKLoader mkLoader;


    public Total_Jobs_Adapter(Context context, ArrayList<Applicationdatum> jobdatumArrayList, MKLoader mkLoader) {
        this.context = context;
        this.mkLoader = mkLoader;
        this.applicationdatumArrayList = jobdatumArrayList;
    }

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.total_jobs_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        applicationdatum = applicationdatumArrayList.get(position);
        sharedpref();
        applicationdatum = applicationdatumArrayList.get(position);
        setdata(holder);

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
        public CardView company_info_applied_card_mainview;
        public ImageView company_logo;
        public MKLoader image_loader_total;
        public TextView card_company_name, card_job_post, card_job_salary, card_job_location, approve_reject_check;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.company_info_applied_card_mainview = itemView.findViewById(R.id.company_info_apply_card_mainview_total);
            this.card_company_name = itemView.findViewById(R.id.card_company_name_total);
            this.card_job_post = itemView.findViewById(R.id.card_job_post_total);
            this.card_job_salary = itemView.findViewById(R.id.card_salary_total);
            this.card_job_location = itemView.findViewById(R.id.card_job_location_total);
            this.approve_reject_check = itemView.findViewById(R.id.approved_rejected_tv);
            company_logo = itemView.findViewById(R.id.company_logo_image);
            image_loader_total = itemView.findViewById(R.id.image_loader_total);
            image_loader_total.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("ResourceAsColor")
    public void setdata(ViewHolder holder) {
        try {
            holder.card_company_name.setText(applicationdatum.getCompanyName());
            holder.card_job_post.setText(applicationdatum.getJobPost());
            holder.card_job_salary.setText(applicationdatum.getJobSalary());
            String area = applicationdatum.getCompanyArea();
            String location = applicationdatum.getCompanyLocation();
            String company_logo_string = applicationdatum.getCompanyLogo();
            Picasso.get().load(company_logo_string).into(holder.company_logo);
            holder.image_loader_total.setVisibility(View.INVISIBLE);
//            new DownloadImageTaskImageView(holder.company_logo, holder.image_loader_total).execute(company_logo_string);
            String main_loc = area + ", " + location;
            holder.card_job_location.setText(main_loc);
            if (applicationdatum.getApplicationResponseId().equals("0")) {
                holder.approve_reject_check.setText("Rejected");
                holder.approve_reject_check.setBackground(context.getResources().getDrawable(
                        R.drawable.rejected_bg));
                holder.approve_reject_check.setTextColor(Color.WHITE);
            }

        } catch (Exception e) {
            Log.e("ERROR", applicationdatum.getCompanyName());
        }

    }

    @Override
    public int getItemCount() {
        return applicationdatumArrayList.size();
    }

}
