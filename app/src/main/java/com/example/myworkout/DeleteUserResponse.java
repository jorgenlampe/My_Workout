package com.example.myworkout;

public class DeleteUserResponse {
    private boolean result;
    private String message;

    public DeleteUserResponse() {
        result=true;
        message="";
    }

    public DeleteUserResponse(boolean result, String message) {
        this.result = result;
        this.message = message;
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
}
