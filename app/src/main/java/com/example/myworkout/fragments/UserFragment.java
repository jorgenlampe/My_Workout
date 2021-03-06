package com.example.myworkout.fragments;

import android.content.SharedPreferences;
import android.media.tv.TvContentRating;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myworkout.R;
import com.example.myworkout.data.DataViewModel;
import com.example.myworkout.entities.User;
import com.example.myworkout.helpers.ApiError;
import com.example.myworkout.helpers.ApiResponse;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class UserFragment extends Fragment {

    //Fragment for å vise brukerinfo. Herfra kan man logge ut, slette konto og endre brukerinfo

    private TextView tvUserName;
    private TextView tvPhoneNumber;
    private TextView tvEmail;
    private TextView tvBirthYear;
    private TextView tvAccountName;

    private Button btnEditUser;
    private Button btnDeleteAccount;
    private Button btnLogout;


    private DataViewModel dataViewModel;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Observer<ApiResponse> apiResponseObserver = null;
    private Observer<ApiError> apiErrorObserver = null;


    public UserFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user, container, false);

        getActivity().setTitle(R.string.titleUser);

        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);

        tvUserName = view.findViewById(R.id.tvUserNameText);
        tvPhoneNumber = view.findViewById(R.id.tvPhoneNumberText);
        tvEmail = view.findViewById(R.id.tvEmailText);
        tvBirthYear = view.findViewById(R.id.tvBirthYearText);
        tvAccountName = view.findViewById(R.id.tvAccountName);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        btnLogout = view.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        btnDeleteAccount = view.findViewById(R.id.btnDeleteAccount);
        btnDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUserFromServer();
            }
        });

        btnEditUser = view.findViewById(R.id.btnEditUser);
        btnEditUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getView()).navigate(R.id.action_userFragment_to_editUserFragment);
            }
        });

        if (firebaseUser!=null) {
            tvAccountName.setText(firebaseUser.getEmail());
            btnLogout.setAlpha(1f);
            btnDeleteAccount.setAlpha(1f);
            btnEditUser.setAlpha(1f);
        } else {
            btnEditUser.setClickable(false);
            btnEditUser.setAlpha(0.5f);
            btnDeleteAccount.setClickable(false);
            btnDeleteAccount.setAlpha(0.5f);
            btnLogout.setClickable(false);
            btnLogout.setAlpha(0.5f);
        }


        subscribeToApiResponse();
        subscribeToErrors();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        String firebaseId = null;

        if (firebaseUser!=null) {
            firebaseId = firebaseUser.getUid();
        }

        dataViewModel.getUser(getActivity(), firebaseId, false);


    }

    public void signOut() {
        AuthUI.getInstance()
                .signOut(getActivity())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        Navigation.findNavController(getView()).navigate(R.id.action_userFragment_to_navigation);
                    }
                });
    }

    private void subscribeToErrors() {

        if (apiErrorObserver == null) {
            // Observerer endringer i errorMessage:
            apiErrorObserver = new Observer<ApiError>() {
                @Override
                public void onChanged(ApiError apiError) {
                    if(getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
                        if (apiError != null)
                            Toast.makeText(getActivity(), apiError.getMessage() + ": " + String.valueOf(apiError.getCode()), Toast.LENGTH_SHORT).show();
                    }
                }
            };
            dataViewModel.getApiError().observe(getViewLifecycleOwner(), apiErrorObserver);
        }
    }

    public void deleteUserFromServer() {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            String firebaseId = firebaseUser.getUid();
            dataViewModel.deleteUser(getActivity(), firebaseId, firebaseUser);
            NavHostFragment.findNavController(UserFragment.this).navigate(R.id.navigation);
        }

    }

    private void subscribeToApiResponse() {
        if (apiResponseObserver == null) {
            // Observerer endringer:
            apiResponseObserver = new Observer<ApiResponse>() {
                @Override
                public void onChanged(ApiResponse apiResponse) {
                    if(getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
                        Toast.makeText(getActivity(), apiResponse.getMessage() + ": " + String.valueOf(apiResponse.getHttpStatusCode()) + " ("  + ")", Toast.LENGTH_SHORT).show();
                        User user = (User) apiResponse.getResponseObject();
                        if (user != null) {
                            // Dersom response på GET, PUT, POST:

                            TextView tvUserName = getView().findViewById(R.id.tvUserNameText);
                            TextView tvPhoneNumber = getView().findViewById(R.id.tvPhoneNumberText);
                            TextView tvEmail = getView().findViewById(R.id.tvEmailText);
                            TextView tvBirthYear = getView().findViewById(R.id.tvBirthYearText);

                            tvUserName.setText(user.getName());
                            tvEmail.setText(user.getEmail());
                            if(user.getBirth_year() == 2020) {
                                tvBirthYear.setText("");
                            } else {
                                tvBirthYear.setText(String.valueOf(user.getBirth_year()));
                            }
                            if(user.getPhone().equals("00000000")) {
                                tvPhoneNumber.setText("");
                            } else {
                                tvPhoneNumber.setText(user.getPhone());
                            }
                        }
                    }
                }
            };
            dataViewModel.getApiResponse().observe(getViewLifecycleOwner(), apiResponseObserver);
        }
    }

}