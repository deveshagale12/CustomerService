package com.CustomerService.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "customer_contacts")
public class CustomerContact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contact_id")
    private Long contactId;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "contact_type", nullable = false, length = 30)
    private String contactType;

    @Column(name = "contact_value", nullable = false, length = 200)
    private String contactValue;

    @Column(name = "is_verified", nullable = false)
    private boolean isVerified;

    public CustomerContact() {
    }

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getContactType() {
        return contactType;
    }

    public void setContactType(String contactType) {
        this.contactType = contactType;
    }

    public String getContactValue() {
        return contactValue;
    }

    public void setContactValue(String contactValue) {
        this.contactValue = contactValue;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }
}
