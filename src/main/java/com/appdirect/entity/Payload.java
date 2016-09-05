package com.appdirect.entity;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

/**
 * Created by abidk on 03/09/16.
 */
@Embeddable
public class Payload {

    private Company company;


    private Order order;


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

