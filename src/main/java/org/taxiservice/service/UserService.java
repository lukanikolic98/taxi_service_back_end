package org.taxiservice.service;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.taxiservice.dto.UserDTO;
import org.taxiservice.dto.UserTokenStateDTO;
import org.taxiservice.model.RegisteredUser;
import org.taxiservice.model.User;
import org.taxiservice.repository.UserRepository;
import org.taxiservice.security.CustomUserDetailsService;
import org.taxiservice.security.TokenUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class UserService {
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private TokenUtils tokenUtils;
  @Autowired
  private AuthenticationManager authenticationManager;
  @Autowired
  private CustomUserDetailsService userDetailsService;

  public UserTokenStateDTO create(UserDTO dto) {
    RegisteredUser user = new RegisteredUser(dto.getName(), dto.getSurname(), dto.getUsername(), dto.getPassword(),
        dto.getCity());
    user.setPassword(userDetailsService.encodePassword(user.getPassword()));

    // Big NO nO, popraviti
    user.setEnabled(true);
    user = userRepository.save(user);
    return generateToken(user.getUsername(), dto.getPassword());
  }

  public UserTokenStateDTO generateToken(String username, String password) {
    Authentication authentication = null;
    authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    // create token
    User user = (User) authentication.getPrincipal();
    String jwt = tokenUtils.generateToken(username);
    int expiresIn = tokenUtils.getExpiredIn();

    return new UserTokenStateDTO(user.getId(), jwt, expiresIn, user.getRole());
  }

  public UserTokenStateDTO login(String username, String password) {
    UserTokenStateDTO token = generateToken(username, password);
    return token;

  }

  public User getOne(String username) throws NoSuchElementException {
    User user = userRepository.findByUsername(username);
    return user;
  }
}
