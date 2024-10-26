package com.bandeira.api_eleicoes.model;

import com.bandeira.api_eleicoes.model.enums.SituationCandidate;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Table(name = "tb_candidates")
@Entity
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String photoPath;

    private String voucherFile;

    private String candidateRegistration;

    private SituationCandidate situationCandidate;

    private String vice;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "political_party_id")
    private PoliticalParty politicalParty;

    private String coalitionAndFederation;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "election_id")
    private Election election;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "past_elections_id")
    private PastElections pastElections;

    public Candidate(String name, SituationCandidate situationCandidate
            , String vice, String coalitionAndFederation) {
        this.name = name;
        this.situationCandidate = situationCandidate;
        this.vice = vice;
        this.candidateRegistration = coalitionAndFederation;
    }
}
