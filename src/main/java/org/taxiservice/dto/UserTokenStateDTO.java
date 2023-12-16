package org.taxiservice.dto;

import javax.validation.constraints.NotBlank;

public class UserTokenStateDTO {

    private Long id;
    @NotBlank(message = "Access token is required!")
    private String accessToken;
    private Long expiresIn;
    @NotBlank(message = "User role is required!")
    private String userRole;

    public UserTokenStateDTO() {
        this.accessToken = null;
        this.expiresIn = null;
    }

    public UserTokenStateDTO(Long id, String accessToken, long expiresIn, String role) {
        this.id = id;
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.setUserRole(role);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}
