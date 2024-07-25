package com.felipegeroldi.gestao_vagas.utils;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestUtils {
    public static String objectToJSON(Object obj) throws JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.writeValueAsString(obj);
    }

    public static String generateToken(UUID idCompany, String secretKey) {
        var expiresIn = Instant.now().plus(Duration.ofHours(2));

        String token = JWT.create().withIssuer("vagascomp")
            .withSubject(idCompany.toString())
            .withClaim("roles", Arrays.asList("COMPANY"))
            .withExpiresAt(expiresIn)
            .sign(Algorithm.HMAC256(secretKey));

        return token;
    }
}
