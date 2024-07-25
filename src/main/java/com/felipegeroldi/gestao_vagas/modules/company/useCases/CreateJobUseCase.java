package com.felipegeroldi.gestao_vagas.modules.company.useCases;

import org.springframework.stereotype.Service;

import com.felipegeroldi.gestao_vagas.exceptions.CompanyNotFoundException;
import com.felipegeroldi.gestao_vagas.modules.company.entities.JobEntity;
import com.felipegeroldi.gestao_vagas.modules.company.repositories.CompanyRepository;
import com.felipegeroldi.gestao_vagas.modules.company.repositories.JobRepository;

@Service
public class CreateJobUseCase {
    private JobRepository jobRepository;
    private CompanyRepository companyRepository;

    public CreateJobUseCase(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public JobEntity execute(JobEntity jobEntity) {
        companyRepository.findById(jobEntity.getCompanyId())
            .orElseThrow(() -> new CompanyNotFoundException());

        return this.jobRepository.save(jobEntity);
    }
}
