package com.mscourse.hroauth.security.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.mscourse.hroauth.model.Role;
import com.mscourse.hroauth.model.User;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

public class SecurityContextUtil {
	private SecurityContextUtil() {

	}

	public static void setSecurityContext(SignedJWT signedJWT) throws JOSEException {
		try {
			JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
			String username = claims.getSubject();
			if(username == null) throw new JOSEException("Email não encontrado no jwt");

			List<String> authorities = claims.getStringListClaim("authorities");
			User user = new User(claims.getLongClaim("userId"), claims.getClaim("userName").toString() , username, "", convertList(authorities));
			UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, createAuthorities(authorities));
			auth.setDetails(signedJWT.serialize());
			SecurityContextHolder.getContext().setAuthentication(auth);
		} catch (Exception e) {
			System.out.println("Set security context");
			e.printStackTrace();
			SecurityContextHolder.clearContext();
		}
	}

	private static List<SimpleGrantedAuthority> createAuthorities(List<String> authorities) {
        return authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

	private static Set<Role> convertList(List<String> authorities){
		Set<Role> roles = new HashSet<>();
		authorities.stream().map(map -> roles.add(new Role(null, map)));
		return roles;
	}
}
