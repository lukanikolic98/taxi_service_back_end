package org.taxiservice.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("USER")
public class RegisteredUser extends User {

	public RegisteredUser() {
	}

	public RegisteredUser(String name, String surname, String username, String password, String city) {
		super(name, surname, username, password, city);
	}
}