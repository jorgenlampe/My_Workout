package com.example.myworkout;


public class PutUserResponse {
    private boolean result;
    private String message;
    private User user;

    public PutUserResponse() {
        result=true;
        message="";
        user = null;
    }

    public PutUserResponse(boolean result, String message, User user) {
        this.result = result;
        this.message = message;
        this.user = user;
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
}
