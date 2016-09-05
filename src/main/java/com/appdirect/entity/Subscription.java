package com.appdirect.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by abidk on 03/09/16.
 */
@Entity
@Table(name = "ad_subscription", uniqueConstraints = {@UniqueConstraint(columnNames = {"identifier","email", "creator_uuid"})})
public class Subscription extends  AbstractAuditablEntity{

    @NotNull
    private String identifier;

    @Embedded
    private Marketplace marketplace;
    @Embedded
    @AttributeOverrides(@AttributeOverride(name = "uuid", column = @Column(name = "creator_uuid")))
    private Creator creator;

    @OneToOne(mappedBy = "subscription", cascade = {CascadeType.ALL})
    private Company company;

    @OneToOne(mappedBy = "subscription", cascade = {CascadeType.ALL})
    private Order order;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Marketplace getMarketplace() {
        return marketplace;
    }

    public void setMarketplace(Marketplace marketplace) {
        this.marketplace = marketplace;
    }

    public Creator getCreator() {
        return creator;
    }

    public void setCreator(Creator creator) {
        this.creator = creator;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
