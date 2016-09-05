package com.appdirect.service;

import com.appdirect.entity.Subscription;
import com.appdirect.exception.SubscriptionNotPresentServiceException;
import com.appdirect.exception.SubscriptionServiceException;
import org.springframework.stereotype.Service;

/**
 * Created by abidk on 03/09/16.
 */

public interface SubscriptionService {


    /**
     *
     * @param id
     * @return
     * @throws SubscriptionNotPresentServiceException
     */
    Subscription findById(Long id) throws SubscriptionNotPresentServiceException;

    /**
     *
     * @param identifier
     * @return
     * @throws SubscriptionNotPresentServiceException
     */
    Subscription findByIdentifier(String identifier) throws SubscriptionNotPresentServiceException;

    /**
     *
     * @param subscription
     * @return
     * @throws SubscriptionServiceException
     */
     Subscription create(Subscription  subscription) throws SubscriptionServiceException;

    /**
     *
     * @param subscription
     * @return
     * @throws SubscriptionServiceException
     */
     Subscription cancel(Subscription  subscription) throws SubscriptionServiceException;
}
