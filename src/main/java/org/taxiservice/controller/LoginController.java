package org.taxiservice.controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.DisabledException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.taxiservice.dto.UserDTO;
import org.taxiservice.dto.UserTokenStateDTO;
import org.taxiservice.service.UserService;

@RestController
@Validated
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class LoginController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@Valid @RequestBody UserDTO user) {
        System.out.println("Register controller");
        UserTokenStateDTO created = userService.create(user);
        System.out.println("Register controller registered user: " + user.getUsername() + user.getId());
        return new ResponseEntity<>(created, HttpStatus.OK);
    }

    // @GetMapping("/register/{key}")
    // public ResponseEntity<UserDTO> confirmRegistration(@PathVariable("key")
    // String key) {
    // userService.confirmRegistration(key);
    // return new ResponseEntity<>(HttpStatus.OK);
    // }

    // @PostMapping("/login")
    // public ResponseEntity<UserTokenStateDTO> createAuthenticationToken(
    // @RequestBody JwtAuthenticationRequest authenticationRequest,
    // HttpServletResponse response) throws DisabledException, UserException {
    // String username = authenticationRequest.getUsername();
    // String password = authenticationRequest.getPassword();
    // UserTokenStateDTO token = userService.getLoggedIn(username, password);
    // return new ResponseEntity<>(token, HttpStatus.OK);

    // }

}
