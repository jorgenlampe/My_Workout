package com.example.myworkout.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.Toast;

import com.example.myworkout.R;
import com.example.myworkout.adapters.GalleryAdapter;
import com.example.myworkout.adapters.UserProgramAdapter;
import com.example.myworkout.data.DataViewModel;
import com.example.myworkout.entities.UserProgram;
import com.example.myworkout.helpers.ApiError;
import com.example.myworkout.helpers.ApiResponse;
import com.example.myworkout.helpers.GlideApp;

import java.util.ArrayList;

/**
 * Fragment hvor bruker kan velge bilde som skal brukes
 */
public class ImagesFragment extends Fragment {


    private Observer<ApiResponse> apiResponseObserver = null;
    private Observer<ApiError> apiErrorObserver = null;
    private Observer<String> selectedImageObserver = null;

    private DataViewModel dataViewModel;

    private RecyclerView recyclerView;
    private GalleryAdapter mAdapter;

    private String userId;
    final private int numberOfColumns = 3;

    public ImagesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_images, container, false);

        dataViewModel = new ViewModelProvider(getActivity()).get(DataViewModel.class);
        recyclerView = view.findViewById(R.id.image_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));
        subscribeToApiResponse();
        subscribeToErrors();
        //subscribeToIconSelector();

        return view;
    }
    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        getActivity().setTitle(R.string.titleGallery);
        dataViewModel.getImageUrls(getContext());
        userId = ImagesFragmentArgs.fromBundle(getArguments()).getUserid();
    }

    public void subscribeToApiResponse() {
        if (apiResponseObserver == null) {
            // Observerer endringer:
            apiResponseObserver = new Observer<ApiResponse>() {
                @Override
                public void onChanged(ApiResponse apiResponse) {
                    if (getViewLifecycleOwner().getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
                        Toast.makeText(getActivity(), apiResponse.getMessage() + ": " + String.valueOf(apiResponse.getHttpStatusCode()) + " (" + ")", Toast.LENGTH_SHORT).show();
                        final ArrayList<String> urls = (ArrayList) apiResponse.getResponseObject();
                        if(urls == null) return;
                        if (urls.size() > 0) {
                            // Dersom response på GET, PUT, POST:
                            mAdapter = new GalleryAdapter(urls);
                            recyclerView.setAdapter(mAdapter);
                            //bruker onitemclicklistener interface laget i adapter..
                            mAdapter.setOnItemClickListener(new GalleryAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    ImagesFragmentDirections.ActionImagesFragmentToAddExerciseFragment actionImagesFragmentToAddExerciseFragment
                                            = ImagesFragmentDirections.actionImagesFragmentToAddExerciseFragment(userId, urls.get(position));
                                    NavHostFragment.findNavController(ImagesFragment.this).navigate(actionImagesFragmentToAddExerciseFragment);

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
