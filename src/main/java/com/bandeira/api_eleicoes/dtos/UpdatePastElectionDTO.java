package com.bandeira.api_eleicoes.dtos;

import com.bandeira.api_eleicoes.model.enums.ElectionTurn;
import com.bandeira.api_eleicoes.model.enums.ElectionType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UpdatePastElectionDTO(

        Long pastElectionID,

        LocalDate date,

        String uf,

        ElectionTurn electionTurn,

        ElectionType electionType,

        Double totalSessions,

        Double remainingSessions,

        Double percentageOfSectionsTotaled,

        LocalDateTime latestUpdate
) {
}
