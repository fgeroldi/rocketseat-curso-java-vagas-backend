package com.felipegeroldi.gestao_vagas.modules.company.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.felipegeroldi.gestao_vagas.exceptions.UserFoundException;
import com.felipegeroldi.gestao_vagas.modules.company.entities.CompanyEntity;
import com.felipegeroldi.gestao_vagas.modules.company.useCases.CreateCompanyUseCase;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/company")
@Tag(name = "Empresa", description = "Informações das empresas")
public class CompanyController {
    private CreateCompanyUseCase createCompanyUseCase;

    public CompanyController(CreateCompanyUseCase createCompanyUseCase) {
        this.createCompanyUseCase = createCompanyUseCase;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CompanyEntity entity) {
        try {
            var result = createCompanyUseCase.execute(entity);
            return ResponseEntity.ok().body(result);
        } catch (UserFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
