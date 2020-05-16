package com.example.myworkout.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.myworkout.data.DataViewModel;
import com.example.myworkout.R;
import com.example.myworkout.entities.User;
import com.example.myworkout.helpers.ApiError;
import com.example.myworkout.helpers.ApiResponse;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class LoginFragment extends Fragment {
    private static final int RC_SIGN_IN = 123;
    private Button btnLogin;
    private Button btnLogout;
    private TextView tvStatus;
    private DataViewModel dataViewModel;
    //private int count=0;

    private Observer<ApiResponse> apiResponseObserver = null;
    private Observer<ApiError> apiErrorObserver = null;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null)
            updateUI(currentUser.getEmail());
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


            dataViewModel.getApiError().observe(getViewLifecycleOwner(), apiErrorObserver);  //feil her....?
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // Bruk cachet verdi på user, hvis den eksisterer. Hvis ikke last ned.
        getUserFromServer(false);   //<==
    }

    // GET: Last ned brukerinfo fra server:
    private void getUserFromServer(boolean forceDownload) {
        //    count = 0;
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            String firebaseId = firebaseUser.getUid();
            dataViewModel.getUser(getActivity(), firebaseId, forceDownload);
        } else {
            Toast.makeText(getActivity(), "Du er ikke logget inn.", Toast.LENGTH_SHORT).show();
        }

    }

    private void subscribeToApiResponse() {
        if (apiResponseObserver == null) {
            // Observerer endringer:
            apiResponseObserver = new Observer<ApiResponse>() {
                @Override
                public void onChanged(ApiResponse apiResponse) {
                    if (getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
                        Toast.makeText(getActivity(), apiResponse.getMessage() + ": " + String.valueOf(apiResponse.getHttpStatusCode()) + " (" + ")", Toast.LENGTH_SHORT).show();
                        User user = (User) apiResponse.getResponseObject();
                        if (user != null) {
                            // Dersom response på GET, PUT, POST:
                            //            tvUserInfo.setText(user.toString());
                            //          etName.setText(user.getName());
                            //        etEmail.setText(user.getEmail());
                            //      etPhone.setText(user.getPhone());
                            //    etName.setEnabled(true);
                            //  etEmail.setEnabled(true);
                            //etPhone.setEnabled(true);
                        } else {
                            // Dersom response på DELETE
                            signOut();
                            //   tvUserInfo.setText("");
                            // etName.setText("");
                            // etEmail.setText("");
                            // etPhone.setText("");
                            //etName.setEnabled(false);
                            //etEmail.setEnabled(false);
                            //  etPhone.setEnabled(false);
                        }
                    }
                }
            };
            dataViewModel.getApiResponse().observe(getViewLifecycleOwner(), apiResponseObserver);
        }
    }


    // Enkel metode for å vise innloggings-status:
    private void updateUI(String status) {
        tvStatus.setText(status);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        btnLogin = view.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createSignInIntent();

            }
        });
        btnLogout = view.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

        /*
        * Skrur av/på login og logg ut knappene, og markerer de grå dersom de ikke er aktivert, og oppdaterer UI
         */
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            updateUIOnSignIn();
        } else {
            updateUIOnSignOut();
        }

        tvStatus = view.findViewById(R.id.tvStatus);

        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);

        subscribeToErrors();
        subscribeToApiResponse();

        return view;
    }

    // FIREBASE SIGN IN & OUT:
    public void createSignInIntent() {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());
        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                updateUIOnSignIn();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                updateUI(user.getEmail());
                String firebaseId = user.getUid();
                String username = user.getDisplayName();
                String email = user.getEmail();
                String phone = user.getPhoneNumber();
                dataViewModel.postUser(getContext(), firebaseId, username, phone, email, 0);

            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                updateUI("Innlogging feilet... Prøv på nytt.");
            }
        }
    }

    public void signOut() {
        AuthUI.getInstance()
                .signOut(getActivity())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI("Logget ut");
                    }
                });
        updateUIOnSignOut();
    }

    public void updateUIOnSignOut() {
        btnLogout.setClickable(false);
        btnLogout.setAlpha(0.5f);
        btnLogin.setClickable(true);
        btnLogin.setAlpha(1f);
    }

    public void updateUIOnSignIn() {
        btnLogout.setClickable(true);
        btnLogout.setAlpha(1f);
        btnLogin.setClickable(false);
        btnLogin.setAlpha(0.5f);
    }
}