package com.felipegeroldi.gestao_vagas.modules.company.useCases;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.felipegeroldi.gestao_vagas.modules.company.dto.AuthCompanyDTO;
import com.felipegeroldi.gestao_vagas.modules.company.dto.AuthCompanyResponseDTO;
import com.felipegeroldi.gestao_vagas.modules.company.repositories.CompanyRepository;

@Service
public class AuthCompanyUseCase {
    @Value("${security.token.secret}")
    private String secretKey;
    
    private CompanyRepository companyRepository;
    private PasswordEncoder passwordEncoder;

    public AuthCompanyUseCase(CompanyRepository companyRepository, PasswordEncoder passwordEncoder) {
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthCompanyResponseDTO execute(AuthCompanyDTO authCompanyDTO) throws AuthenticationException {
        var company = this.companyRepository.findByUsername(authCompanyDTO.getUsername())
            .orElseThrow(() -> {
                throw new UsernameNotFoundException("Username/password incorrect");
            });

        Boolean passwordMatches = this.passwordEncoder.matches(authCompanyDTO.getPassword(), company.getPassword());
        if (!passwordMatches) {
            throw new AuthenticationException();
        }

        var expiresIn = Instant.now().plus(Duration.ofHours(2));

        String token = JWT.create().withIssuer("vagascomp")
            .withSubject(company.getId().toString())
            .withClaim("roles", Arrays.asList("COMPANY"))
            .withExpiresAt(expiresIn)
            .sign(Algorithm.HMAC256(secretKey));


        var responseDTO = AuthCompanyResponseDTO.builder()
            .accessToken(token)
            .expiresIn(expiresIn.toEpochMilli())
            .build();

        return responseDTO;
    }
}
