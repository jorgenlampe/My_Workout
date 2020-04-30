package com.example.myworkout;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
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

    private MutableLiveData<GetUserResponse> getUserResponse = new MutableLiveData<>();
    private MutableLiveData<PostUserResponse> postUserResponse = new MutableLiveData<>();
    private MutableLiveData<PutUserResponse> putUserResponse = new MutableLiveData<>();
    private MutableLiveData<DeleteUserResponse> deleteUserResponse = new MutableLiveData<>();

    private MutableLiveData<ApiError> errorMessage = new MutableLiveData<>();


    private RequestQueue queue = null;

    public MutableLiveData<GetUserResponse> getGetUserResponse() {
        return getUserResponse;
    }
    public MutableLiveData<PostUserResponse> getPostUserResponse() {
        return postUserResponse;
    }
    public MutableLiveData<PutUserResponse> getPutUserResponse() {
        return putUserResponse;
    }
    public MutableLiveData<DeleteUserResponse> getDeleteUserResponse() {
        return deleteUserResponse;
    }

    public MutableLiveData<ApiError> getErrorMessage() {
        return errorMessage;
    }


    public DataRepository(Application application) {
    }


    // endre navn til get, post, put...

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

        public void getUser(Context context, final String firebaseId) {
                String url = USERS_PREFIX + firebaseId + "?_api_key=" + API_KEY;

                queue = MySingletonQueue.getInstance(context).getRequestQueue();
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                        Request.Method.GET,
                        url,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                Gson gson = new Gson();
                                User userObject = gson.fromJson(jsonObject.toString(), User.class);
                                GetUserResponse resp = new GetUserResponse(true, "OK", userObject);
                                getUserResponse.postValue(resp);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                ApiError apiError = VolleyErrorParser.parse(error);
                                errorMessage.postValue(apiError);
                            }
                        }) { };
                queue.add(jsonObjectRequest);


        }



        public void postUser(Context context,
                             String firebase_id,
                             String name,
                             String phone,
                             String email,
                             int birth_year) {

            final HashMap<String, String> params = new HashMap<String, String>();
            params.put("_api_key", API_KEY);
            params.put("firebase_id", firebase_id);
            params.put("name", name);
            params.put("phone", phone);
            params.put("email", email);
            params.put("birth_year", String.valueOf(birth_year));
            //post(context, mUrlString, params);

            queue = MySingletonQueue.getInstance(context).getRequestQueue();

            JsonObjectRequest postRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    USERS_PREFIX,
                    null,
                    new Response.Listener<JSONObject>()
                    {
                        @Override
                        public void onResponse(JSONObject response) {
                            Gson gson = new Gson();
                            try {
                                String message = response.getString("message");
                                JSONObject userAsJsonObject = response.getJSONObject("record");
                                User user = gson.fromJson(userAsJsonObject.toString(), User.class);
                                PostUserResponse resp = new PostUserResponse(true, message, user);
                                postUserResponse.postValue(resp);
                            } catch (JSONException e) {
                                ApiError apiError = new ApiError(-1, e.getMessage());
                                errorMessage.postValue(apiError);
                            }
                        }
                    },
                    new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            ApiError apiError = VolleyErrorParser.parse(error);
                            errorMessage.postValue(apiError);
                        }
                    }
            ) {
                @Override
                public byte[] getBody() {
                    return new JSONObject(params).toString().getBytes();
                }

                @Override
                public String getBodyContentType() {
                    return "application/json";
                }
            };
            queue.add(postRequest);
        }


        public void putUser(Context context,
                            String firebase_id,
                            String name,
                            String phone,
                            String email,
                            int birth_year) {


                final HashMap<String, String> params = new HashMap<String, String>();
                params.put("_api_key", API_KEY);
                params.put("firebase_id", firebase_id);
                params.put("name", name);
                params.put("phone", phone);
                params.put("email", email);
                params.put("birth_year", String.valueOf(birth_year));
            //    put(context, mUrlString, params);


            // Generell PUT:
//            private void put(final Context context, String mUrlString, final HashMap params) {
                queue = MySingletonQueue.getInstance(context).getRequestQueue();
                JsonObjectRequest putRequest = new JsonObjectRequest(
                        Request.Method.PUT,
                        USERS_PREFIX,
                        null,
                        new Response.Listener<JSONObject>()
                        {
                            @Override
                            public void onResponse(JSONObject response) {
                                Gson gson = new Gson();
                                try {
                                    String message = response.getString("message");
                                    JSONObject userAsJsonObject = response.getJSONObject("record");
                                    User user = gson.fromJson(userAsJsonObject.toString(), User.class);
                                    PutUserResponse resp = new PutUserResponse(true, message, user);
                                    putUserResponse.postValue(resp);
                                } catch (JSONException e) {
                                    ApiError apiError = new ApiError(-1, e.getMessage());
                                    errorMessage.postValue(apiError);
                                }
                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                ApiError apiError = VolleyErrorParser.parse(error);
                                errorMessage.postValue(apiError);
                            }
                        }
                ) {
                    @Override
                    public byte[] getBody() {
                        return new JSONObject(params).toString().getBytes();
                    }

                    @Override
                    public String getBodyContentType() {
                        return "application/json";
                    }
                };
                queue.add(putRequest);
            }


        public void deleteUser(Context context,
                    String firebase_id) {
                //String mUrlString = BASE_URL_ENDPOINTS + "users/" + firebase_id + "?_api_key=" + API_KEY;
              //  delete(context, mUrlString);

            // Generell DELETE:
            //private void delete(final Context context, String mUrlString) {
                queue = MySingletonQueue.getInstance(context).getRequestQueue();

                JsonObjectRequest deleteRequest = new JsonObjectRequest(
                        Request.Method.DELETE,
                        USERS_PREFIX,
                        null,
                        new Response.Listener<JSONObject>()
                        {
                            @Override
                            public void onResponse(JSONObject response) {
                                DeleteUserResponse resp = new DeleteUserResponse(true, "OK");
                                deleteUserResponse.postValue(resp);
                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                ApiError apiError = VolleyErrorParser.parse(error);
                                errorMessage.postValue(apiError);
                            }
                        }
                ) {
                    @Override
                    public String getBodyContentType() {
                        return "application/json";
                    }
                };
                queue.add(deleteRequest);
            }

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