package com.appdirect.service;

import com.appdirect.constant.StatusType;
import com.appdirect.entity.Subscription;
import com.appdirect.exception.SubscriptionNotPresentServiceException;
import com.appdirect.exception.SubscriptionServiceException;
import com.appdirect.repository.SubscriptionRepository;
import com.appdirect.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by abidk on 03/09/16.
 */
@Service
@Transactional(readOnly = true)
public class SubscriptionServiceImpl implements SubscriptionService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Override
    public Subscription findById(Long id) throws SubscriptionNotPresentServiceException {

        try {

            logger.info("Fetching a subscription");
            Subscription subscription = subscriptionRepository.findOne(id);
            if (CommonUtil.isNull(subscription)) {
                throw new SubscriptionNotPresentServiceException("Subscription with id " + id + "does not exists");
            }

            return subscription;

        } catch (Exception ex) {
            throw new SubscriptionServiceException("Failed created subscription due to  {}", ex);
        }
    }

    @Override
    public Subscription findByIdentifier(String identifier) throws SubscriptionNotPresentServiceException {
        try {

            logger.info("Fetching a subscription");
            Subscription subscription = subscriptionRepository.findByIdentifier(identifier);
            if (CommonUtil.isNull(subscription)) {
                throw new SubscriptionNotPresentServiceException("Subscription with String identifier " + identifier + " does not exists");
            }

            return subscription;

        } catch (Exception ex) {
            throw new SubscriptionServiceException("Failed to find subscription due to  {}", ex);
        }
    }

    @Transactional
    @Override
    public Subscription create(Subscription subscription) throws SubscriptionServiceException {

        try {

            logger.info("Creating a subscription");
            subscription = subscriptionRepository.saveAndFlush(subscription);
            return subscription;

        } catch (Exception ex) {
            throw new SubscriptionServiceException("Failed created subscription due to  {}", ex);
        }
    }

    @Transactional
    @Override
    public Subscription cancel(Subscription subscription) throws SubscriptionServiceException {

        try {

            logger.info("Canceling a subscription");
            subscription.setStatus(StatusType.CANCELED);
            return subscriptionRepository.save(subscription);

        } catch (Exception ex) {
            throw new SubscriptionServiceException("Failed cancel subscription due to  {}", ex);
        }
    }
}
