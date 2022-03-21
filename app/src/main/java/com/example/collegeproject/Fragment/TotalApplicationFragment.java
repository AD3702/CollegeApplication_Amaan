package com.example.collegeproject.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.collegeproject.Adapter.Total_Jobs_Adapter;
import com.example.collegeproject.Database.APIInterface;
import com.example.collegeproject.Database.AppClient;
import com.example.collegeproject.Database.ApplicationData;
import com.example.collegeproject.Database.Applicationdatum;
import com.example.collegeproject.R;
import com.tuyenmonkey.mkloader.MKLoader;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TotalApplicationFragment extends Fragment {

    Toolbar toolbar_total_jobs;
    String user_id;
    TextView textView_on_null_job_response;
    SharedPreferences sharedPreferences;
    public static final String mypreference = "mypref";
    public static final String USER_ID = "user_id";
    RecyclerView recyclerView_total_jobs;
    ArrayList<Applicationdatum> applicationdatumArrayList;
    MKLoader mkLoader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_total_application, container, false);
        mkLoader = view.findViewById(R.id.total_jobs_loader);
        mkLoader.setVisibility(View.INVISIBLE);
        recyclerView_total_jobs = view.findViewById(R.id.total_jobs_recycle);
        textView_on_null_job_response = view.findViewById(R.id.on_null_job_response_text);
        toolbar_total_jobs = view.findViewById(R.id.toolbar_actionbar_total_jobs);
        toolbar_total_jobs.setBackground(new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.main_color)));
        toolbar_total_jobs.setTitleTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        toolbar_total_jobs.setTitle("Total Jobs");
        sharedPreferences = getContext().getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString(USER_ID, "");
        load_total_jobs();
        return view;
    }

    public void load_total_jobs() {
        mkLoader.setVisibility(View.VISIBLE);
        APIInterface apiInterface = AppClient.getclient().create(APIInterface.class);
        Call<ApplicationData> applicationDataCall = apiInterface.total_applied_job_show(user_id);
        applicationDataCall.enqueue(new Callback<ApplicationData>() {
            @Override
            public void onResponse(Call<ApplicationData> call, Response<ApplicationData> response) {
                try {
                    mkLoader.setVisibility(View.INVISIBLE);
                    applicationdatumArrayList = (ArrayList<Applicationdatum>) response.body().getApplicationdata();
                    Total_Jobs_Adapter total_jobs_adapter = new Total_Jobs_Adapter(getActivity(), applicationdatumArrayList, mkLoader);
                    recyclerView_total_jobs.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView_total_jobs.setAdapter(total_jobs_adapter);
                    int resId = R.anim.layout_recycle_animation;
                    LayoutAnimationController layoutAnimationController = AnimationUtils.loadLayoutAnimation(getActivity(), resId);
                    recyclerView_total_jobs.setLayoutAnimation(layoutAnimationController);
                } catch (Exception e) {
                    mkLoader.setVisibility(View.VISIBLE);
                    textView_on_null_job_response.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Call<ApplicationData> call, Throwable t) {

            }
        });
    }

}