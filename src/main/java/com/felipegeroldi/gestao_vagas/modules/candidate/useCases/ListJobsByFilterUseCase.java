package com.felipegeroldi.gestao_vagas.modules.candidate.useCases;

import java.util.List;

import org.springframework.stereotype.Service;

import com.felipegeroldi.gestao_vagas.modules.company.entities.JobEntity;
import com.felipegeroldi.gestao_vagas.modules.company.repositories.JobRepository;

@Service
public class ListJobsByFilterUseCase {
    private JobRepository jobRepository;
    
    public ListJobsByFilterUseCase(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public List<JobEntity> execute(String filter) {
        return this.jobRepository.findByDescriptionContainingIgnoreCase(filter);
    }
}
