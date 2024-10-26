package com.bandeira.api_eleicoes.dtos;


public record CreateCandidateDTO(

        String name,

        String vice,

        String politicalPartyName,

        String coalitionAndFederation

) {
}
