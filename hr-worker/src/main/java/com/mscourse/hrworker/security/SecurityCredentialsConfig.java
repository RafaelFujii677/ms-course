package com.mscourse.hrworker.security;
//package com.mscourse.security;
//
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.web.cors.CorsConfiguration;
//
//import com.mscourse.service.OauthService;
//
//@EnableWebSecurity
//public class SecurityCredentialsConfig extends WebSecurityConfigurerAdapter{
//	private final OauthService oauthService;
//	
//	@Autowired
//	public SecurityCredentialsConfig(OauthService oauthService) {
//		this.oauthService = oauthService;
//	}
//	
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http
//				.csrf().disable()
//				.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues())
//				.and()
//				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//				.and()
//				.exceptionHandling().authenticationEntryPoint((req, resp, e) -> resp.sendError(HttpServletResponse.SC_UNAUTHORIZED))
//				.and()
//				.addFilter(new AuthorizationFilter(oauthService))
//				.authorizeRequests()
//				.antMatchers("/hr-worker/**").hasAnyRole("USER", "ADMIN")
//				.anyRequest().authenticated();
//	}
//}
