package com.appdirect.exception;

/**
 * Created by abidk on 04/09/16.
 */
public class SubscriptionNotPresentServiceException extends  SubscriptionServiceException {

    /**
     *
     */

    public SubscriptionNotPresentServiceException(){
        super();
    }


    /**
     *
     * @param message
     */
    public SubscriptionNotPresentServiceException(String message) {
        super(message);
    }


    /**
     *
     * @param message
     * @param cause
     */
    public SubscriptionNotPresentServiceException(String message, Throwable cause) {
        super(message, cause);
    }

}
