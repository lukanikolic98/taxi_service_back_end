package org.taxiservice.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class WelcomeController {
  @GetMapping("/test")
  @PreAuthorize("hasRole('ROLE_USER')")
  public String testGetMethod() {
    System.out.println("Usli smo u /test get method");
    return "Welcome";
  }

  @GetMapping("/admintest")
  @PreAuthorize("hasRole('ADMIN')")
  public String testAdminGetMethod() {
    return "Welcome Admin";
  }
}
