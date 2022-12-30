package br.com.llduran.config;

import br.com.llduran.security.jwt.JwtConfigurer;
import br.com.llduran.security.jwt.JwtTokenProvider;
import br.com.llduran.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.HashMap;
import java.util.Map;
@Configuration
public class SecurityConfig
{
	@Autowired
	private JwtTokenProvider tokenProvider;

	@Autowired
	private UserServices userServices;

	@Bean
	public PasswordEncoder passwordEncoder()
	{
		Map<String, PasswordEncoder> encoders = new HashMap<>();
		encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
		DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", encoders);
		passwordEncoder.setDefaultPasswordEncoderForMatches(new Pbkdf2PasswordEncoder());
		return passwordEncoder;
	}

	@Bean
	AuthenticationManager authManager(HttpSecurity http) throws Exception
	{
		return http.getSharedObject(AuthenticationManagerBuilder.class)
				.userDetailsService(userServices)
				.passwordEncoder(passwordEncoder())
				.and()
				.build();
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception
	{
		http
			.httpBasic().disable()
			.csrf().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
					.authorizeRequests()
					.antMatchers(
							"/auth/signin",
							"/auth/refresh",
							"/api-docs/**",
							"/swagger-ui.html**"
					).permitAll()
					.antMatchers("/api/**").authenticated()
					.antMatchers("/users/**").denyAll()/*.hasRole("ADMIN")*/
				.and()
					.cors()
				.and()
				.apply(new JwtConfigurer(tokenProvider));

		return http.build();
	}
}