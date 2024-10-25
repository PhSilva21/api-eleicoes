package com.bandeira.api_eleicoes.model;

import com.bandeira.api_eleicoes.model.enums.SituationCandidate;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Table(name = "tb_candidates")
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String voucherFile;

    private String candidateRegistration;

    private SituationCandidate situationCandidate;

    private String vice;

    private PoliticalParty politicalParty;

    private String coalitionAndFederation;

    public Candidate(String name, SituationCandidate situationCandidate
            , String vice, String coalitionAndFederation) {
        this.name = name;
        this.situationCandidate = situationCandidate;
        this.vice = vice;
        this.candidateRegistration = coalitionAndFederation;
    }
}
