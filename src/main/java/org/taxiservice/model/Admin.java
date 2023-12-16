package org.taxiservice.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends User {

    public Admin() {
        super();
    }

    public Admin(String name, String surname, String username, String password, String city) {
        super(name, surname, username, password, city);
    }

}