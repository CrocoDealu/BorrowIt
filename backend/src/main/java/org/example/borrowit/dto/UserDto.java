package org.example.borrowit.dto;

import static org.example.borrowit.utils.HashStringUtility.hashOwnerIdentifier;

public class UserDto {
    private String name;
    private final String userHash;
    private String address;
    private String contactNumber;
    private int accountBalance;

    public UserDto(String name, String email, String address, String contactNumber, int accountBalance) {
        this.name = name;
        this.userHash = hashOwnerIdentifier(email);
        this.address = address;
        this.contactNumber = contactNumber;
        this.accountBalance = accountBalance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserHash() {
        return userHash;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public int getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(int accountBalance) {
        this.accountBalance = accountBalance;
    }
}
