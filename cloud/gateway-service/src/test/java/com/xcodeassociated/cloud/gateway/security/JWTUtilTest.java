package com.xcodeassociated.cloud.gateway.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.xcodeassociated.cloud.gateway.security.dto.UserQueryResponseServiceDto;
import com.xcodeassociated.cloud.gateway.security.model.UserRole;
import com.xcodeassociated.cloud.gateway.security.model.UserSubject;
import com.xcodeassociated.cloud.gateway.security.utils.JWTUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Instant.class, JWTUtil.class})
@PowerMockIgnore("javax.crypto.*")
public class JWTUtilTest {

    private String instantExpected = "2019-09-22T00:00:00Z";

    @Before
    public void setUp() {
        Clock clock = Clock.fixed(Instant.parse(this.instantExpected), ZoneId.of("UTC"));
        Instant instant = Instant.now(clock);
        PowerMockito.mockStatic(Instant.class);
        when(Instant.now()).thenReturn(instant);
    }

    @Test
    public void timeMock_Test() {
        Instant now = Instant.now();
        assertEquals(now.toString(), this.instantExpected);
    }

    @Test
    public void timeMockFormat_Test() throws ParseException {
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = isoFormat.parse(this.instantExpected);
        assertEquals(date, Date.from(Instant.now()));
    }

    @Test
    public void generateToken_Test() throws JsonProcessingException, ParseException {
        // given
        final String secret = "ThisIsSecretForJWTHS512SignatureAlgorithmThatMUSTHave512bitsKeySize";
        final Integer expirationTime = 28800;

        final UserQueryResponseServiceDto userQueryResponseServiceDto =
            new UserQueryResponseServiceDto(1L, "admin", "admin", Arrays.asList(UserRole.ROLE_ADMIN));
        final UserSubject userSubject = new UserSubject(1L, "admin");

        final String expected = "eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjpbIlJPTEVfQURNSU4iXSwic3ViIjoie1wiaWRcIjoxLFwibmFtZVwiOlwiYWRtaW5cIn0iLCJpYXQiOjE1NjkxMTA0MDAsImV4cCI6MTU2OTEzOTIwMH0.gcD9zFm6mFRGtUBn8XsqL4KG_a5RNgZqWN7vY9WlB5-g6wYpcsiWZMHMTK3OcPC3wIjsPPmEYJ0NOYyJJl139g";

        // when
        final JWTUtil jwtUtil = new JWTUtil(secret, expirationTime.toString());
        final String result = jwtUtil.generateToken(userQueryResponseServiceDto, userSubject);

        // then
        assertEquals(expected, result);
    }

}
