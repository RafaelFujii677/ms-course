package com.mscourse.hroauth.security.config;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import com.mscourse.hroauth.property.JwtConfiguration;
import com.mscourse.hroauth.security.filter.JwtTokenAuthorizationFilter;
import com.mscourse.hroauth.security.filter.JwtUsernameAndPasswordAuthenticationFilter;
import com.mscourse.hroauth.security.token.TokenConverter;
import com.mscourse.hroauth.security.token.TokenCreator;

@EnableWebSecurity
public class ResourceServerConfig extends WebSecurityConfigurerAdapter{
	private final UserDetailsService userDetailService;
	private final JwtConfiguration jwtConfiguration;
	private final TokenCreator tokenCreator;
	private final TokenConverter tokenConverter;

	@Autowired
	public ResourceServerConfig(UserDetailsService userDetailService, JwtConfiguration jwtConfiguration, TokenCreator tokenCreator, TokenConverter tokenConverter) {
		this.userDetailService = userDetailService;
		this.jwtConfiguration = jwtConfiguration;
		this.tokenCreator = tokenCreator;
		this.tokenConverter = tokenConverter;
	}

	@Autowired
	private Environment env;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		jwtConfiguration.setPrivateKey(env.getProperty("private-key.config"));
		http
				.csrf().disable()
				.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues())
				.and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.exceptionHandling().authenticationEntryPoint((req, resp, e) -> resp.sendError(HttpServletResponse.SC_UNAUTHORIZED))
				.and()
				.addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfiguration, tokenCreator))
				.addFilterAfter(new JwtTokenAuthorizationFilter(jwtConfiguration, tokenConverter), UsernamePasswordAuthenticationFilter.class)
				.authorizeRequests()
				.antMatchers(jwtConfiguration.getLoginUrl()).permitAll()
				.antMatchers("/hr-worker/**").hasAnyRole("USER", "ADMIN")
				.antMatchers("/hr-payroll/**").hasRole("ADMIN")
				.antMatchers("/hr-users/**").hasRole("ADMIN")
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
