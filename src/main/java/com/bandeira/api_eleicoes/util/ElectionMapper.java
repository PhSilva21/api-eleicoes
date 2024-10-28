package com.bandeira.api_eleicoes.util;

import com.bandeira.api_eleicoes.dtos.CreateElectionDTO;
import com.bandeira.api_eleicoes.model.Candidate;
import com.bandeira.api_eleicoes.model.Election;
import com.bandeira.api_eleicoes.model.PastElections;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ElectionMapper {
    
    public Election toEntity(CreateElectionDTO request){
        return new Election(request.date(), request.uf(), request.electionTurn()
                , request.electionType(), request.sessions());
    }

    public PastElections toPastElections(Election election) {
        List<Candidate> clonedCandidates = election.getCandidates().stream()
                .map(candidate -> {
                    Candidate newCandidate = new Candidate();
                    newCandidate.setId(candidate.getId());
                    newCandidate.setName(candidate.getName());
                    newCandidate.setPhotoPath(candidate.getPhotoPath());
                    newCandidate.setCandidateRegistration(candidate.getCandidateRegistration());
                    newCandidate.setSituationCandidate(candidate.getSituationCandidate());
                    newCandidate.setPoliticalParty(candidate.getPoliticalParty());
                    newCandidate.setVice(candidate.getVice());
                    newCandidate.setCoalitionAndFederation(candidate.getCoalitionAndFederation());
                    return newCandidate;
                })
                .collect(Collectors.toList());

        List<Candidate> clonedElectedCandidates = election.getElectedCandidates().stream()
                .map(candidate -> {
                    Candidate newCandidate = new Candidate();
                    return newCandidate;
                })
                .collect(Collectors.toList());

        return new PastElections(
                election.getDate(),
                election.getUf(),
                election.getElectionTurn(),
                election.getElectionType(),
                election.getTotalSessions(),
                election.getRemainingSessions(),
                clonedCandidates,
                clonedElectedCandidates,
                election.getPercentageOfSectionsTotaled(),
                election.getLatestUpdate()
        );
    }
}
