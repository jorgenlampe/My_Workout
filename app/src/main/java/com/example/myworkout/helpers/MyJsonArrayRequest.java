package com.example.myworkout.helpers;

import androidx.annotation.Nullable;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import org.json.JSONArray;

/**
 * MyJsonArrayRequest arver fra JsonArrayRequest slik at vi kan overstyre parseNetworkResponse() for å kunne hente ut HTTP responsekode.
 * Se bruk i DataRepository
 */
public class MyJsonArrayRequest extends JsonArrayRequest {
    private int httpStatusCode = -1;

    public MyJsonArrayRequest(int method, String url, @Nullable JSONArray jsonRequest, Response.Listener<JSONArray> listener, @Nullable Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }

    @Override
    protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
        httpStatusCode = response.statusCode;
        return super.parseNetworkResponse(response);
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }
}
