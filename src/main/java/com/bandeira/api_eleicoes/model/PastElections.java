package com.bandeira.api_eleicoes.model;


import com.bandeira.api_eleicoes.model.enums.ElectionTurn;
import com.bandeira.api_eleicoes.model.enums.ElectionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "tb_past_elections")
@Entity
public class PastElections {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    private String uf;

    private ElectionTurn electionTurn;

    private ElectionType electionType;

    private Double totalSessions;

    private Double remainingSessions;

    @OneToMany(mappedBy = "pastElections")
    private List<Candidate> candidates = new ArrayList<>();

    @ElementCollection
    private List<Candidate> electedCandidates = new ArrayList<>();

    private Double percentageOfSectionsTotaled;

    private LocalDateTime latestUpdate;

    public PastElections(LocalDate date, String uf, ElectionTurn electionTurn
            , ElectionType electionType, Double totalSessions,Double remainingSessions
            , List<Candidate> candidates, List<Candidate> electedCandidates
            ,Double percentageOfSectionsTotaled, LocalDateTime latestUpdate) {
        this.date = date;
        this.uf = uf;
        this.electionTurn = electionTurn;
        this.electionType = electionType;
        this.totalSessions = totalSessions;
        this.remainingSessions = remainingSessions;
        this.candidates = candidates;
        this.electedCandidates = electedCandidates;
        this.percentageOfSectionsTotaled = percentageOfSectionsTotaled;
        this.latestUpdate = latestUpdate;
    }
}
