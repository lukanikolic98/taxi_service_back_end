package org.taxiservice;

import org.taxiservice.security.CustomUserDetailsService;
import org.taxiservice.security.TokenUtils;
import org.taxiservice.security.auth.RestAuthenticationEntryPoint;
import org.taxiservice.security.auth.TokenAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	// @Autowired
	// private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

	@Lazy
	private CustomUserDetailsService jwtUserDetailsService;
	@Autowired
	private TokenUtils tokenUtils;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.authorizeHttpRequests((authz) -> authz
						// komunikacija izmedju klijenta i servera je stateless posto je u pitanju REST
						// aplikacija
						// sve neautentifikovane zahteve obradi uniformno i posalji 401 gresku
						// svim korisnicima dopusti da pristupe putanjama /auth/**, (/h2-console/** ako
						// se koristi H2 baza) i /api/foo
						.requestMatchers("/auth/**").permitAll().requestMatchers("/h2-console/**").permitAll()

						// za svaki drugi zahtev korisnik mora biti autentifikovan
						.anyRequest().authenticated()

				// umetni custom filter TokenAuthenticationFilter kako bi se vrsila provera JWT
				// tokena umesto cistih korisnickog imena i lozinke (koje radi
				// BasicAuthenticationFilter)
				// zbog jednostavnosti primera

				);
		http.addFilterAfter(new TokenAuthenticationFilter(tokenUtils, jwtUserDetailsService),
				BasicAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	public WebSecurityCustomizer securityCustomizer() {
		return (web) -> web.ignoring().requestMatchers("/auth/**").requestMatchers("/h2-console/**");
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// // Registrujemo authentication manager koji ce da uradi autentifikaciju
	// // korisnika za nas
	@Bean
	public AuthenticationManager authenticationManagerBean(UserDetailsService userDetailService,
			PasswordEncoder passwordEncoder) throws Exception {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailService);
		authenticationProvider.setPasswordEncoder(passwordEncoder);
		return new ProviderManager(authenticationProvider);
	}

	// // Definisemo uputstvo za authentication managera koji servis da koristi da
	// // izvuce podatke o korisniku koji zeli da se autentifikuje,
	// // kao i kroz koji enkoder da provuce lozinku koju je dobio od klijenta u
	// // zahtevu da bi adekvatan hash koji dobije kao rezultat bcrypt algoritma
	// // uporedio sa onim koji se nalazi u bazi (posto se u bazi ne cuva plain
	// // lozinka)
	// @Autowired
	// public void configureGlobal(AuthenticationManagerBuilder auth) throws
	// Exception {
	// auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
	// }

	// // Injektujemo implementaciju iz TokenUtils klase kako bismo mogli da
	// koristimo
	// // njene metode za rad sa JWT u TokenAuthenticationFilteru

	// // Definisemo prava pristupa odredjenim URL-ovima
	// @Override
	// protected void configure(HttpSecurity http) throws Exception {
	// http
	// // komunikacija izmedju klijenta i servera je stateless posto je u pitanju
	// REST
	// // aplikacija
	// .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

	// // sve neautentifikovane zahteve obradi uniformno i posalji 401 gresku
	// .exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint).and()

	// // svim korisnicima dopusti da pristupe putanjama /auth/**, (/h2-console/**
	// ako
	// // se koristi H2 baza) i /api/foo
	// .authorizeRequests().antMatchers("/auth/**").permitAll()

	// // za svaki drugi zahtev korisnik mora biti autentifikovan
	// .anyRequest().authenticated().and()
	// // za development svrhe ukljuci konfiguraciju za CORS iz WebConfig klase
	// .cors().and()

	// // umetni custom filter TokenAuthenticationFilter kako bi se vrsila provera
	// JWT
	// // tokena umesto cistih korisnickog imena i lozinke (koje radi
	// // BasicAuthenticationFilter)
	// .addFilterBefore(new TokenAuthenticationFilter(tokenUtils,
	// jwtUserDetailsService),
	// BasicAuthenticationFilter.class);
	// // zbog jednostavnosti primera
	// http.csrf().disable();
	// }

	// // Generalna bezbednost aplikacije
	// @Override
	// public void configure(WebSecurity web) throws Exception {
	// // TokenAuthenticationFilter ce ignorisati sve ispod navedene putanje
	// web.ignoring().antMatchers(HttpMethod.POST, "/auth/login", "/auth/register,
	// /reviews/*", "/reviews");
	// web.ignoring().antMatchers(HttpMethod.GET, "/", "/webjars/**", "/*.html",
	// "/favicon.ico", "/**/*.html",
	// "/**/*.css", "/**/*.js", "/categories", "/cultural_offers/*", "/reviews",
	// "/reviews/*", "/reviews/offer/*", "/posts",
	// "/posts/offer/*","/pictures", "/images/*");
	// }
}