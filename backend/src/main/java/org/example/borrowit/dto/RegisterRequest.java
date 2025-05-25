package org.example.borrowit.dto;

public class RegisterRequest {
    private String email;
    private String password;
    private String name;
    private String contactNumber;
    private String address;

    public RegisterRequest(String email, String password, String name, String contactNumber, String address) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.contactNumber = contactNumber;
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getAddress() {
        return address;
    }
}
