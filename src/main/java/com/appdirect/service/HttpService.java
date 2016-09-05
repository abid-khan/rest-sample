package com.appdirect.service;

import com.appdirect.exception.HttpServiceException;
import net.sf.json.JSONObject;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Created by abidk on 03/09/16.
 */
public interface HttpService {

    /**
     *
     * @param url
     * @return
     * @throws HttpServiceException
     */
    JSONObject getResponse(String url) throws HttpServiceException;

    /**
     *
     * @param url
     * @return
     * @throws HttpServiceException
     */
    String  validate(String url) throws HttpServiceException;
}
