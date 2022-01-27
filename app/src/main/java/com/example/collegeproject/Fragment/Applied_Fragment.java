package com.example.collegeproject.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.collegeproject.Adapter.Company_Info_Card_Applied_Adapter;
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

public class Applied_Fragment extends Fragment {

    ArrayList<Applicationdatum> jobdatumArrayList;
    RecyclerView recyclerView;
    TextView textView_on_null_job_applied;
    MKLoader mkLoader;
    String user_id;
    SharedPreferences sharedPreferences;
    public static final String mypreference = "mypref";
    public static final String USER_ID = "user_id";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_applied_, container, false);
        initializeUI(view);
        sharedpref();
        load_details_applied();
        return view;
    }

    public void initializeUI(View view) {
        mkLoader = view.findViewById(R.id.applied_loader);
        textView_on_null_job_applied = view.findViewById(R.id.on_null_job_applied_text);
        jobdatumArrayList = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.company_info_applied_recycler);
    }

    public void load_details_applied() {
        APIInterface apiInterface = AppClient.getclient().create(APIInterface.class);
        Call<ApplicationData> jobDataCall = apiInterface.applied_user(user_id);
        jobDataCall.enqueue(new Callback<ApplicationData>() {
            @Override
            public void onResponse(Call<ApplicationData> call, Response<ApplicationData> response) {
                try {
                    mkLoader.setVisibility(View.INVISIBLE);
                    jobdatumArrayList = (ArrayList<Applicationdatum>) response.body().getApplicationdata();
                    Company_Info_Card_Applied_Adapter adapter = new Company_Info_Card_Applied_Adapter(getContext(), jobdatumArrayList, mkLoader);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    int resId = R.anim.layout_recycle_animation;
                    LayoutAnimationController layoutAnimationController = AnimationUtils.loadLayoutAnimation(getActivity(), resId);
                    recyclerView.setLayoutAnimation(layoutAnimationController);
                    recyclerView.setAdapter(adapter);
                } catch (Exception e) {
                    textView_on_null_job_applied.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Call<ApplicationData> call, Throwable t) {
                mkLoader.setVisibility(View.INVISIBLE);
                Log.e("ERROR", String.valueOf(t));
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void sharedpref() {
        sharedPreferences = getContext().getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString(USER_ID, "");
    }


}