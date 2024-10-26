package com.bandeira.api_eleicoes.util;

import com.bandeira.api_eleicoes.dtos.CreateCandidateDTO;
import com.bandeira.api_eleicoes.model.Candidate;
import com.bandeira.api_eleicoes.model.enums.SituationCandidate;

public class CandidateMapper {

    public static Candidate toEntity(CreateCandidateDTO createCandidateDTO){
        return new Candidate(createCandidateDTO.name(), SituationCandidate.FIRST_TURN
                , createCandidateDTO.vice(), createCandidateDTO.coalitionAndFederation());
    }
}
