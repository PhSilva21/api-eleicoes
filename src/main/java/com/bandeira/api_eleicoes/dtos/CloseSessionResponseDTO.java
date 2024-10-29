package com.bandeira.api_eleicoes.dtos;

import java.time.LocalDateTime;

public record CloseSessionResponseDTO(


        String percentage,

        LocalDateTime latestUpdate
) {
}
