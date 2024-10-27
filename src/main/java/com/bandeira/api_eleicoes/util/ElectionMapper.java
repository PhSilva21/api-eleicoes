package com.bandeira.api_eleicoes.util;

import com.bandeira.api_eleicoes.dtos.CreateElectionDTO;
import com.bandeira.api_eleicoes.model.Election;
import com.bandeira.api_eleicoes.model.PastElections;

public class ElectionMapper {
    
    public Election toEntity(CreateElectionDTO request){
        return new Election(request.date(), request.uf(), request.electionTurn()
                , request.electionType(), request.sessions());
    }

    public PastElections toPastElections(Election election){
        return new PastElections(election.getDate(), election.getUf(), election.getElectionTurn()
                , election.getElectionType(), election.getSessions(), election.getCandidates()
                , election.getElectedCandidates(), election.getPercentageOfSectionsTotaled()
                , election.getLatestUpdate());
    }
}
