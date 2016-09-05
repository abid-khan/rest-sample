package com.appdirect.constant;

/**
 * Created by abidk on 05/09/16.
 * Defines custom error codes used in application
 */
public enum ErrorCodes {

    ERROR_400("Bad Request");

    private String name;

    private ErrorCodes(String name){
        this.name=name;
    }

    public String getName() {
        return name;
    }
}
