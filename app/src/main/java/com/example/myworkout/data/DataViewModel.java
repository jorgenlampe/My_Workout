package com.example.myworkout.data;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;

import com.example.myworkout.data.DataRepository;
import com.example.myworkout.helpers.ApiError;
import com.example.myworkout.helpers.ApiResponse;
import com.google.firebase.auth.FirebaseUser;

public class DataViewModel extends AndroidViewModel {

    private DataRepository mRepository;
    private MutableLiveData<ApiError> mApiError;
    private MutableLiveData<ApiResponse> mApiResponse;


    public DataViewModel(Application application) {
            super(application); // <====
            mRepository = new DataRepository(application);
            mApiError = mRepository.getErrorMessage();
            mApiResponse = mRepository.getApiResponse();
        }


    public MutableLiveData<ApiError> getApiError() {
        return mApiError;
    }

    public MutableLiveData<ApiResponse> getApiResponse() {
        return mApiResponse;
    }

    // GET user
    public void getUser(Context context,
                        String firebase_id, boolean forceDownload) {
        mRepository.getUser(context, firebase_id, forceDownload);

    }

    // POST user
    public void postUser(Context context,
                         String firebase_id,
                         String name,
                         String phone,
                         String email,
                         int birth_year) {
        mRepository.postUser(context, firebase_id, name, phone, email,birth_year);
    }

    // PUT user
    public void putUser(Context context,
                        String firebase_id,
                        String name,
                        String phone,
                        String email,
                        int birth_year)
    {
        mRepository.putUser(context, firebase_id, name, phone, email, birth_year);
    }

    // DELETE user
    public void deleteUser(Context context, String firebase_id, FirebaseUser firebaseUser)
    {
        mRepository.deleteUser(context, firebase_id, firebaseUser);
    }

    //GET ProgramTypes
    public void getProgramTypes(Context context, boolean forceDownload) {
        mRepository.getProgramTypes(context, forceDownload);
    }
}
