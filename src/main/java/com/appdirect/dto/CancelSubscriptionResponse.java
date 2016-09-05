package com.appdirect.dto;

/**
 * Created by abidk on 04/09/16.
 */
public class CancelSubscriptionResponse {
    private String success;

    private String errorCode;

    private String message;

    public CancelSubscriptionResponse(String success, String errorCode,String message){
        this.success=success;
        this.errorCode= errorCode;
        this.message=message;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
