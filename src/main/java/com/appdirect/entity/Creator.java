package com.appdirect.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by abidk on 03/09/16.
 */
@Embeddable
public class Creator {

    @Embedded
    @AttributeOverrides({@AttributeOverride(name = "firstName", column = @Column(name = "address_first_name")), @AttributeOverride(name = "lastName", column = @Column(name = "address_last_name")),
            @AttributeOverride(name = "fullName", column = @Column(name = "address_full_name"))})
    private Address address;

    @NotNull
    private String email;

    private String firstName;

    private String lastName;

    private String language;

    private String openId;

    private String uuid;

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
