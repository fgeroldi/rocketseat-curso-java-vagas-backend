package com.felipegeroldi.gestao_vagas.modules.candidate.useCases;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.felipegeroldi.gestao_vagas.exceptions.UserFoundException;
import com.felipegeroldi.gestao_vagas.modules.candidate.entity.CandidateEntity;
import com.felipegeroldi.gestao_vagas.modules.candidate.repository.CandidateRepository;

@Service
public class CreateCandidateUseCase {

    private CandidateRepository candidateRepository;
    private PasswordEncoder passwordEncoder;

    public CreateCandidateUseCase(CandidateRepository candidateRepository,  PasswordEncoder passwordEncoder) {
        this.candidateRepository = candidateRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public CandidateEntity execute(CandidateEntity candidateEntity) {
        this.candidateRepository.findByEmailOrEmail(
                candidateEntity.getUsername(),
                candidateEntity.getEmail())
            .ifPresent((user) -> {
                throw new UserFoundException();
            });

        var password = passwordEncoder.encode(candidateEntity.getPassword());
        candidateEntity.setPassword(password);

        return this.candidateRepository.save(candidateEntity);
    }
}
