package com.mscourse.hroauth.security.filter;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.text.ParseException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import com.mscourse.hroauth.property.JwtConfiguration;
import com.mscourse.hroauth.security.token.TokenConverter;
import com.mscourse.hroauth.security.util.SecurityContextUtil;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;

public class JwtTokenAuthorizationFilter extends OncePerRequestFilter{
	private final JwtConfiguration jwtConfiguration;
	private final TokenConverter tokenConverter;
	
	public JwtTokenAuthorizationFilter(JwtConfiguration jwtConfiguration, TokenConverter tokenConverter) {
		this.jwtConfiguration = jwtConfiguration;
		this.tokenConverter = tokenConverter;
	}
	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
		String header = request.getHeader(jwtConfiguration.getHeader().getName());
		if(header == null || !header.startsWith(jwtConfiguration.getHeader().getPrefix())) {
			filterChain.doFilter(request, response);
			return;
		}
		
		String token = header.replace(jwtConfiguration.getHeader().getPrefix(), "").trim();

		try {
			SecurityContextUtil.setSecurityContext(StringUtils.equalsIgnoreCase("signed", jwtConfiguration.getType()) ? validade(token) : decryptValidating(token));
		} catch (AccessDeniedException e) {
			System.out.println("Acesso negado");
			e.printStackTrace();
		} catch (JOSEException e) {
			System.out.println("Excessão Jose");
			e.printStackTrace();
		} catch (ParseException e) {
			System.out.println("Erro de parse");
			e.printStackTrace();
		}
		filterChain.doFilter(request, response);
	}

	private SignedJWT decryptValidating(String encryptedToken) throws ParseException, AccessDeniedException, JOSEException {
		String signedToken = tokenConverter.decryptToken(encryptedToken);
		tokenConverter.validadeTokenSignature(signedToken);
		return SignedJWT.parse(signedToken);
	}

	private SignedJWT validade(String signedToken) throws ParseException, AccessDeniedException, JOSEException {
		tokenConverter.validadeTokenSignature(signedToken);
		return SignedJWT.parse(signedToken);
	}
}
