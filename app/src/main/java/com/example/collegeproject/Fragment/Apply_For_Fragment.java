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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.collegeproject.Adapter.Company_Info_Card_Apply_For_Adapter;
import com.example.collegeproject.Database.APIInterface;
import com.example.collegeproject.Database.AppClient;
import com.example.collegeproject.Database.ApplicationData;
import com.example.collegeproject.Database.Applicationdatum;
import com.example.collegeproject.R;
import com.tuyenmonkey.mkloader.MKLoader;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Apply_For_Fragment extends Fragment {

    ArrayList<Applicationdatum> applicationdatumArrayList, applicationdatumArrayList_jobtype;
    RecyclerView recyclerView;
    public static MKLoader mkLoader;
    String user_id;
    SharedPreferences sharedPreferences;
    Fragment fragment;
    TextView null_job_get;
    List<String> job_types = new ArrayList<String>();
    String job_type;
    public static final String mypreference = "mypref";
    Spinner show_job_type;
    LinearLayout job_type_frame;
    CoordinatorLayout main_layout_swipe;
    public static final String USER_ID = "user_id";
    int setSelectionSpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_apply__for_, container, false);
        initializeUI(view);
        sharedpref();
        load_view_job_type_fn();
        setSpinner();
        show_job_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mkLoader.setVisibility(View.VISIBLE);
                job_type = job_types.get(i);
                if (job_type.equals("All")) {
                    load_details_apply_for_all();
                    update_job_view_type("All");
                } else if (job_type.equals("Android")) {
                    update_job_view_type("Android");
                    display_job_by_type("Android");
                } else if (job_type.equals("PHP")) {
                    update_job_view_type("PHP");
                    display_job_by_type("PHP");
                } else if (job_type.equals("Python")) {
                    update_job_view_type("Python");
                    display_job_by_type("Python");
                } else if (job_type.equals("Software Testing")) {
                    update_job_view_type("Software Testing");
                    display_job_by_type("Software Testing");
                } else if (job_type.equals("Software Development")) {
                    update_job_view_type("Software Development");
                    display_job_by_type("Software Development");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return view;
    }

    public void initializeUI(View view) {
        show_job_type = view.findViewById(R.id.show_jobs_type);
        mkLoader = view.findViewById(R.id.apply_for_loader);
        applicationdatumArrayList = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.company_info_apply_for_recycler);
        null_job_get = view.findViewById(R.id.on_null_job_set);
        job_type_frame = view.findViewById(R.id.job_type_frame);
        main_layout_swipe = view.findViewById(R.id.main_layout_swipe);
    }

    public void setSpinner() {
        job_types.add("All");
        job_types.add("Android");
        job_types.add("PHP");
        job_types.add("Python");
        job_types.add("Software Testing");
        job_types.add("Software Development");

        ArrayAdapter job = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, job_types);
        job.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        show_job_type.setAdapter(job);
    }

    public void load_details_apply_for_all() {
        APIInterface apiInterface = AppClient.getclient().create(APIInterface.class);
        Call<ApplicationData> jobDataCall = apiInterface.job_show(user_id);
        jobDataCall.enqueue(new Callback<ApplicationData>() {
            @Override
            public void onResponse(Call<ApplicationData> call, Response<ApplicationData> response) {
                try {
                    mkLoader.setVisibility(View.INVISIBLE);
                    applicationdatumArrayList = (ArrayList<Applicationdatum>) response.body().getApplicationdata();
                    int resId = R.anim.layout_recycle_animation;
                    /*LayoutAnimationController layoutAnimationController = AnimationUtils.loadLayoutAnimation(getActivity(), resId);
                    recyclerView.setLayoutAnimation(layoutAnimationController);*/
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    //linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setHasFixedSize(true);
                    Company_Info_Card_Apply_For_Adapter companyInfoCardApplyForAdapter = new Company_Info_Card_Apply_For_Adapter(getActivity(), applicationdatumArrayList, mkLoader);
                    recyclerView.setAdapter(companyInfoCardApplyForAdapter);
                    fragment = new Apply_For_Fragment();
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApplicationData> call, Throwable t) {
                mkLoader.setVisibility(View.INVISIBLE);
                Log.e("ERROR", String.valueOf(t));
            }
        });
    }

    public void load_view_job_type_fn() {
        APIInterface apiInterface = AppClient.getclient().create(APIInterface.class);
        Call<ApplicationData> applicationDataCall = apiInterface.load_view_job_type(user_id);
        applicationDataCall.enqueue(new Callback<ApplicationData>() {
            @Override
            public void onResponse(Call<ApplicationData> call, Response<ApplicationData> response) {
                try {
                    applicationdatumArrayList_jobtype = (ArrayList<Applicationdatum>) response.body().getApplicationdata();
                    job_type = applicationdatumArrayList_jobtype.get(0).getUserViewJobType();
                    if (job_type.equals("All")) {
                        setSelectionSpinner = 0;
                        load_details_apply_for_all();
                    } else if (job_type.equals("Android")) {
                        setSelectionSpinner = 1;
                    } else if (job_type.equals("PHP")) {
                        setSelectionSpinner = 2;
                        display_job_by_type(job_type);
                    } else if (job_type.equals("Python")) {
                        setSelectionSpinner = 3;
                        display_job_by_type(job_type);
                    } else if (job_type.equals("Software Testing")) {
                        setSelectionSpinner = 4;
                        display_job_by_type(job_type);
                    } else if (job_type.equals("Software Development")) {
                        setSelectionSpinner = 5;
                        display_job_by_type(job_type);
                    }
                    show_job_type.setSelection(setSelectionSpinner);
                } catch (Exception e) {

                }

            }

            @Override
            public void onFailure(Call<ApplicationData> call, Throwable t) {

            }
        });
    }

    public void update_job_view_type(String job_type_fn) {
        APIInterface apiInterface = AppClient.getclient().create(APIInterface.class);
        Call<ApplicationData> applicationDataCall = apiInterface.update_view_job_type(user_id, job_type_fn);
        applicationDataCall.enqueue(new Callback<ApplicationData>() {
            @Override
            public void onResponse(Call<ApplicationData> call, Response<ApplicationData> response) {
                applicationdatumArrayList_jobtype = (ArrayList<Applicationdatum>) response.body().getApplicationdata();
            }

            @Override
            public void onFailure(Call<ApplicationData> call, Throwable t) {

            }
        });
    }

    public void display_job_by_type(String job_type_temp) {
        APIInterface apiInterface = AppClient.getclient().create(APIInterface.class);
        Call<ApplicationData> applicationDataCall = apiInterface.display_job_details_by_filter(user_id, job_type_temp);
        applicationDataCall.enqueue(new Callback<ApplicationData>() {
            @Override
            public void onResponse(Call<ApplicationData> call, Response<ApplicationData> response) {
                try {
                    mkLoader.setVisibility(View.INVISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                    null_job_get.setVisibility(View.INVISIBLE);
                    applicationdatumArrayList = (ArrayList<Applicationdatum>) response.body().getApplicationdata();
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    //linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    int resId = R.anim.layout_recycle_animation;
                    /*LayoutAnimationController layoutAnimationController = AnimationUtils.loadLayoutAnimation(getActivity(), resId);
                    recyclerView.setLayoutAnimation(layoutAnimationController);*/
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setHasFixedSize(true);
                    Company_Info_Card_Apply_For_Adapter companyInfoCardApplyForAdapter = new Company_Info_Card_Apply_For_Adapter(getActivity(), applicationdatumArrayList, mkLoader);
                    recyclerView.setAdapter(companyInfoCardApplyForAdapter);
                } catch (Exception e) {
                    null_job_get.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ApplicationData> call, Throwable t) {

            }
        });
    }

    public void sharedpref() {
        sharedPreferences = getContext().getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString(USER_ID, "");
    }

}