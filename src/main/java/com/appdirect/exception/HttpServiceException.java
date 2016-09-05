package com.appdirect.exception;

/**
 * Created by abidk on 04/09/16.
 */
public class HttpServiceException extends   RuntimeException {
    /**
     *
     */

    public HttpServiceException(){
        super();
    }


    /**
     *
     * @param message
     */
    public HttpServiceException(String message) {
        super(message);
    }


    /**
     *
     * @param message
     * @param cause
     */
    public HttpServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
