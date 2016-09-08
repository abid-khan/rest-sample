package com.appdirect.util;

import oauth.signpost.http.HttpRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by abidk on 08/09/16.
 */
public class RequestValidatorUtil {

    public static boolean isValid(HttpServletRequest request, String consumerKey){
        String authorization = request.getHeader("Authorization");

        //Authorization header is missing
        if(CommonUtil.isEmpty(authorization)){
            return false;
        }

        String[] params = authorization.split(",");
        for(String param  : params){
            if(!param.contains("oauth_consumer_key") && param.substring(param.lastIndexOf("=")+1).equals(consumerKey)){
                return false;
            }

            //TODO other validation check
        }

        //All headers is present
        return true;
    }
}
