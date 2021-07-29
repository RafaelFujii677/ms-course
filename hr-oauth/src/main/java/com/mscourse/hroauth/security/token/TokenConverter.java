package com.mscourse.hroauth.security.token;

import java.nio.file.AccessDeniedException;
import java.text.ParseException;

import org.springframework.stereotype.Service;

import com.mscourse.hroauth.property.JwtConfiguration;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.SignedJWT;

@Service
public class TokenConverter {
	private final JwtConfiguration jwtConfiguration;

	public TokenConverter(JwtConfiguration jwtConfiguration) {
		this.jwtConfiguration = jwtConfiguration;
	}

	public String decryptToken(String encryptedToken) throws ParseException, JOSEException {
		JWEObject jweObject = JWEObject.parse(encryptedToken);
		DirectDecrypter directDecrypter = new DirectDecrypter(jwtConfiguration.getPrivateKey().getBytes());
		jweObject.decrypt(directDecrypter);

		return jweObject.getPayload().toSignedJWT().serialize();
	}

	public void validadeTokenSignature(String signedToken) throws ParseException, JOSEException, AccessDeniedException{
		SignedJWT signedJWT = SignedJWT.parse(signedToken);
		RSAKey publicKey = RSAKey.parse(signedJWT.getHeader().getJWK().toJSONObject());
		if(!signedJWT.verify(new RSASSAVerifier(publicKey))) throw new AccessDeniedException("Assinatura de token inválida");

	}
}
