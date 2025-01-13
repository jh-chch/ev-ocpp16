package com.ev.ocpp16.global.config.security;

import static com.ev.ocpp16.websocket.Constants.USER_TYPE_ADMIN;
import static com.ev.ocpp16.websocket.Constants.USER_TYPE_USER;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ev.ocpp16.domain.member.entity.Member;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	@Bean
	@Order(1)
	public SecurityFilterChain wsuserSecurityFilterChain(HttpSecurity http) throws Exception {
		// WebSocket 엔드포인트는 Security 필터 체인에서 제외
		return http
				.securityMatcher("/" + USER_TYPE_USER + "/**")
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth
						.anyRequest().permitAll())
				.build();
	}

	@Bean
	@Order(2)
	public SecurityFilterChain wsadminSecurityFilterChain(HttpSecurity http) throws Exception {
		return http
				.securityMatcher("/" + USER_TYPE_ADMIN + "/**")
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth
						.anyRequest().hasRole("ADMIN"))
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}

	@Bean
	@Order(3)
	public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
		return http
				.securityMatcher("/api/v1/**")
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/api/v1/auth/**").permitAll()
						.anyRequest().hasRole("ADMIN"))
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}

	@Bean
	@Order(4)
	public SecurityFilterChain formLoginSecurityFilterChain(HttpSecurity http) throws Exception {
		return http
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/login", "/logout", "/profile", "/health").permitAll()
						.anyRequest().authenticated())
				.formLogin(form -> form
						.loginPage("/login")
						.successHandler((request, response, authentication) -> {
							// 로그인 성공 시 처리
							Member principal = (Member) authentication.getPrincipal();
							TokenResponse jwtToken = JwtUtil.generateToken(principal.getEmail());

							// HttpOnly 쿠키에 JWT 저장
							ResponseCookie jwtCookie = ResponseCookie.from("jwt", jwtToken.getToken())
									.httpOnly(true)
									.secure(false)
									.path("/")
									.maxAge(Duration.between(jwtToken.getIssuedAt(), jwtToken.getExpiresAt()).getSeconds())
									.sameSite("Strict")
									.build();
							response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());
							response.sendRedirect("/");
						})
						.failureHandler((request, response, exception) -> {
							// 로그인 실패 시 처리
							response.sendRedirect("/login?error");
						})
						.permitAll())
				.logout(logout -> logout
						.logoutUrl("/logout")
						.logoutSuccessUrl("/login"))
				.csrf(csrf -> csrf.disable())
				.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
}