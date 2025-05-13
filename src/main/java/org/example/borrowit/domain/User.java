package org.example.borrowit.domain;

import jakarta.persistence.*;

@jakarta.persistence.Entity
@Table(name = "Users")
public class User extends Person {

    @Column(name = "address")
    private String address;

    @Column(name = "contact_number")
    private String contactNumber;

    @Column(name = "account_balance")
    private int accountBalance;

    public User() {
    }

    public User(int aInt, String name, String email, String password, String address, String contactNumber, int accountBalance) {
        super(aInt, name, email, password);
        this.address = address;
        this.contactNumber = contactNumber;
        this.accountBalance = accountBalance;
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