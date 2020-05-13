package com.example.myworkout.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myworkout.data.DataViewModel;
import com.example.myworkout.entities.User;
import com.example.myworkout.helpers.ApiError;
import com.example.myworkout.helpers.ApiResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.example.myworkout.data.DataViewModel;
import com.example.myworkout.entities.User;
import com.example.myworkout.helpers.ApiError;
import com.example.myworkout.helpers.ApiResponse;

import com.example.myworkout.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class  RegisterFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Observer<ApiResponse> apiResponseObserver = null;
    private Observer<ApiError> apiErrorObserver = null;

    private EditText etUsername;
    private EditText etEmail;
    private EditText etPhoneNumber;
    private EditText etBirthYear;

    private String firebaseId;

    private Button btnRegister;

    private DataViewModel dataViewModel;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
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



    //onCreateVieww.... View view = inflater.inflate(R.layout.fragment_user, container, false); return view..

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = etUsername.getText().toString();
                String email = etEmail.getText().toString();
                String phoneNumber = etPhoneNumber.getText().toString();
                int birthYear = Integer.parseInt(etBirthYear.getText().toString());
                dataViewModel.postUser(getContext(), firebaseId, username, phoneNumber, email, birthYear);
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

    //TODO MÅ OPPDATERES
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

                            User newUser = new User(user.getFirebase_id(), user.getName(), user.getEmail(), user.getPhone(), user.getBirth_year());


                        } else {


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


    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState){
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_register, container, false);

        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        firebaseId = firebaseUser.getUid();

        etUsername = view.findViewById(R.id.etUsername);
        etEmail = view.findViewById(R.id.etEmail);
        etPhoneNumber = view.findViewById(R.id.etPhoneNumber);
        etBirthYear = view.findViewById(R.id.etBirthYear);


        btnRegister = view.findViewById(R.id.btnRegister);

        subscribeToApiResponse();
        subscribeToErrors();

        return view;
    }
}





