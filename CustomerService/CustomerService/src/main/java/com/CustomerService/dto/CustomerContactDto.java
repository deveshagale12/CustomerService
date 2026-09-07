package com.CustomerService.dto;

import jakarta.validation.constraints.NotBlank;

public class CustomerContactDto {

    private Long contactId;

    @NotBlank(message = "contactType is required")
    private String contactType;

    @NotBlank(message = "contactValue is required")
    private String contactValue;

    private boolean isVerified;

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
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
