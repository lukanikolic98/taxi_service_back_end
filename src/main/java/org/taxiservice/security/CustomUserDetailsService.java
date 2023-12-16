package org.taxiservice.security;

import org.taxiservice.model.User;
import org.taxiservice.repository.UserRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	protected final Log LOGGER = LogFactory.getLog(getClass());
	@Autowired
	private UserRepository userRepository;
	@Lazy
	private AuthenticationManager authenticationManager;
	@Autowired
	private PasswordEncoder passwordEncoder;

	// Funkcija koja na osnovu username-a iz baze vraca objekat User-a
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
		} else {
			return user;
		}
	}

	// Funkcija pomocu koje korisnik menja svoju lozinku
	public String changePassword(String oldPassword, String newPassword) {

		// Ocitavamo trenutno ulogovanog korisnika
		Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
		String username = currentUser.getName();

		LOGGER.debug("Re-authenticating user '" + username + "' for password change request.");
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, oldPassword));
		LOGGER.debug("Changing password for user '" + username + "'");

		User user = (User) loadUserByUsername(username);
		changePasswordUtil(user, newPassword);

		userRepository.save(user);

		return username;
	}

	public void changePasswordUtil(User user, String newPassword) {
		user.setPassword(passwordEncoder.encode(newPassword));
	}

	public String encodePassword(String password) {
		return passwordEncoder.encode(password);
	}
}
