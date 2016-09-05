package com.appdirect.entity;

import javax.persistence.*;
import java.util.List;

/**
 * Created by abidk on 03/09/16.
 */
@Entity
@Table(name = "ad_orders")
public class Order   extends  AbstractAuditablEntity{

    private String editionCode;
    private String pricingDuration;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    private List<Item> items;

    @OneToOne
    @PrimaryKeyJoinColumn
    private Subscription subscription;

    public String getEditionCode() {
        return editionCode;
    }

    public void setEditionCode(String editionCode) {
        this.editionCode = editionCode;
    }

    public String getPricingDuration() {
        return pricingDuration;
    }

    public void setPricingDuration(String pricingDuration) {
        this.pricingDuration = pricingDuration;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }
}
