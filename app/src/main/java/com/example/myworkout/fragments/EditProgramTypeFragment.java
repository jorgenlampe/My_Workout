package com.example.myworkout.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myworkout.R;
import com.example.myworkout.data.DataViewModel;
import com.example.myworkout.entities.Exercise;
import com.example.myworkout.entities.ProgramType;
import com.example.myworkout.helpers.ApiError;
import com.example.myworkout.helpers.ApiResponse;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditProgramTypeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditProgramTypeFragment extends Fragment {

    //Fragment for å endre programtype

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    private DataViewModel dataViewModel;

    private Observer<ApiResponse> apiResponseObserver = null;
    private Observer<ApiError> apiErrorObserver = null;

    private EditText etDescription;
    private EditText etIcon;
    private EditText etBackColor;

    private Button btnEditProgramType;

    private String description;
    private String icon;
    private String backColor;

    private String rid;

    private boolean redirect = true;

    public EditProgramTypeFragment() {

    }

    public static EditProgramTypeFragment newInstance(String param1, String param2) {
        EditProgramTypeFragment fragment = new EditProgramTypeFragment();
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

        View view = inflater.inflate(R.layout.fragment_edit_program_type, container, false);

        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);

        etDescription = view.findViewById(R.id.etDescription);
        etIcon = view.findViewById(R.id.etIcon);
        etBackColor = view.findViewById(R.id.etBackColor);

        btnEditProgramType = view.findViewById(R.id.btnEditProgramType);

        subscribeToApiResponse();
        subscribeToErrors();


        return view;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {

        getActivity().setTitle(R.string.editProgramType);

        rid = EditProgramTypeFragmentArgs.fromBundle(getArguments()).getProgramTypeRid();
        dataViewModel.getProgramType(getContext(), rid);

        btnEditProgramType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                description = etDescription.getText().toString();
                icon = etIcon.getText().toString();
                backColor = etBackColor.getText().toString();

                //Sjekker at bruker har fyllt ut feltene
                if (description.isEmpty()) {
                    Toast.makeText(getContext(), "Fill out form before sending!", Toast.LENGTH_LONG).show();
                    return;
                }

                //Sjekker at farge er i riktig format
                if (!backColor.matches("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$")) {
                    Toast.makeText(getContext(), "Wrong color. Must be in HEX", Toast.LENGTH_LONG).show();
                    return;
                }
                dataViewModel.putProgramType(getContext(), rid, description, icon, backColor);
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

                        if (apiResponse.getResponseObject() instanceof ProgramType) {
                            if (redirect) {
                                ProgramType programType = (ProgramType) apiResponse.getResponseObject();
                                etDescription.setText(programType.getDescription());
                                etIcon.setText(programType.getIcon());
                                etBackColor.setText(programType.getBack_color());
                                redirect = false;
                            } else {
                                NavHostFragment.findNavController(EditProgramTypeFragment.this).navigateUp();
                            }

                        }

                    }

                }

                    };
                    dataViewModel.getApiResponse().observe(getViewLifecycleOwner(), apiResponseObserver);
                }
            };


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

}
