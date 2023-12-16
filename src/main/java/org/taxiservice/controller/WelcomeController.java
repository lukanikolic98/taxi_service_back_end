package org.taxiservice.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class WelcomeController {
  @GetMapping("/test")
  public String testGetMethod() {
    return "Welcome";
  }

}
