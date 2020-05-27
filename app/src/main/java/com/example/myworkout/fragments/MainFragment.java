package com.example.myworkout.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myworkout.R;
import com.example.myworkout.adapters.ExerciseAdapter;
import com.example.myworkout.adapters.UserProgramAdapter;
import com.example.myworkout.adapters.UserProgramSessionAdapter;
import com.example.myworkout.data.DataViewModel;
import com.example.myworkout.entities.Exercise;
import com.example.myworkout.entities.UserProgramSession;
import com.example.myworkout.entities.UserStats;
import com.example.myworkout.helpers.ApiError;
import com.example.myworkout.helpers.ApiResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.internal.$Gson$Preconditions;

import java.util.ArrayList;
import java.util.Map;


public class MainFragment extends Fragment {
    private Observer<ApiResponse> apiResponseObserver = null;
    private Observer<ApiError> apiErrorObserver = null;

    private Button btnToUserPrograms;
    private Button btnToSessions;
    private Button btnAddNewSession;
    private Button btnToProgramTypes;
    private Button btnAddProgramType;

    private TextView tvLastWeek;
    private TextView tvCurrentWeek;
    private TextView tvCurrentMonth;
    private TextView tvLastMonth;
    private TextView tvCurrentYear;

    private DataViewModel dataViewModel;


    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);

        getActivity().setTitle(R.string.titleMain);

        btnAddNewSession = view.findViewById(R.id.btnAddSession);
        btnToSessions = view.findViewById(R.id.btnToSessions);
        btnToUserPrograms = view.findViewById(R.id.btnToUserPrograms);
        btnToProgramTypes = view.findViewById(R.id.btnToProgramTypes);
        btnAddProgramType = view.findViewById(R.id.btnAddProgramType);

        tvLastWeek = view.findViewById(R.id.tvLastWeek);
        tvCurrentMonth = view.findViewById(R.id.tvCurrentMonth);
        tvCurrentWeek = view.findViewById(R.id.tvCurrentWeek);
        tvLastMonth = view.findViewById(R.id.tvLastMonth);
        tvCurrentYear = view.findViewById(R.id.tvCurrentYear);


        subscribeToApiResponse();
        subscribeToErrors();

        //Oppdaterer observer ved backpress
        if(apiResponseObserver != null) {
            dataViewModel.getApiResponse().observe(getViewLifecycleOwner(), apiResponseObserver);
        }
        return view;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        String firebaseId = null;

        if (firebaseUser!=null) {
            firebaseId = firebaseUser.getUid();
        }

        dataViewModel.getUserStats(getContext(), firebaseId);



      btnToUserPrograms.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {


            NavHostFragment.findNavController(MainFragment.this).navigate(MainFragmentDirections.actionMainFragmentToUserProgramsFragment());
          }
      });

        btnToSessions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NavHostFragment.findNavController(MainFragment.this).navigate(MainFragmentDirections.actionMainFragmentToSessionsFragment());
            }
        });


        btnAddNewSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(MainFragment.this).navigate(MainFragmentDirections.actionMainFragmentToAddSessionFragment());
            }
        });

        btnToProgramTypes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(MainFragment.this).navigate(MainFragmentDirections.actionMainFragmentToProgramTypesFragment());
            }
        });

        btnAddProgramType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(MainFragment.this).navigate(MainFragmentDirections.actionMainFragmentToAddProgramType());
            }
        });

    }


    public void subscribeToApiResponse() {
        if (apiResponseObserver == null) {
            // Observerer endringer:
            apiResponseObserver = new Observer<ApiResponse>() {

                @Override
                public void onChanged(ApiResponse apiResponse) {
                    if (getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
                        Toast.makeText(getActivity(), apiResponse.getMessage() + ": " + String.valueOf(apiResponse.getHttpStatusCode()) + " (" + ")", Toast.LENGTH_SHORT).show();
                        UserStats stats = (UserStats) apiResponse.getResponseObject();

                        tvCurrentWeek.setText("This week: " + stats.getCurrentWeek());
                        tvLastWeek.setText("Last week: " + stats.getLast7Days());
                        tvCurrentMonth.setText("This month: " + stats.getCurrentMonth());
                        tvLastMonth.setText("Last month: " + stats.getLast30days());
                        tvCurrentYear.setText("This year: " + stats.getCurrentYear());

                    }

                }
            };

            dataViewModel.getApiResponse().observe(getViewLifecycleOwner(), apiResponseObserver);

        }

    }



    private void subscribeToErrors() {

        if (apiErrorObserver == null) {
            // Observerer endringer i errorMessage:
            apiErrorObserver = new Observer<ApiError>() {
                @Override
                public void onChanged(ApiError apiError) {
                    if (getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
                        if (apiError != null)
                            Toast.makeText(getActivity(), apiError.getMessage() + ": " + String.valueOf(apiError.getCode()), Toast.LENGTH_SHORT).show();
                    }
                }
            };
            dataViewModel.getApiError().observe(getViewLifecycleOwner(), apiErrorObserver);
        }
    }


}
