package com.xcodeassociated.cloud.gateway.security;

import com.xcodeassociated.cloud.gateway.security.utils.PBKDF2Encoder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class PBKDF2EncoderTest {

    @Test
    public void characterEncoder_Test() {
        // given
        final String secret = "ThisIsSecretForJWTHS512SignatureAlgorithmThatMUSTHave512bitsKeySize";
        final Integer iteration = 33;
        final Integer keylength = 256;

        final CharSequence cs = "user";
        final String expected = "sB/5YHPBhTF1UJDCATiNC2HDl683bAT9fctBCkuSpl0=";

        // when
        final PBKDF2Encoder encoder = new PBKDF2Encoder(secret, iteration, keylength);
        final String result = encoder.encode(cs);

        // then
        assertEquals(expected, result);
    }
}
