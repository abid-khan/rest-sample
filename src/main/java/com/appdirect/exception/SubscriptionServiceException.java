package com.appdirect.exception;

/**
 * Created by abidk on 03/09/16.
 */
public class SubscriptionServiceException extends  RuntimeException {

    /**
     *
     */

    public SubscriptionServiceException(){
        super();
    }


    /**
     *
     * @param message
     */
    public SubscriptionServiceException(String message) {
        super(message);
    }


    /**
     *
     * @param message
     * @param cause
     */
    public SubscriptionServiceException(String message, Throwable cause) {
        super(message, cause);
    }

}
