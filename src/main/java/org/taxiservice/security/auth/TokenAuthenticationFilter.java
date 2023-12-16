package org.taxiservice.security.auth;

import org.taxiservice.model.User;
import org.taxiservice.security.TokenUtils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

//Filter koji ce presretati svaki zahtev klijenta ka serveru
//Sem nad putanjama navedenim u WebSecurityConfig.configure(WebSecurity web)
public class TokenAuthenticationFilter extends OncePerRequestFilter {

	private TokenUtils tokenUtils;

	private UserDetailsService userDetailsService;

	public TokenAuthenticationFilter(TokenUtils tokenHelper, UserDetailsService userDetailsService) {
		this.tokenUtils = tokenHelper;
		this.userDetailsService = userDetailsService;
	}

	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		String username;
		String authToken = tokenUtils.getToken(request);

		if (authToken != null) {
			// uzmi username iz tokena
			username = tokenUtils.getUsernameFromToken(authToken);

			if (username != null) {
				// uzmi user-a na osnovu username-a
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				User u = (User) userDetails;
				// provera da li je admin promenio lozinku, ako nije treba da se redirektuje na
				// change-password
				if (u.getLastPasswordResetDate() != null || (u.getRole().equals("ADMIN") && u.getLastPasswordResetDate() == null
						&& request.getRequestURI().equals("/auth/change-password"))) {
					// proveri da li je prosledjeni token validan
					if (tokenUtils.validateToken(authToken, userDetails)) {
						// kreiraj autentifikaciju
						TokenBasedAuthentication authentication = new TokenBasedAuthentication(userDetails);
						authentication.setToken(authToken);
						SecurityContextHolder.getContext().setAuthentication(authentication);
					}
				}
			}
		}
		// prosledi request dalje u sledeci filter
		chain.doFilter(request, response);
	}

}