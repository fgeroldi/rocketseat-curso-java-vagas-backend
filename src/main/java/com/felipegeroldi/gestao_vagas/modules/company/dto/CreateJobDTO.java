package com.felipegeroldi.gestao_vagas.modules.company.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateJobDTO {
    @Schema(example = "Vaga para pessoa desenvolvedora Júnior", requiredMode = RequiredMode.REQUIRED)
    private String description;

    @Schema(example = "GymPass, Plano de Saúde", requiredMode = RequiredMode.REQUIRED)
    private String benefits;

    @Schema(example = "Júnior", requiredMode = RequiredMode.REQUIRED)
    @NotBlank(message = "Esse campo é obrigatório")
    private String level;
}
