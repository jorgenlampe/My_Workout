package com.example.myworkout.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myworkout.R;
import com.example.myworkout.adapters.UserProgramAdapter;
import com.example.myworkout.data.DataViewModel;
import com.example.myworkout.entities.UserProgram;
import com.example.myworkout.helpers.ApiError;
import com.example.myworkout.helpers.ApiResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;


public class UserProgramsFragment extends Fragment {

    private Observer<ApiResponse> apiResponseObserver = null;
    private Observer<ApiError> apiErrorObserver = null;

    private Button btnAddUserProgram;

    private DataViewModel dataViewModel;

    private RecyclerView recyclerView;
    private UserProgramAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;


    public UserProgramsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_programs, container, false);

        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);

        recyclerView = view.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());//bedre ytelse med fast størrelse på layout
        recyclerView.setLayoutManager(layoutManager);

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

        getActivity().setTitle(R.string.titleUserPrograms);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        String firebaseId = null;

        if (firebaseUser != null) {
            firebaseId = firebaseUser.getUid();
            System.out.println(firebaseId);
        }

        dataViewModel.getUserPrograms(getContext(), firebaseId);

        btnAddUserProgram = getView().findViewById(R.id.btnAddUserProgram);

        btnAddUserProgram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_toAddUserFragment);
            }
        });

        layoutManager = new LinearLayoutManager(getContext());
    }



    public void subscribeToApiResponse() {
        if (apiResponseObserver == null) {
            // Observerer endringer:
            apiResponseObserver = new Observer<ApiResponse>() {
                @Override
                public void onChanged(ApiResponse apiResponse) {
                    if (getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
                        Toast.makeText(getActivity(), apiResponse.getMessage() + ": " + String.valueOf(apiResponse.getHttpStatusCode()) + " (" + ")", Toast.LENGTH_SHORT).show();
                        final ArrayList<UserProgram> programs = (ArrayList) apiResponse.getResponseObject();
                        if(programs == null) return;
                        if (programs.size() > 0) {
                            // Dersom response på GET, PUT, POST:
                            mAdapter = new UserProgramAdapter(programs);
                            recyclerView.setAdapter(mAdapter);
                            //bruker onitemclicklistener interface laget i adapter..
                            mAdapter.setOnItemClickListener(new UserProgramAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    UserProgramsFragmentDirections.ActionToUserProgramFragment actionToUserProgramFragment = UserProgramsFragmentDirections.actionToUserProgramFragment(programs.get(position).getRid(), programs.get(position).getId()); //
                                    NavHostFragment.findNavController(UserProgramsFragment.this).navigate(actionToUserProgramFragment);
                                }

                                @Override
                                public void onDeleteClick(int position) {
                                    dataViewModel.deleteUserProgram(programs.get(position).getRid(), getContext());
                                    programs.remove(position);
                                    mAdapter.notifyItemRemoved(position);
                                }

                                @Override
                                public void onEditClick(int position) {
                                    UserProgramsFragmentDirections.ActionToEditUserProgram actionToEditUserProgram = UserProgramsFragmentDirections.actionToEditUserProgram(programs.get(position).getRid());
                                    NavHostFragment.findNavController(UserProgramsFragment.this).navigate(actionToEditUserProgram);
                                }
                            });
                        }
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
