package com.bandeira.api_eleicoes.dtos;

public record AddCandidateToElectionDTO(

        String candidateRegistration,

        Long electionId
) {
}
