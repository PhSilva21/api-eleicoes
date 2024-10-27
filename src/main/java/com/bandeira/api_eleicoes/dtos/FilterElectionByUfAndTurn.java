package com.bandeira.api_eleicoes.dtos;

import com.bandeira.api_eleicoes.model.enums.ElectionTurn;

public record FilterElectionByUfAndTurn(

        String uf,

        ElectionTurn electionTurn
) {
}
