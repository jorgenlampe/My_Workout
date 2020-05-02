package com.example.myworkout;

public class ApiResponse {
    private boolean result;
    private String message;
    private int httpStatusCode;
    private User user;

    public ApiResponse() {
        result=true;
        message="";
        user = null;
        httpStatusCode = -1;
    }

    public ApiResponse(boolean result, String message, User user, int httpStatusCode) {
        this.result = result;
        this.message = message;
        this.user = user;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }
}
