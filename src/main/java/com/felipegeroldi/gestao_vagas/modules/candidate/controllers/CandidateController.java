package com.felipegeroldi.gestao_vagas.modules.candidate.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.felipegeroldi.gestao_vagas.modules.candidate.dto.ProfileCandidateResponseDTO;
import com.felipegeroldi.gestao_vagas.modules.candidate.entity.ApplyJobEntity;
import com.felipegeroldi.gestao_vagas.modules.candidate.entity.CandidateEntity;
import com.felipegeroldi.gestao_vagas.modules.candidate.useCases.ApplyJobCandidateUseCase;
import com.felipegeroldi.gestao_vagas.modules.candidate.useCases.CreateCandidateUseCase;
import com.felipegeroldi.gestao_vagas.modules.candidate.useCases.ListJobsByFilterUseCase;
import com.felipegeroldi.gestao_vagas.modules.candidate.useCases.ProfileCandidateUseCase;
import com.felipegeroldi.gestao_vagas.modules.company.entities.JobEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/candidate")
@Tag(name = "Candidato", description = "Informações do candidato")
public class CandidateController {
    private CreateCandidateUseCase createCandidateUseCase;
    private ProfileCandidateUseCase profileCandidadeUseCase;
    private ListJobsByFilterUseCase listJobsByFilterUseCase;
    private ApplyJobCandidateUseCase applyJobCandidateUseCase;

    public CandidateController(CreateCandidateUseCase createCandidateUseCase,
            ProfileCandidateUseCase profileCandidadeUseCase, ListJobsByFilterUseCase listJobsByFilterUseCase,
            ApplyJobCandidateUseCase applyJobCandidateUseCase) {
        this.createCandidateUseCase = createCandidateUseCase;
        this.profileCandidadeUseCase = profileCandidadeUseCase;
        this.listJobsByFilterUseCase = listJobsByFilterUseCase;
        this.applyJobCandidateUseCase = applyJobCandidateUseCase;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CandidateEntity candidateEntity) {
        try {
            CandidateEntity result = createCandidateUseCase.execute(candidateEntity);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/")
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(summary = "Perfil do candidato", description = "Essa função é responsável por buscar as informações do perfil do candidato")
    @SecurityRequirement(name = "jwt_auth")
    @ApiResponses({
        @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = ProfileCandidateResponseDTO.class))
        }),
        @ApiResponse(responseCode = "400")
    })
    public ResponseEntity<?> get(HttpServletRequest request) {
        try {
            var idCandidate = UUID.fromString(request.getAttribute("candidate_id").toString());
            var profile = this.profileCandidadeUseCase.execute(idCandidate);
            return ResponseEntity.ok().body(profile);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/job")
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(summary = "Listagem de vagas disponíveis para o candidato", description = "Essa função é responsável por listar todas as vagas disponíveis, baseadas no filtro")
    @ApiResponses({
        @ApiResponse(responseCode = "200", content = {
            @Content(
                array = @ArraySchema(schema = @Schema(implementation = JobEntity.class))
            )
        })
    })
    @SecurityRequirement(name = "jwt_auth")
    public List<JobEntity> findJobByFilter(@RequestParam String filter) {
        return listJobsByFilterUseCase.execute(filter);
    }

    @PostMapping("/job/apply")
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(summary = "Inscrição do canditato para uma vaga", description = "Essa função é responsável por realizar a inscrição de um candiato em uma vaga")
    @ApiResponses({
        @ApiResponse(responseCode = "200", content = {
            @Content(schema = @Schema(implementation = ApplyJobEntity.class))
        })
    })
    @SecurityRequirement(name = "jwt_auth")
    public ResponseEntity<?> applyJob(@RequestBody UUID idJob, HttpServletRequest request) {
        var idCandidate = UUID.fromString(request.getAttribute("candidate_id").toString());

        try {
            var result = applyJobCandidateUseCase.execute(idCandidate, idJob);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
}
