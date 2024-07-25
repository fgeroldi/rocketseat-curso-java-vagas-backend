package com.felipegeroldi.gestao_vagas.modules.company.useCases;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.felipegeroldi.gestao_vagas.exceptions.UserFoundException;
import com.felipegeroldi.gestao_vagas.modules.company.entities.CompanyEntity;
import com.felipegeroldi.gestao_vagas.modules.company.repositories.CompanyRepository;

@Service
public class CreateCompanyUseCase {
    private CompanyRepository companyRepository;
    private PasswordEncoder passwordEncoder;

    public CreateCompanyUseCase(CompanyRepository companyRepository, PasswordEncoder passwordEncoder) {
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public CompanyEntity execute(CompanyEntity entity) {
        this.companyRepository.findByUsernameOrPassword(
                entity.getUsername(), entity.getEmail())
            .ifPresent((user) -> {
                throw new UserFoundException();
            });

            String password = passwordEncoder.encode(entity.getPassword());
            entity.setPassword(password);

        return companyRepository.save(entity);
    }
}
