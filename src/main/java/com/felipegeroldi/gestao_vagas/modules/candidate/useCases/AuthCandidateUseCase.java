package com.felipegeroldi.gestao_vagas.modules.candidate.useCases;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.felipegeroldi.gestao_vagas.modules.candidate.dto.AuthCandidateRequestDTO;
import com.felipegeroldi.gestao_vagas.modules.candidate.dto.AuthCandidateResponseDTO;
import com.felipegeroldi.gestao_vagas.modules.candidate.repository.CandidateRepository;

@Service
public class AuthCandidateUseCase {
    @Value("${security.token.secret.candidate}")
    private String secretKey;

    private CandidateRepository candidateRepository;
    private PasswordEncoder passwordEncoder;

    public AuthCandidateUseCase(CandidateRepository candidateRepository, PasswordEncoder passwordEncoder) {
        this.candidateRepository = candidateRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthCandidateResponseDTO execute(AuthCandidateRequestDTO authCandidateRequestDTO) throws AuthenticationException {
        var candidate = candidateRepository.findByUsername(authCandidateRequestDTO.username())
            .orElseThrow(() -> {
                throw new UsernameNotFoundException("Username/password incorrect");
            });

        var passwordMatches = this.passwordEncoder.matches(authCandidateRequestDTO.password(), candidate.getPassword());
        if(!passwordMatches) {
            throw new AuthenticationException();
        }

        var algorithm = Algorithm.HMAC256(secretKey);
        var expiresIn = Instant.now().plus(Duration.ofMinutes(30));
        List<String> roles = Arrays.asList("CANDIDATE");
        var token = JWT.create()
            .withIssuer("test-vagas")
            .withExpiresAt(expiresIn)
            .withSubject(candidate.getId().toString())
            .withClaim("roles", roles)
            .sign(algorithm);

        var response = AuthCandidateResponseDTO.builder()
            .accessToken(token)
            .expiresIn(expiresIn.toEpochMilli())
            .roles(roles)
            .build();

        return response;
    }
}
