package com.appdirect.util;

import com.appdirect.dto.CancelSubscriptionResponse;
import com.appdirect.dto.CreateSubscriptionResponse;

/**
 * Created by abidk on 04/09/16.
 */
public class ResponseUtil {


    /**
     * @param success
     * @param accountIdentifier
     * @param errorCode
     * @param message
     * @return
     */
    public static CreateSubscriptionResponse buildResponse(String success, String accountIdentifier, String errorCode, String message) {
        return new CreateSubscriptionResponse(success, accountIdentifier, errorCode, message);
    }

    /**
     * @param success
     * @param errorCode
     * @param message
     * @return
     */
    public static CancelSubscriptionResponse buildResponse(String success, String errorCode, String message) {
        return new CancelSubscriptionResponse(success, errorCode, message);
    }
}
