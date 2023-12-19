package org.taxiservice.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.taxiservice.dto.UserDTO;
import org.taxiservice.dto.UserTokenStateDTO;
import org.taxiservice.model.Authority;
import org.taxiservice.model.RegisteredUser;
import org.taxiservice.model.User;
import org.taxiservice.repository.AuthorityRepository;
import org.taxiservice.repository.UserRepository;
import org.taxiservice.security.CustomUserDetailsService;
import org.taxiservice.security.TokenUtils;
import net.bytebuddy.utility.RandomString;

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
  @Autowired
  private AuthorityRepository authorityRepository;
  @Autowired
  private MailService mailService;

  public void create(UserDTO dto) throws IOException {
    RegisteredUser user = new RegisteredUser(dto.getName(), dto.getSurname(), dto.getUsername(), dto.getPassword(),
        dto.getCity());
    user.setPassword(userDetailsService.encodePassword(user.getPassword()));

    Authority a = authorityRepository.findByName("ROLE_USER");

    List<Authority> authorityList = new ArrayList<>();
    authorityList.add(a);
    user.setAuthorities(authorityList);

    user.setRegistrationKey(RandomString.make(20));
    // Big NO nO, popraviti
    user.setEnabled(false);
    user = userRepository.save(user);

    mailService.sendTextEmail(user.getUsername(), user.getRegistrationKey());
    // generateToken(user.getUsername(), dto.getPassword());
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

  public void confirmRegistration(String registrationKey) {
    User user = userRepository.findByRegistrationKey(registrationKey);
    user.setEnabled(true);
    userRepository.save(user);
  }
}
