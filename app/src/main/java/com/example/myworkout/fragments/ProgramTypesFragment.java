package com.example.myworkout.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.myworkout.R;
import com.example.myworkout.adapters.ProgramTypeAdapter;
import com.example.myworkout.adapters.UserProgramAdapter;
import com.example.myworkout.data.DataViewModel;
import com.example.myworkout.entities.ProgramType;
import com.example.myworkout.entities.UserProgram;
import com.example.myworkout.helpers.ApiError;
import com.example.myworkout.helpers.ApiResponse;

import java.util.ArrayList;


public class ProgramTypesFragment extends Fragment {

    //Fragment for å vise programtyper med bilder og bakgrunnsfarge i RecyclerViewen

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Observer<ApiResponse> apiResponseObserver = null;
    private Observer<ApiError> apiErrorObserver = null;

    private DataViewModel dataViewModel;

    private RecyclerView recyclerView;
    private ProgramTypeAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;


    private String mParam1;
    private String mParam2;

    public ProgramTypesFragment() {

    }


    public static ProgramTypesFragment newInstance(String param1, String param2) {
        ProgramTypesFragment fragment = new ProgramTypesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_program_types, container, false);


        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);

        recyclerView = view.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());//bedre ytelse med fast størrelse på layout
        recyclerView.setLayoutManager(layoutManager);

        subscribeToApiResponse();
        subscribeToErrors();

        //Oppdaterer observer ved backpress
        if (apiResponseObserver != null) {
            dataViewModel.getApiResponse().observe(getViewLifecycleOwner(), apiResponseObserver);
        }

        return view;
    }


    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {

        getActivity().setTitle(R.string.titleProgramTypes);

        dataViewModel.getProgramTypes(getContext(), false);

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
                        final ArrayList<ProgramType> programTypes = (ArrayList) apiResponse.getResponseObject();
                        if(programTypes == null) return;
                        if (programTypes.size() > 0) {
                            // Dersom response på GET, PUT, POST:
                            mAdapter = new ProgramTypeAdapter(programTypes);
                            recyclerView.setAdapter(mAdapter);

                            mAdapter.setOnItemClickListener(new ProgramTypeAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {

                                }

                                @Override
                                public void onDeleteClick(int position) {
                                    dataViewModel.deleteProgramType(getContext(), programTypes.get(position).getRid());
                                    programTypes.remove(position);
                                    mAdapter.notifyItemRemoved(position);
                                }

                                @Override
                                public void onEditClick(int position) {

                                    ProgramTypesFragmentDirections.ActionProgramTypesToEditProgramTypes actionProgramTypesToEditProgramTypes = ProgramTypesFragmentDirections.actionProgramTypesToEditProgramTypes(programTypes.get(position).getRid());
                                    NavHostFragment.findNavController(ProgramTypesFragment.this).navigate(actionProgramTypesToEditProgramTypes);
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
