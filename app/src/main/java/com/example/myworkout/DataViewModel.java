package com.example.myworkout;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class DataViewModel extends AndroidViewModel {

    private DataRepository mRepository;
    private MutableLiveData<ApiError> mErrorMessage;

    private MutableLiveData<ApiError> mApiError;
    private MutableLiveData<ApiResponse> mApiResponse;

    public DataViewModel(Application application) {
        super(application);
        mRepository = new DataRepository(application);
        mErrorMessage = mRepository.getErrorMessage();



    }

    public MutableLiveData<ApiError> getErrorMessage() {
        return mErrorMessage;
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
        mRepository.getUser(context, firebase_id, true);         //true eller false her????

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
        mRepository.putUser(context, firebase_id, name, phone, email,birth_year);
    }

    // DELETE user
    public void deleteUser(Context context, String firebase_id)
    {
        mRepository.deleteUser(context, firebase_id);
    }

}
