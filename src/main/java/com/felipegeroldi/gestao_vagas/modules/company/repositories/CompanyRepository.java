package com.felipegeroldi.gestao_vagas.modules.company.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.felipegeroldi.gestao_vagas.modules.company.entities.CompanyEntity;

public interface CompanyRepository extends JpaRepository<CompanyEntity, UUID> {
    public Optional<CompanyEntity> findByUsernameOrPassword(String username, String password);
    public Optional<CompanyEntity> findByUsername(String username);
}
