package org.taxiservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserDTO {

  private Long id;
  @NotBlank(message = "Name is required!")
  private String name;
  @NotBlank(message = "Surname is required")
  private String surname;
  @NotBlank(message = "Username is required!")
  @Email(message = "Username should be your email address!")
  private String username;
  @NotBlank(message = "Password is required!")
  @Size(min = 5, message = "Password must be at least 5 characters!")
  // @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String password;
  @NotBlank(message = "City is required!")
  private String city;

  public UserDTO() {
  }

  public UserDTO(Long id, String name, String surname, String username, String password, String city) {
    this.id = id;
    this.name = name;
    this.surname = surname;
    this.username = username;
    this.password = password;
    this.city = city;
  }

  public UserDTO(String name, String surname, String username, String password, String city) {
    this.name = name;
    this.username = username;
    this.surname = surname;
    this.password = password;
    this.city = city;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof UserDTO))
      return false;

    UserDTO user = (UserDTO) obj;
    return user.getId().equals(this.id) && user.getUsername().equals(this.getUsername());
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSurname() {
    return surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }
}
