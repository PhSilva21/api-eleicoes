package com.bandeira.api_eleicoes.dtos;

import com.bandeira.api_eleicoes.model.enums.ElectionTurn;
import com.bandeira.api_eleicoes.model.enums.ElectionType;

import java.time.LocalDate;

public record CreateElectionDTO(

        LocalDate date,

        String uf,

        ElectionTurn electionTurn,

        ElectionType electionType,

        Double sessions
) {
}
