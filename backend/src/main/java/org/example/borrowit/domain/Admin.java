package org.example.borrowit.domain;


@jakarta.persistence.Entity
public class Admin extends Person {

    public Admin() {
        super();
    }

    public Admin(int aInt, String name, String email, String password) {
        super(aInt, name, email, password);
    }

}

