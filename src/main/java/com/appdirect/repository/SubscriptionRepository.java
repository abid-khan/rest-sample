package com.appdirect.repository;

import com.appdirect.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by abidk on 03/09/16.
 */
@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    @Transactional(readOnly = true)
    Subscription findByIdentifier(String identifier);
}


