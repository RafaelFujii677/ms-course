//package com.mscourse.security;
//
//import java.io.IOException;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import com.mscourse.service.OauthService;
//
//public class AuthorizationFilter extends UsernamePasswordAuthenticationFilter {
//	private final OauthService oauthService;
//	
//	@Autowired
//	public AuthorizationFilter(OauthService oauthService) {
//		this.oauthService = oauthService;
//	}
//	
//	@Override
//	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//		HttpServletRequest httpRequest = (HttpServletRequest) request;
//		String header = httpRequest.getHeader("Authorization");
//		if(header == null || !header.startsWith("Bearer ")) {
//			chain.doFilter(request, response);
//			return;
//		}
//
//		HttpServletResponse httpResponse = (HttpServletResponse) response;
//		String token = header.replace("Bearer ", "").trim();
//
//		try {
//			oauthService.authenticate(token);
//		} catch (Exception e) {
//			System.out.println("Authorization worker");
//			e.printStackTrace();
//		}
//
//		chain.doFilter(httpRequest, httpResponse);
//	}
//
//}
