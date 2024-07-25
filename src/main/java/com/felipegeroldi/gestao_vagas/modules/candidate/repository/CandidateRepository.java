package com.felipegeroldi.gestao_vagas.modules.candidate.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.felipegeroldi.gestao_vagas.modules.candidate.entity.CandidateEntity;


@Repository
public interface CandidateRepository extends JpaRepository<CandidateEntity, UUID> {
    Optional<CandidateEntity> findByEmailOrEmail(String username, String email);
    Optional<CandidateEntity> findByUsername(String username);
}
