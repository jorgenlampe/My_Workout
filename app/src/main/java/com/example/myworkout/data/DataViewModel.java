package com.example.myworkout.data;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.myworkout.entities.User;
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
        mRepository.postUser(context, firebase_id, name, phone, email, birth_year);
    }

    // PUT user
    public void putUser(Context context,
                        String firebase_id,
                        String name,
                        String phone,
                        String email,
                        int birth_year) {
        mRepository.putUser(context, firebase_id, name, phone, email, birth_year);
    }

    // DELETE user
    public void deleteUser(Context context, String firebase_id, FirebaseUser firebaseUser) {
        mRepository.deleteUser(context, firebase_id, firebaseUser);
    }

    //GET ProgramTypes
    public void getProgramTypes(Context context, boolean forceDownload) {
        mRepository.getProgramTypes(context, forceDownload);
    }

    //GET UserPrograms
    public void getUserPrograms(Context context, String firebaseId) {
        mRepository.getUserPrograms(context, firebaseId);
    }

    public void getUserProgramExercise(Context context, String rid){
        mRepository.getUserProgramExercise(context, rid);
    }

    //POST UserPrograms
    public void postUserProgram(Context context,
                                String app_program_type_id,
                                String firebase_id,
                                String name,
                                String description,
                                boolean use_timing, int id) {
        mRepository.postUserProgram(context, app_program_type_id, name, description, use_timing, id);
    }

    //DELETE UserProgram
    public void deleteUserProgram(String rid, Context context) { mRepository.deleteUserProgram(rid,context);}
    public void getExercises(Context context) {
        mRepository.getExercises(context);
    }


    public void postExercise(Context context, String name, String description, String icon, String infoboxColor) {
        mRepository.postExercise(context, name, description, icon, infoboxColor);

    }

    public void postUserProgramExercise(Context context, String user_program_id, String app_exercise_id){
        mRepository.postUserProgramExercise(context, user_program_id, app_exercise_id);
    }

}
