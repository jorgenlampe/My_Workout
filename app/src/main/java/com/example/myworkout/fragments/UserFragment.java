package com.example.myworkout.fragments;

import android.media.tv.TvContentRating;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

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


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private DataViewModel dataViewModel;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Observer<ApiResponse> apiResponseObserver = null;
    private Observer<ApiError> apiErrorObserver = null;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
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
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);


        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String firebaseId = null;

        if (firebaseUser != null)
            firebaseId = firebaseUser.getUid();

        dataViewModel.getUser(getActivity(), firebaseId, false);

        subscribeToApiResponse();
        subscribeToErrors();

        TextView tvAccountName = view.findViewById(R.id.tvAccountName);

        if (firebaseUser != null)
            tvAccountName.setText(firebaseUser.getDisplayName());


        Button btnLogout = view.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        Button btnDeleteAccount = view.findViewById(R.id.btnDeleteAccount);
        btnDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUserFromServer();
            }
        });


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
                    if (getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
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

        }

    }

    private void subscribeToApiResponse() {

        final TextView tvUserName = getView().findViewById(R.id.tvUserNameText);
        final TextView tvPhoneNumber = getView().findViewById(R.id.tvPhoneNumberText);
        final TextView tvEmail = getView().findViewById(R.id.tvEmailText);
        final TextView tvBirthYear = getView().findViewById(R.id.tvBirthYearText);

        if (apiResponseObserver == null) {
            // Observerer endringer:
            apiResponseObserver = new Observer<ApiResponse>() {
                @Override
                public void onChanged(ApiResponse apiResponse) {
                    if (getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
                        Toast.makeText(getActivity(), apiResponse.getMessage() + ": " + String.valueOf(apiResponse.getHttpStatusCode()) + " (" + ")", Toast.LENGTH_SHORT).show();
                        User user = apiResponse.getUser();
                        if (user != null) {
                            // Dersom response på GET, PUT, POST:


                            tvUserName.setText(user.getName());
                            tvPhoneNumber.setText(user.getPhone());
                            tvEmail.setText(user.getEmail());
//                                tvBirthYear.setText(user.getBirth_year());    //noe feil her....
                        } else {

                            tvUserName.setText("...");


                            // Dersom response på DELETE
                            //                         signOut();
                            //                       tvUserInfo.setText("");
                            //                     etName.setText("");
                            //                   etEmail.setText("");
                            //                 etPhone.setText("");
                            //               etName.setEnabled(false);
                            //             etEmail.setEnabled(false);
                            //           etPhone.setEnabled(false);
                        }
                    }
                }
            };
            dataViewModel.getApiResponse().observe(getViewLifecycleOwner(), apiResponseObserver);
        }


    }
}
