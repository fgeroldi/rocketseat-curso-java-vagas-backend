package com.felipegeroldi.gestao_vagas.modules.candidate.repository;

import com.felipegeroldi.gestao_vagas.modules.candidate.entity.ApplyJobEntity;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ApplyJobRepository extends JpaRepository<ApplyJobEntity, UUID>{
    
}
