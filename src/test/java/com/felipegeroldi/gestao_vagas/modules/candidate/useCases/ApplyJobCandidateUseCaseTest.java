package com.felipegeroldi.gestao_vagas.modules.candidate.useCases;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.felipegeroldi.gestao_vagas.exceptions.JobNotFoundException;
import com.felipegeroldi.gestao_vagas.exceptions.UserNotFoundException;
import com.felipegeroldi.gestao_vagas.modules.candidate.entity.ApplyJobEntity;
import com.felipegeroldi.gestao_vagas.modules.candidate.entity.CandidateEntity;
import com.felipegeroldi.gestao_vagas.modules.candidate.repository.ApplyJobRepository;
import com.felipegeroldi.gestao_vagas.modules.candidate.repository.CandidateRepository;
import com.felipegeroldi.gestao_vagas.modules.company.entities.JobEntity;
import com.felipegeroldi.gestao_vagas.modules.company.repositories.JobRepository;

@ExtendWith(MockitoExtension.class)
public class ApplyJobCandidateUseCaseTest {

    @InjectMocks private ApplyJobCandidateUseCase applyJobCandidateUseCase;
    @Mock private CandidateRepository candidateRepository;
    @Mock private JobRepository jobRepository;
    @Mock private ApplyJobRepository applyJobRepository;

    @Test
    @DisplayName("Should not ble able to apply job with candidate not found")
    public void ShouldNotBeAbleToApplyJobWithCandidateNotFound() {
        assertThrows(UserNotFoundException.class, 
            () -> applyJobCandidateUseCase.execute(null, null));
    }

    @Test
    public void ShouldNotBeAbleToApplyJobWithJobNotFound() {
        var idCandidate = UUID.randomUUID();
        var candidate = new CandidateEntity();

        when(candidateRepository.findById(idCandidate))
            .thenReturn(Optional.of(candidate));

        assertThrows(JobNotFoundException.class,
            () -> applyJobCandidateUseCase.execute(idCandidate, null));
    }

    @Test
    public void ShoudBeAbleToCreateNewApplyJob() {
        var idCandidate = UUID.randomUUID();
        var idJob = UUID.randomUUID();
        var candidate = new CandidateEntity();
        var job = new JobEntity();


        var applyJob = ApplyJobEntity.builder()
            .candidateId(idCandidate)
            .jobId(idJob)
            .build();

        when(applyJobRepository.save(applyJob))
            .thenAnswer(invocation -> {
                applyJob.setId(UUID.randomUUID());
                applyJob.setCreatedAt(LocalDateTime.now());
                return applyJob;
            });

        when(candidateRepository.findById(idCandidate))
            .thenReturn(Optional.of(candidate));

        when(jobRepository.findById(idJob))
            .thenReturn(Optional.of(job));

        ApplyJobEntity result = applyJobCandidateUseCase.execute(idCandidate, idJob);
        
        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getCreatedAt());
    }
}
