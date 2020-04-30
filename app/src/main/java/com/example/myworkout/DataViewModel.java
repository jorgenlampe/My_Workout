package com.example.myworkout;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class DataViewModel extends AndroidViewModel {

    private DataRepository mRepository;
    private MutableLiveData<ApiError> mErrorMessage;
    private MutableLiveData<GetUserResponse> mGetUserResponse;
    private MutableLiveData<PostUserResponse> mPostUserResponse;
    private MutableLiveData<PutUserResponse> mPutUserResponse;
    private MutableLiveData<DeleteUserResponse> mDeleteUserResponse;

    public DataViewModel(Application application) {
        super(application);
        mRepository = new DataRepository(application);
        mErrorMessage = mRepository.getErrorMessage();
        mGetUserResponse = mRepository.getGetUserResponse();
        mPostUserResponse = mRepository.getPostUserResponse();
        mPutUserResponse = mRepository.getPutUserResponse();
        mDeleteUserResponse = mRepository.getDeleteUserResponse();
    }

    public MutableLiveData<ApiError> getErrorMessage() {
        return mErrorMessage;
    }

    public MutableLiveData<GetUserResponse> getGetUserResponse() {
        return mGetUserResponse;
    }

    public MutableLiveData<PostUserResponse> getPostUserResponse() {
        return mPostUserResponse;
    }

    public MutableLiveData<PutUserResponse> getPutUserResponse() {
        return mPutUserResponse;
    }

    public MutableLiveData<DeleteUserResponse> getDeleteUserResponse() {
        return mDeleteUserResponse;
    }

    // GET user
    public void getUser(Context context,
                        String firebase_id) {
        mRepository.getUser(context, firebase_id);
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
