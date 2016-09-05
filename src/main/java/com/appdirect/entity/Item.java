package com.appdirect.entity;

import javax.persistence.*;

/**
 * Created by abidk on 03/09/16.
 */

@Entity
@Table(name = "ad_items")
public class Item  extends  AbstractAuditablEntity{

    private Long quantity;
    private String unit;

    @ManyToOne
    @JoinColumn(name="order_id")
    private Order order;

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
