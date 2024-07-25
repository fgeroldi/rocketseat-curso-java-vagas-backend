package com.felipegeroldi.gestao_vagas.modules.candidate.useCases;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.felipegeroldi.gestao_vagas.exceptions.JobNotFoundException;
import com.felipegeroldi.gestao_vagas.exceptions.UserNotFoundException;
import com.felipegeroldi.gestao_vagas.modules.candidate.entity.ApplyJobEntity;
import com.felipegeroldi.gestao_vagas.modules.candidate.repository.ApplyJobRepository;
import com.felipegeroldi.gestao_vagas.modules.candidate.repository.CandidateRepository;
import com.felipegeroldi.gestao_vagas.modules.company.repositories.JobRepository;

@Service
public class ApplyJobCandidateUseCase {
    private CandidateRepository candidateRepository;
    private JobRepository jobRepository;
    private ApplyJobRepository applyJobRepository;
    
    public ApplyJobCandidateUseCase(CandidateRepository candidateRepository, JobRepository jobRepository,
            ApplyJobRepository applyJobRepository) {
        this.candidateRepository = candidateRepository;
        this.jobRepository = jobRepository;
        this.applyJobRepository = applyJobRepository;
    }

    public ApplyJobEntity execute(UUID idCandidate, UUID idJob) {
        // Validar se o candidato existe
        candidateRepository.findById(idCandidate)
            .orElseThrow(() -> new UserNotFoundException());

        // Validar se a vaga existe
        jobRepository.findById(idJob)
            .orElseThrow(() -> new JobNotFoundException());

        // Candidato se inscrever na vaga
        var applyJob = ApplyJobEntity.builder()
            .candidateId(idCandidate)
            .jobId(idJob)
            .build();

        applyJob = applyJobRepository.save(applyJob);
        return applyJob;
    }
}
