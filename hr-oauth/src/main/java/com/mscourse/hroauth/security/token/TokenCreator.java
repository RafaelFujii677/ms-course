package com.mscourse.hroauth.security.token;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

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

@Service
public class TokenCreator {
	private final JwtConfiguration jwtConfiguration;

	public TokenCreator(JwtConfiguration jwtConfiguration) {
		this.jwtConfiguration = jwtConfiguration;
	}

	public SignedJWT createSignedJWT(Authentication auth) throws JOSEException {
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

		return signedJwt;
	}

	private JWTClaimsSet createJWTClaimSet(Authentication auth, User user) {
		return new JWTClaimsSet.Builder()
				.subject(user.getEmail())
				.claim("authorities", auth.getAuthorities()
						.stream()
						.map(GrantedAuthority::getAuthority)
						.collect(Collectors.toList()))
				.claim("userId", user.getId())
				.claim("userName", user.getName())
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

	public String encryptToken(SignedJWT signedJWT) throws JOSEException {
		DirectEncrypter directEncrypter = new DirectEncrypter(jwtConfiguration.getPrivateKey().getBytes());
		
		JWEObject jweObject = new JWEObject(new JWEHeader.Builder(JWEAlgorithm.DIR, EncryptionMethod.A128CBC_HS256)
				.contentType("JWT")
				.build(), new Payload(signedJWT));
		jweObject.encrypt(directEncrypter);
		return jweObject.serialize();
	}
}
