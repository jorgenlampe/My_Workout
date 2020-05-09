package com.example.myworkout;

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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditUserFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private DataViewModel dataViewModel;

    private Observer<ApiResponse> apiResponseObserver = null;
    private Observer<ApiError> apiErrorObserver = null;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditUserFragment() {
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);



        EditText etUsername = view.findViewById(R.id.etEditUsername);
        EditText etPhoneNumber = view.findViewById(R.id.etEditPhoneNumber);
        EditText etEmail = view.findViewById(R.id.etEditEmail);
        EditText etBirthYear = view.findViewById(R.id.etEditBirthYear);

        final String username = etUsername.getText().toString();
        final String phoneNumber = etPhoneNumber.getText().toString();
        final String email = etEmail.getText().toString();
      //  final int birthYear = Integer.parseInt(etBirthYear.getText().toString());

        subscribeToApiResponse();
        subscribeToErrors();

        Button btnUpdateUser = view.findViewById(R.id.btnUpdateUser);
        btnUpdateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                String firebaseId = null;

                if (firebaseUser!=null)
                    firebaseId = firebaseUser.getUid();

                dataViewModel.putUser(getContext(), firebaseId, username, phoneNumber, email, 0);  //todo birthyear
            }
        });


    }

    private void subscribeToApiResponse() {

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
                        if(getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
                            if (apiError != null)
                                Toast.makeText(getActivity(), apiError.getMessage() + ": " + String.valueOf(apiError.getCode()), Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                dataViewModel.getApiError().observe(getViewLifecycleOwner(), apiErrorObserver);
            }
        }



        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment editUserFragment.
         */
    // TODO: Rename and change types and number of parameters
    public static EditUserFragment newInstance(String param1, String param2) {
        EditUserFragment fragment = new EditUserFragment();
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
        return inflater.inflate(R.layout.fragment_edit_user, container, false);
    }
}
