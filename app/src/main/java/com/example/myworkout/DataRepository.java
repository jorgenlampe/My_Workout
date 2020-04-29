package com.example.myworkout;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.RequestQueue;

import java.util.List;

public class DataRepository {

    final String API_KEY = "82906135-8856-11ea-b";
    final String PROGRAMTYPE_PREFIX = "https://tusk.systems/trainingapp/v2/api.php/app_program_types/";
    final String EXERCISES_PREFIX = "https://tusk.systems/trainingapp/v2/api.php/app_exercises/";
    final String USERS_PREFIX = "https://tusk.systems/trainingapp/v2/api.php/users/";
    final String USER_PROGRAM_PREFIX = "https://tusk.systems/trainingapp/v2/api.php/user_programs/";
    final String USER_PROGRAM_EXERCISES_PREFIX = "https://tusk.systems/trainingapp/v2/api.php/user_program_exercises/";
    final String USER_PROGRAM_SESSION_PREFIX = "https://tusk.systems/trainingapp/v2/api.php/user_program_sessions/";
    final String USER_STATS_PREFIX = "https://tusk.systems/trainingapp/v2/api.php/user_stats/";

    private MutableLiveData<List<Exercise>> exercises = new MutableLiveData<>();
    private MutableLiveData<List<ProgramType>> programTypes = new MutableLiveData<>();
    private MutableLiveData<List<User>> users = new MutableLiveData<>();
    private MutableLiveData<List<UserProgram>> userPrograms = new MutableLiveData<>();
    private MutableLiveData<List<UserProgramExercise>> userProgramExercises = new MutableLiveData<>();
    private MutableLiveData<List<UserProgramSession>> userProgramSessions = new MutableLiveData<>();
    private RequestQueue queue;

    public MutableLiveData<List<Exercise>> getExercises() {
        return exercises;
    }

    public MutableLiveData<List<ProgramType>> getProgramTypes() {
        return programTypes;
    }

    public MutableLiveData<List<User>> getUsers() {
        return users;
    }

    public MutableLiveData<List<UserProgram>> getUserPrograms() {
        return userPrograms;
    }

    public MutableLiveData<List<UserProgramExercise>> getUserProgramExercises() {
        return userProgramExercises;
    }

    public MutableLiveData<List<UserProgramSession>> getUserProgramSessions() {
        return userProgramSessions;
    }




        public void downloadProgramTypes(){

        }

        public void addProgramType(){

        }

        public void editProgramType(){

        }

        public void deleteProgramType(){

        }

        public void downloadExercises(){}

        public void downloadExercises(String rid){}

        public void addExercise(){}

        public void editExercise(){}

        public void deleteExercise(){}

        public void downloadUsers(){}

        public void addUser(){}

        public void editUser(){}

        public void deleteUser(){}

        public void downloadUserPrograms(){}

        public void addUserProgram(){}

        public void editUserProgram(){}

        public void deleteUserProgram(){}

        public void downloadUserProgramExercises(){}

        public void addUserProgramExercise(){}

        public void editUserProgramExercise(){}

        public void deleteUserProgramExercise(){}

        public void downloadUserProgramSessions(){}

        public void addUserProgramSession(){}

        public void editUserProgramSession(){}

        public void deleteUserProgramSession(){}

        public void downloadUserStats(){}







}
