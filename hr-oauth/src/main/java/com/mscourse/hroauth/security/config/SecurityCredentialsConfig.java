package com.mscourse.hroauth.security.config;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;

import com.mscourse.hroauth.property.JwtConfiguration;
import com.mscourse.hroauth.security.filter.JwtUsernameAndPasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityCredentialsConfig extends WebSecurityConfigurerAdapter{
	private final UserDetailsService userDetailService;
	private final JwtConfiguration jwtConfiguration;
	
	@Autowired
	public SecurityCredentialsConfig(UserDetailsService userDetailService, JwtConfiguration jwtConfiguration) {
		this.userDetailService = userDetailService;
		this.jwtConfiguration = jwtConfiguration;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.csrf().disable()
				.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues())
				.and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.exceptionHandling().authenticationEntryPoint((req, resp, e) -> resp.sendError(HttpServletResponse.SC_UNAUTHORIZED))
				.and()
				.addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfiguration))
				.authorizeRequests()
				.antMatchers(jwtConfiguration.getLoginUrl()).permitAll()
				.antMatchers("/hr-worker/**").hasRole("USER")
				.antMatchers("/hr-payroll/**").hasRole("ADMIN")
				.anyRequest().authenticated();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailService).passwordEncoder(bCryptPasswordEncoder());
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
