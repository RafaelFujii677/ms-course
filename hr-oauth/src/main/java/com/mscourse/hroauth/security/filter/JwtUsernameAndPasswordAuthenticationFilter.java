package com.mscourse.hroauth.security.filter;

import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mscourse.hroauth.model.User;
import com.mscourse.hroauth.property.JwtConfiguration;
import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
	private final AuthenticationManager authenticationManager;
	private final JwtConfiguration jwtConfiguration;

	@Autowired
	public JwtUsernameAndPasswordAuthenticationFilter(AuthenticationManager authenticationManager, JwtConfiguration jwtConfiguration) {
		this.authenticationManager = authenticationManager;
		this.jwtConfiguration = jwtConfiguration;
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		try {
			User user = new ObjectMapper().readValue(request.getInputStream(), User.class);			
			if(user == null) throw new UsernameNotFoundException("Não foi possível obter o usuário e a senha");
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(), user.getRoles());
			usernamePasswordAuthenticationToken.setDetails(user);
			return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication auth) throws IOException, ServletException {
		try {
			SignedJWT signedJWT = createSignedJWT(auth);
			String ecyptedToken = encryptToken(signedJWT);
			response.addHeader("Access-Control-Expose-Headers", "XSRF-TOKEN, " + jwtConfiguration.getHeader().getName());
			response.addHeader(jwtConfiguration.getHeader().getName(), jwtConfiguration.getHeader().getPrefix() + ecyptedToken);
		} catch (JOSEException e) {
			e.printStackTrace();
		}
		
	}

	private SignedJWT createSignedJWT(Authentication auth) throws JOSEException {
		User user = (User) auth.getPrincipal();
		JWTClaimsSet jwtClaimSet = createJWTClaimSet(auth, user);
		
		KeyPair rsaKeys = generateRsaKey();
		
		JWK jwk = new RSAKey.Builder((RSAPublicKey) rsaKeys.getPublic()).keyID(UUID.randomUUID().toString()).build();
		
		SignedJWT signedJwt = new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.RS256)
				.jwk(jwk)
				.type(JOSEObjectType.JWT)
				.build(), jwtClaimSet);
		
		// assinatura chave privada
		RSASSASigner signer = new RSASSASigner(rsaKeys.getPrivate());
		signedJwt.sign(signer);
		
		System.out.println("Serialized token " + signedJwt.serialize());

		return signedJwt;
	}

	private JWTClaimsSet createJWTClaimSet(Authentication auth, User user) {
		return new JWTClaimsSet.Builder()
				.subject(user.getEmail())
				.claim("authorities", auth.getAuthorities()
						.stream()
						.map(GrantedAuthority::getAuthority)
						.collect(Collectors.toList()))
//				.issuer(null) TODO
				.issueTime(new Date()).expirationTime(new Date(System.currentTimeMillis() + jwtConfiguration.getExpiration() * 1000))
				.build();
	}

	private static KeyPair generateRsaKey() {
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(2048);
			return keyPairGenerator.generateKeyPair();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private String encryptToken(SignedJWT signedJWT) throws JOSEException {
		DirectEncrypter directEncrypter = new DirectEncrypter(jwtConfiguration.getPrivateKey().getBytes());
		
		JWEObject jweObject = new JWEObject(new JWEHeader.Builder(JWEAlgorithm.DIR, EncryptionMethod.A128CBC_HS256)
				.contentType("JWT")
				.build(), new Payload(signedJWT));
		jweObject.encrypt(directEncrypter);
		return jweObject.serialize();
	}
}
