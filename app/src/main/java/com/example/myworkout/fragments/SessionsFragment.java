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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myworkout.R;
import com.example.myworkout.adapters.UserProgramAdapter;
import com.example.myworkout.adapters.UserProgramSessionAdapter;
import com.example.myworkout.data.DataViewModel;
import com.example.myworkout.entities.UserProgram;
import com.example.myworkout.entities.UserProgramSession;
import com.example.myworkout.helpers.ApiError;
import com.example.myworkout.helpers.ApiResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SessionsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SessionsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Observer<ApiResponse> apiResponseObserver = null;
    private Observer<ApiError> apiErrorObserver = null;

    private DataViewModel dataViewModel;

    private RecyclerView recyclerView;
    private UserProgramSessionAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public SessionsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SessionsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SessionsFragment newInstance(String param1, String param2) {
        SessionsFragment fragment = new SessionsFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sessions, container, false);

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

        getActivity().setTitle(R.string.titleSessions);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        String firebaseId = null;

        if (firebaseUser != null) {
            firebaseId = firebaseUser.getUid();
        }

        dataViewModel.getSessions(getContext(), firebaseId);

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
                        final ArrayList<UserProgramSession> sessions = (ArrayList) apiResponse.getResponseObject();

                        if(sessions == null) return;
                        if (sessions.size() > 0) {
                            // Dersom response på GET, PUT, POST:


                            mAdapter = new UserProgramSessionAdapter(sessions);  //todo sende med programNavn
                            recyclerView.setAdapter(mAdapter);
                            //bruker onitemclicklistener interface laget i adapter..
                            mAdapter.setOnItemClickListener(new UserProgramSessionAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {


                                }

                                @Override
                                public void onDeleteClick(int position) {
                            //todo         dataViewModel.deleteUserProgramSession(sessions.get(position).getRid(), getContext());
                                    sessions.remove(position);
                                    mAdapter.notifyItemRemoved(position);
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
