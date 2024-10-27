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
@Table(name = "tb_elections")
@Entity
public class Election {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    private String uf;

    private ElectionTurn electionTurn;

    private ElectionType electionType;

    private Double totalSessions;

    private Double remainingSessions;

    @OneToMany(mappedBy = "election")
    private List<Candidate> candidates = new ArrayList<>();

    @ElementCollection
    private List<Candidate> electedCandidates = new ArrayList<>();

    private Double percentageOfSectionsTotaled;

    private LocalDateTime latestUpdate;

    public Election(LocalDate date, String uf, ElectionTurn electionTurn
            , ElectionType electionType,Double totalSessions) {
        this.date = date;
        this.uf = uf;
        this.electionType = electionType;
        this.electionTurn = electionTurn;
        this.totalSessions = totalSessions;
        this.remainingSessions = totalSessions;
        this.percentageOfSectionsTotaled = 0.0;
    }

    public void decrementSession() {
        this.remainingSessions -= 1;
    }
}
