package com.felipegeroldi.gestao_vagas.modules.candidate.useCases;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.felipegeroldi.gestao_vagas.exceptions.UserNotFoundException;
import com.felipegeroldi.gestao_vagas.modules.candidate.dto.ProfileCandidateResponseDTO;
import com.felipegeroldi.gestao_vagas.modules.candidate.entity.CandidateEntity;
import com.felipegeroldi.gestao_vagas.modules.candidate.repository.CandidateRepository;

@Service
public class ProfileCandidateUseCase {
    private CandidateRepository candidateRepository;
    
    public ProfileCandidateResponseDTO execute(UUID idCandidate) {
        CandidateEntity candidate = candidateRepository.findById(idCandidate)
            .orElseThrow(() -> new UserNotFoundException());

        var profileDTO = ProfileCandidateResponseDTO.builder()
            .description(candidate.getDescription())
            .username(candidate.getUsername())
            .email(candidate.getEmail())
            .name(candidate.getName())
            .id(candidate.getId())
            .build();

        return profileDTO;
    }
}
