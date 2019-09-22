package com.xcodeassociated.cloud.gateway.security.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

@Component
public class PBKDF2Encoder implements PasswordEncoder {

	private String secret;
	private Integer iteration;
	private Integer keylength;

    public PBKDF2Encoder(@Value("${springbootwebfluxjjwt.password.encoder.secret}") String secret,
                         @Value("${springbootwebfluxjjwt.password.encoder.iteration}") Integer iteration,
                         @Value("${springbootwebfluxjjwt.password.encoder.keylength}") Integer keylength) {
        this.secret = secret;
        this.iteration = iteration;
        this.keylength = keylength;
    }

    @Override
	public String encode(CharSequence cs) {
		try {
			byte[] result = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512")
											.generateSecret(new PBEKeySpec(cs.toString().toCharArray(), secret.getBytes(), iteration, keylength))
											.getEncoded();
			return Base64.getEncoder().encodeToString(result);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public boolean matches(CharSequence cs, String string) {
		return encode(cs).equals(string);
	}

}
