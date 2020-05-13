package com.example.myworkout.helpers;

public class ApiResponse<T> {
    private boolean result;
    private String message;
    private int httpStatusCode;
    private T responseObject;

    public ApiResponse() {
        result=true;
        message="";
        responseObject = null;
        httpStatusCode = -1;
    }

    public ApiResponse  (boolean result, String message, T type, int httpStatusCode) {
        this.result = result;
        this.message = message;
        this.responseObject = type;
        this.httpStatusCode = httpStatusCode;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResponseObject() {
        return responseObject;
    }

    public void setResponseObject(T responseObject) {
        this.responseObject = responseObject;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }
}
