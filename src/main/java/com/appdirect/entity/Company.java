package com.appdirect.entity;

import javax.persistence.*;

/**
 * Created by abidk on 03/09/16.
 */

@Entity
@Table(name = "ad_companies")
public class Company  extends  AbstractAuditablEntity{
    private String country;
    private String name;
    private String phNo;
    @Column(name = "company_uuid")
    private String uuid;
    @Column(name = "company_website")
    private String website;

    @OneToOne
    @PrimaryKeyJoinColumn
    private Subscription subscription;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhNo() {
        return phNo;
    }

    public void setPhNo(String phNo) {
        this.phNo = phNo;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }
}
