package com.bandeira.api_eleicoes.model;


import com.bandeira.api_eleicoes.model.enums.ElectionTurn;
import com.bandeira.api_eleicoes.model.enums.ElectionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Table(name = "tb_pats_elections")
@Entity
public class PastElections {

    @Id
    @GeneratedValue
    private Long id;

    private LocalDate date;

    private String uf;

    private ElectionTurn electionTurn;

    private ElectionType electionType;

    private Long sessions;

    @OneToMany(mappedBy = "pastElections")
    private List<Candidate> candidates = new ArrayList<>();

    @ElementCollection
    private List<Candidate> electedCandidates = new ArrayList<>();

    private Integer percentageOfSectionsTotaled;

    private LocalDateTime latestUpdate;

    public PastElections(LocalDate date, String uf, ElectionTurn electionTurn
            , ElectionType electionType, Long sessions, List<Candidate> candidates
            , List<Candidate> electedCandidates,Integer percentageOfSectionsTotaled
            , LocalDateTime latestUpdate) {
        this.date = date;
        this.uf = uf;
        this.electionTurn = electionTurn;
        this.electionType = electionType;
        this.sessions = sessions;
        this.candidates = candidates;
        this.electedCandidates = electedCandidates;
        this.percentageOfSectionsTotaled = percentageOfSectionsTotaled;
        this.latestUpdate = latestUpdate;
    }
}
