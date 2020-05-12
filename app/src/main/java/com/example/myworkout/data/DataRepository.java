package com.example.myworkout.data;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.myworkout.helpers.MyJsonObjectRequest;
import com.example.myworkout.entities.User;
import com.example.myworkout.helpers.ApiError;
import com.example.myworkout.helpers.ApiResponse;
import com.example.myworkout.helpers.VolleyErrorParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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



    private MutableLiveData<ApiError> errorMessage = new MutableLiveData<>();
    private MutableLiveData<ApiResponse> apiResponse = new MutableLiveData<>();

    // MyJsonObjectRequest arver fra JsonObjectRequest slik at vi kan overstyre parseNetworkResponse() for å kunne hente ut HTTP responsekode:
    private MyJsonObjectRequest myJsonGetRequest;
    private MyJsonObjectRequest myJsonPostRequest;
    private MyJsonObjectRequest myJsonPutRequest;
    private MyJsonObjectRequest myJsonDeleteRequest;



    private User currentUser=null;      // Holder på sist nedlastede User-objekt.
    private boolean downloading=false;

    private RequestQueue queue = null;

    public MutableLiveData<ApiError> getErrorMessage() {
        return errorMessage;
    }

    public MutableLiveData<ApiResponse> getApiResponse() {
        return apiResponse;
    }


    public DataRepository(Application application) {
    }


    // endre navn til get, post, put...

        public void getProgramTypes(){

        

        }

        public void postProgramType(){

        }

        public void putProgramType(){

        }

        public void deleteProgramType(){

        }

        public void getExercises(String rid){}

        public void postExercise(){}

        public void putExercise(){}

        public void deleteExercise(){}

        public void getUser(Context context, final String firebaseId, boolean forceDownload) {

            if (forceDownload || this.currentUser == null) {
                // Dersom nedlasting pågår og skjermen roteres vil downloading være true, ingen grunn til å starte nedlasting på nytt:
                if (!this.downloading) {
                    String url = USERS_PREFIX + firebaseId + "?_api_key=" + API_KEY;

                    queue = MySingletonQueue.getInstance(context).getRequestQueue();

                    downloading = true;
                     myJsonGetRequest = new MyJsonObjectRequest(
                            Request.Method.GET,
                            url,
                            null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject jsonObject) {
                                    Gson gson = new Gson();
                                    User userObject = gson.fromJson(jsonObject.toString(), User.class);
                                    ApiResponse resp = new ApiResponse(true, "OK", userObject, myJsonGetRequest.getHttpStatusCode());
                                    apiResponse.postValue(resp);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    ApiError apiError = VolleyErrorParser.parse(error);
                                    errorMessage.postValue(apiError);
                                }
                            }) {
                    };
                    queue.add(myJsonGetRequest);

                } else {
                    ApiResponse resp = new ApiResponse(true, "OK, bruker cached User", this.currentUser, myJsonGetRequest.getHttpStatusCode());
                    apiResponse.postValue(resp);
                }
            }

        }

        //METODENE UNDER MÅ ENDRES, SE OPPDATERT EKSEMPELKODE

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

            myJsonPostRequest = new MyJsonObjectRequest(
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
         //                       PostUserResponse resp = new PostUserResponse(true, message, user);
           //                     postUserResponse.postValue(resp);
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
            queue.add(myJsonPostRequest);
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
                myJsonPutRequest = new MyJsonObjectRequest(
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
             //                       PutUserResponse resp = new PutUserResponse(true, message, user);
               //                     putUserResponse.postValue(resp);
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
                queue.add(myJsonPutRequest);
            }


        public void deleteUser(Context context,
                    String firebase_id, FirebaseUser firebaseUser) {

        //TODO slette fra Firebase, ta en firebaseuser som parameter og kalle .delete();?

            firebaseUser.delete();

            StringBuilder builder = new StringBuilder();

            builder.append(USERS_PREFIX);
            builder.append(firebase_id);
            builder.append("?_api_key=");
            builder.append(API_KEY);

            String url = builder.toString();

                queue = MySingletonQueue.getInstance(context).getRequestQueue();

                myJsonDeleteRequest = new MyJsonObjectRequest(
                        Request.Method.DELETE,
                        url,
                        null,
                        new Response.Listener<JSONObject>()
                        {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                ApiResponse resp = new ApiResponse(true, "OK", null, myJsonDeleteRequest.getHttpStatusCode());
                                apiResponse.postValue(resp);
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
                queue.add(myJsonDeleteRequest);
            }

        public void getUserPrograms(){}

        public void postUserProgram(){}

        public void putUserProgram(){}

        public void deleteUserProgram(){}

        public void getUserProgramExercises(){}

        public void postUserProgramExercise(){}

        public void putUserProgramExercise(){}

        public void deleteUserProgramExercise(){}

        public void getUserProgramSessions(){}

        public void postUserProgramSession(){}

        public void putUserProgramSession(){}

        public void deleteUserProgramSession(){}

        public void getUserStats(){}







}
