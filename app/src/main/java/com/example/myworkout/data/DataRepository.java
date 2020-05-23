package com.example.myworkout.data;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.myworkout.entities.Exercise;
import com.example.myworkout.entities.ProgramType;
import com.example.myworkout.entities.User;
import com.example.myworkout.entities.UserProgram;
import com.example.myworkout.entities.UserProgramExercise;
import com.example.myworkout.entities.UserProgramSession;
import com.example.myworkout.entities.UserStats;
import com.example.myworkout.helpers.ApiError;
import com.example.myworkout.helpers.ApiResponse;
import com.example.myworkout.helpers.MyJsonArrayRequest;
import com.example.myworkout.helpers.MyJsonObjectRequest;
import com.example.myworkout.helpers.VolleyErrorParser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

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

    //MyJsonArrayRequest arver fra JsonArrayRequest slik at vi kan overstyre parseNetworkResponse()
    private MyJsonArrayRequest myJsonArrayGetRequest;

    private User currentUser = null;      // Holder på sist nedlastede User-objekt.
    private boolean downloading = false;

    private RequestQueue queue = null;

    public MutableLiveData<ApiError> getErrorMessage() {
        return errorMessage;
    }

    public MutableLiveData<ApiResponse> getApiResponse() {
        return apiResponse;
    }


    public DataRepository(Application application) {
    }

    /*
     * Henter ALLE ProgramTypes
     * */
    public void getProgramTypes(Context context, boolean forceDownload) {
        // Dersom nedlasting pågår og skjermen roteres vil downloading være true, ingen grunn til å starte nedlasting på nytt:
        if (!this.downloading) {
            String url = PROGRAMTYPE_PREFIX + "?_api_key=" + API_KEY;
            queue = MySingletonQueue.getInstance(context).getRequestQueue();
            downloading = true;
            myJsonArrayGetRequest = new MyJsonArrayRequest(
                    Request.Method.GET,
                    url,
                    null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray jsonArray) {
                            try {
                                Gson gson = new Gson();
                                ArrayList<ProgramType> tmpList = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject programTypeAsJson = jsonArray.getJSONObject(i);
                                    ProgramType programType = gson.fromJson(programTypeAsJson.toString(), ProgramType.class);
                                    System.out.println(programType.getDescription());
                                    tmpList.add(programType);
                                }
                                ApiResponse resp = new ApiResponse(true, "OK", tmpList, myJsonArrayGetRequest.getHttpStatusCode());
                                apiResponse.setValue(resp);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            ApiError apiError = VolleyErrorParser.parse(error);
                            errorMessage.postValue(apiError);

                        }
                    });
            queue.add(myJsonArrayGetRequest);
        }
        downloading = false;
    }


    public void postProgramType() {

    }

    public void putProgramType() {

    }

    public void deleteProgramType() {

    }

    public void getExercises(Context context) {

        String url = EXERCISES_PREFIX + "?_api_key=" + API_KEY;

        if (!this.downloading) {

            queue = MySingletonQueue.getInstance(context).getRequestQueue();
            downloading = true;
            myJsonArrayGetRequest = new MyJsonArrayRequest(
                    Request.Method.GET,
                    url,
                    null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray jsonArray) {
                            try {
                                Gson gson = new Gson();
                                ArrayList<Exercise> tmpList = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject exerciseAsJson = jsonArray.getJSONObject(i);
                                    Exercise exercise = gson.fromJson(exerciseAsJson.toString(), Exercise.class);
                                    System.out.println(exercise.getDescription());
                                    tmpList.add(exercise);
                                }
                                ApiResponse resp = new ApiResponse(true, "OK", tmpList, myJsonArrayGetRequest.getHttpStatusCode());
                                apiResponse.setValue(resp);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            ApiError apiError = VolleyErrorParser.parse(error);
                            errorMessage.postValue(apiError);

                        }
                    });
            queue.add(myJsonArrayGetRequest);
        }
        downloading = false;
    }


    public void postExercise(Context context, String name, String description, String icon, String infobox_color) {


        final HashMap<String, String> params = new HashMap<String, String>();
        params.put("_api_key", API_KEY);
        params.put("name", name);
        params.put("description", description);
        params.put("icon", icon);
        params.put("infobox_color", infobox_color);

        queue = MySingletonQueue.getInstance(context).getRequestQueue();

        myJsonPostRequest = new MyJsonObjectRequest(
                Request.Method.POST,
                EXERCISES_PREFIX,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        try {
                            String message = response.getString("message");
                            JSONObject exerciseAsJsonObject = response.getJSONObject("record");
                            Exercise exercise = gson.fromJson(exerciseAsJsonObject.toString(), Exercise.class);
                            ApiResponse resp = new ApiResponse(true, message, exercise, myJsonPostRequest.getHttpStatusCode());
                            apiResponse.postValue(resp);
                        } catch (JSONException e) {
                            ApiError apiError = new ApiError(-1, e.getMessage());
                            errorMessage.postValue(apiError);
                        }
                    }
                },
                new Response.ErrorListener() {
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

    public void putExercise(Context context, String rid, String name, String description, String icon, String infobox_color) {
        final HashMap<String, String> params = new HashMap<String, String>();
        params.put("_api_key", API_KEY);
        params.put("rid", rid);
        params.put("name", name);
        params.put("description", description);
        params.put("icon", icon);
        params.put("infobox_color", infobox_color);

        queue = MySingletonQueue.getInstance(context).getRequestQueue();
        myJsonPutRequest = new MyJsonObjectRequest(
                Request.Method.PUT,
                EXERCISES_PREFIX,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        try {
                            String message = response.getString("message");
                            JSONObject exerciseAsJsonObject = response.getJSONObject("record");
                            Exercise currentExercise = gson.fromJson(exerciseAsJsonObject.toString(), Exercise.class);
                            ApiResponse resp = new ApiResponse(true, message, currentExercise, myJsonPutRequest.getHttpStatusCode());
                            apiResponse.postValue(resp);
                        } catch (JSONException e) {
                            ApiError apiError = new ApiError(-1, e.getMessage());
                            errorMessage.postValue(apiError);
                        }
                    }
                },
                new Response.ErrorListener() {
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

    public void deleteExercise(String rid, Context context) {
        String url = EXERCISES_PREFIX + rid + "?_api_key=" + API_KEY;

        queue = MySingletonQueue.getInstance(context).getRequestQueue();

        myJsonDeleteRequest = new MyJsonObjectRequest(
                Request.Method.DELETE,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        ApiResponse resp = new ApiResponse(true, "OK", null, myJsonDeleteRequest.getHttpStatusCode());
                        apiResponse.postValue(resp);
                    }
                },
                new Response.ErrorListener() {
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
                                currentUser = gson.fromJson(jsonObject.toString(), User.class);
                                ApiResponse resp = new ApiResponse(true, "OK", currentUser, myJsonGetRequest.getHttpStatusCode());
                                apiResponse.postValue(resp);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                ApiError apiError = VolleyErrorParser.parse(error);
                                errorMessage.setValue(apiError);
                            }
                        });
                queue.add(myJsonGetRequest);

            } else {
                ApiResponse resp = new ApiResponse(true, "OK, bruker cached User", this.currentUser, myJsonGetRequest.getHttpStatusCode());
                apiResponse.setValue(resp);
            }
        }
        downloading = false;
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
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        try {
                            String message = response.getString("message");
                            JSONObject userAsJsonObject = response.getJSONObject("record");
                            User user = gson.fromJson(userAsJsonObject.toString(), User.class);
                            ApiResponse resp = new ApiResponse(true, message, user, myJsonPostRequest.getHttpStatusCode());
                            apiResponse.postValue(resp);
                        } catch (JSONException e) {
                            ApiError apiError = new ApiError(-1, e.getMessage());
                            errorMessage.postValue(apiError);
                        }
                    }
                },
                new Response.ErrorListener() {
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
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        try {
                            String message = response.getString("message");
                            JSONObject userAsJsonObject = response.getJSONObject("record");
                            currentUser = gson.fromJson(userAsJsonObject.toString(), User.class);
                            ApiResponse resp = new ApiResponse(true, message, currentUser, myJsonPutRequest.getHttpStatusCode());
                            apiResponse.postValue(resp);
                        } catch (JSONException e) {
                            ApiError apiError = new ApiError(-1, e.getMessage());
                            errorMessage.postValue(apiError);
                        }
                    }
                },
                new Response.ErrorListener() {
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


    public void deleteUser(final Context context,
                           String firebase_id, FirebaseUser firebaseUser) {


        firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Firebase user deleted", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        ApiResponse resp = new ApiResponse(true, "OK", null, myJsonDeleteRequest.getHttpStatusCode());
                        apiResponse.postValue(resp);
                    }
                },
                new Response.ErrorListener() {
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

    public void getUserProgram(Context context, String rid) {

        if (!this.downloading) {
            String url = USER_PROGRAM_PREFIX + rid + "?_api_key=" + API_KEY;
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
                            UserProgram userProgram = gson.fromJson(jsonObject.toString(), UserProgram.class);
                            ApiResponse resp = new ApiResponse(true, "OK", userProgram, myJsonGetRequest.getHttpStatusCode());
                            apiResponse.postValue(resp);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            ApiError apiError = VolleyErrorParser.parse(error);
                            errorMessage.setValue(apiError);
                        }
                    });
            queue.add(myJsonGetRequest);
        }
        downloading = false;
    }

    public void getUserPrograms(Context context, String firebaseId) {
        if (!this.downloading) {
            String url = USERS_PREFIX + firebaseId + "?_api_key=" + API_KEY + "&_expand_children=true";
            queue = MySingletonQueue.getInstance(context).getRequestQueue();
            downloading = true;
            myJsonGetRequest = new MyJsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            try {
                                Gson gson = new Gson();
                                ArrayList<UserProgram> tmpList = new ArrayList<>();
                                JSONArray jsonUserPrograms = jsonObject.getJSONArray("user_programs");
                                for (int i = 0; i < jsonUserPrograms.length(); i++) {
                                    JSONObject userProgramAsJson = jsonUserPrograms.getJSONObject(i);
                                    UserProgram userProgram = gson.fromJson(userProgramAsJson.toString(), UserProgram.class);
                                    tmpList.add(userProgram);
                                }
                                ApiResponse resp = new ApiResponse(true, "OK", tmpList, myJsonGetRequest.getHttpStatusCode());
                                apiResponse.postValue(resp);
                                System.out.println("tmplist: " + tmpList.size());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            ApiError apiError = VolleyErrorParser.parse(error);
                            errorMessage.postValue(apiError);
                        }
                    });
            queue.add(myJsonGetRequest);
        }
        downloading = false;
    }


    public void postUserProgram(Context context,
                                String app_program_type_id,
                                String name,
                                String description,
                                boolean use_timing,
                                int id) {
        final HashMap<String, String> params = new HashMap<String, String>();
        params.put("_api_key", API_KEY);
        params.put("app_program_type_id", app_program_type_id);
        params.put("user_id", String.valueOf(id));
        params.put("name", name);
        params.put("description", description);
        if (use_timing) {
            params.put("use_timing", "1");
        } else {
            params.put("use_timing", "0");
        }

        queue = MySingletonQueue.getInstance(context).getRequestQueue();

        myJsonPostRequest = new MyJsonObjectRequest(
                Request.Method.POST,
                USER_PROGRAM_PREFIX,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        try {
                            String message = response.getString("message");
                            JSONObject userAsJsonObject = response.getJSONObject("record");
                            UserProgram userProgram = gson.fromJson(userAsJsonObject.toString(), UserProgram.class);
                            ApiResponse resp = new ApiResponse(true, message, userProgram, myJsonPostRequest.getHttpStatusCode());
                            apiResponse.postValue(resp);
                        } catch (JSONException e) {
                            ApiError apiError = new ApiError(-1, e.getMessage());
                            errorMessage.postValue(apiError);
                        }
                    }
                },
                new Response.ErrorListener() {
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

    public void putUserProgram(Context context, String rid, String user_id, String app_program_type_id, String name, String description, boolean use_timing) {
        final HashMap<String, String> params = new HashMap<String, String>();
        params.put("_api_key", API_KEY);
        params.put("rid", rid);
        params.put("user_id", user_id);
        params.put("app_program_type_id", app_program_type_id);
        params.put("name", name);
        params.put("description", description);
        if (use_timing) {
            params.put("use_timing", "1");
        } else {
            params.put("use_timing", "0");
        }

        // Generell PUT:
//            private void put(final Context context, String mUrlString, final HashMap params) {
        queue = MySingletonQueue.getInstance(context).getRequestQueue();
        myJsonPutRequest = new MyJsonObjectRequest(
                Request.Method.PUT,
                USER_PROGRAM_PREFIX,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        try {
                            String message = response.getString("message");
                            JSONObject userProgramAsJsonObject = response.getJSONObject("record");
                            UserProgram userProgram = gson.fromJson(userProgramAsJsonObject.toString(), UserProgram.class);
                            ApiResponse resp = new ApiResponse(true, message, userProgram, myJsonPutRequest.getHttpStatusCode());
                            apiResponse.postValue(resp);
                        } catch (JSONException e) {
                            ApiError apiError = new ApiError(-1, e.getMessage());
                            errorMessage.postValue(apiError);
                        }
                    }
                },
                new Response.ErrorListener() {
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

    public void deleteUserProgram(String rid, Context context) {
        String url = USER_PROGRAM_PREFIX + rid + "?_api_key=" + API_KEY;

        queue = MySingletonQueue.getInstance(context).getRequestQueue();

        myJsonDeleteRequest = new MyJsonObjectRequest(
                Request.Method.DELETE,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        ApiResponse resp = new ApiResponse(true, "OK", null, myJsonDeleteRequest.getHttpStatusCode());
                        apiResponse.postValue(resp);
                    }
                },
                new Response.ErrorListener() {
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

    public void getUserProgramExercises(Context context, String rid) {

        String url = USER_PROGRAM_PREFIX + rid + "?_api_key=" + API_KEY + "&_expand_children=true";
        System.out.println(url);
        if (!this.downloading) {
            queue = MySingletonQueue.getInstance(context).getRequestQueue();
            downloading = true;
            myJsonGetRequest = new MyJsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            try {
                                Gson gson = new Gson();
                                ArrayList<Exercise> tmpList = new ArrayList<>();
                                JSONArray jsonExercises = jsonObject.getJSONArray("user_program_exercises");
                                for (int i = 0; i < jsonExercises.length(); i++) {
                                    JSONObject userProgramExerciseAsJson = jsonExercises.getJSONObject(i);
                                    JSONObject exerciseAsJson = userProgramExerciseAsJson.getJSONObject("app_exercise");
                                    Exercise exercise = gson.fromJson(exerciseAsJson.toString(), Exercise.class);
                                    tmpList.add(exercise);
                                }
                                ApiResponse resp = new ApiResponse(true, "OK", tmpList, myJsonGetRequest.getHttpStatusCode());
                                apiResponse.postValue(resp);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            ApiError apiError = VolleyErrorParser.parse(error);
                            errorMessage.setValue(apiError);
                        }
                    });
            queue.add(myJsonGetRequest);

            }
        downloading = false;
    }

    //Brukes ikke..SLETTE? TODO
    public void deleteUserProgramExercise(Context context, String rid) {
        String url = USER_PROGRAM_EXERCISES_PREFIX + rid + "?_api_key=" + API_KEY;

        queue = MySingletonQueue.getInstance(context).getRequestQueue();

        myJsonDeleteRequest = new MyJsonObjectRequest(
                Request.Method.DELETE,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        ApiResponse resp = new ApiResponse(true, "OK", null, myJsonDeleteRequest.getHttpStatusCode());
                        apiResponse.postValue(resp);
                    }
                },
                new Response.ErrorListener() {
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


    public void getExercise(Context context, String rid) {

        String url = EXERCISES_PREFIX + rid + "?_api_key=" + API_KEY;
        System.out.println(url);
        if (!this.downloading) {
            queue = MySingletonQueue.getInstance(context).getRequestQueue();
            downloading = true;
            myJsonGetRequest = new MyJsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            try {
                                Gson gson = new Gson();
                                Exercise exercise = gson.fromJson(jsonObject.toString(), Exercise.class);
                                ApiResponse resp = new ApiResponse(true, "OK", exercise, myJsonGetRequest.getHttpStatusCode());
                                apiResponse.postValue(resp);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            ApiError apiError = VolleyErrorParser.parse(error);
                            errorMessage.setValue(apiError);
                        }
                    });
            queue.add(myJsonGetRequest);

        }

        downloading = false;


    }


    public void postUserProgramExercise(Context context, String user_program_id, String app_exercise_id) {

        final HashMap<String, String> params = new HashMap<String, String>();
        params.put("_api_key", API_KEY);
        params.put("user_program_id", user_program_id);
        params.put("app_exercise_id", app_exercise_id);

        queue = MySingletonQueue.getInstance(context).getRequestQueue();

        myJsonPostRequest = new MyJsonObjectRequest(
                Request.Method.POST,
                USER_PROGRAM_EXERCISES_PREFIX,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        try {
                            String message = response.getString("message");
                            JSONObject userProgramExerciseAsJsonObject = response.getJSONObject("record");
                            UserProgramExercise exercise = gson.fromJson(userProgramExerciseAsJsonObject.toString(), UserProgramExercise.class);
                            ApiResponse resp = new ApiResponse(true, message, exercise, myJsonPostRequest.getHttpStatusCode());
                            apiResponse.postValue(resp);
                        } catch (JSONException e) {
                            ApiError apiError = new ApiError(-1, e.getMessage());
                            errorMessage.postValue(apiError);
                        }
                    }
                },
                new Response.ErrorListener() {
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

    public void putUserProgramExercise() {
    }

    public void deleteUserProgramExercise() {
    }

    public void getSessions(Context context, String firebaseId) {

        if (!this.downloading) {
            String url = USERS_PREFIX + firebaseId + "?_api_key=" + API_KEY + "&_expand_children=true";

            queue = MySingletonQueue.getInstance(context).getRequestQueue();
            downloading = true;
            myJsonGetRequest = new MyJsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {

                            try {
                                Gson gson = new Gson();
                                ArrayList<UserProgramSession> tmpList = new ArrayList<>();
                                JSONArray jsonUserPrograms = jsonObject.getJSONArray("user_programs");

                                    for (int a = 0; a < jsonUserPrograms.length(); a++) {
                                        JSONObject userProgramAsJson = jsonUserPrograms.getJSONObject(a);
                                        UserProgram program = gson.fromJson(userProgramAsJson.toString(), UserProgram.class);
                                        ArrayList<UserProgramSession> sessionsTemp =
                                                (ArrayList<UserProgramSession>) program.getUser_program_sessions();

                                        for (int i = 0; i < sessionsTemp.size(); i++) {
                                            tmpList.add(sessionsTemp.get(i));
                                        }


                                    }

                                ApiResponse resp = new ApiResponse(true, "OK", tmpList, myJsonGetRequest.getHttpStatusCode());
                                apiResponse.postValue(resp);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            ApiError apiError = VolleyErrorParser.parse(error);
                            errorMessage.postValue(apiError);
                        }
                    });
            queue.add(myJsonGetRequest);
        }
        downloading = false;




    }

    public void postUserProgramSession(Context context, String user_program_id, String date, float time_spent, String description, String extra_json_data) {


        final HashMap<String, String> params = new HashMap<String, String>();
        params.put("_api_key", API_KEY);
        params.put("user_program_id", user_program_id);
        params.put("date", date);
        params.put("time_spent", String.valueOf(time_spent));
        params.put("description", description);
        params.put("extra_json_data", extra_json_data);

        queue = MySingletonQueue.getInstance(context).getRequestQueue();

        myJsonPostRequest = new MyJsonObjectRequest(
                Request.Method.POST,
                USER_PROGRAM_SESSION_PREFIX,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        try {
                            String message = response.getString("message");
                            JSONObject sessionAsJsonObject = response.getJSONObject("record");
                            UserProgramSession session = gson.fromJson(sessionAsJsonObject.toString(), UserProgramSession.class);
                            ApiResponse resp = new ApiResponse(true, message, session, myJsonPostRequest.getHttpStatusCode());
                            apiResponse.postValue(resp);
                        } catch (JSONException e) {
                            ApiError apiError = new ApiError(-1, e.getMessage());
                            errorMessage.postValue(apiError);
                        }
                    }
                },
                new Response.ErrorListener() {
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

    public void putUserProgramSession() {
    }

    public void deleteUserProgramSession() {
    }

    public void getUserStats(Context context, String firebaseId) {

        String url = USER_STATS_PREFIX + firebaseId + "?_api_key=" + API_KEY + "&_expand_children=true";

        if (!this.downloading) {
            queue = MySingletonQueue.getInstance(context).getRequestQueue();
            downloading = true;
            myJsonGetRequest = new MyJsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            try {
                                Gson gson = new Gson();
                                UserStats stats = gson.fromJson(jsonObject.toString(), UserStats.class);
                                ApiResponse resp = new ApiResponse(true, "OK", stats, myJsonGetRequest.getHttpStatusCode());
                                apiResponse.postValue(resp);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            ApiError apiError = VolleyErrorParser.parse(error);
                            errorMessage.setValue(apiError);
                        }
                    });
            queue.add(myJsonGetRequest);

        }
        downloading = false;



    }
}


