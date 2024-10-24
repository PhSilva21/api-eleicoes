package com.bandeira.api_eleicoes.model;


import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Table(name = "tb_elections")
public class Election {

    @Id
    @GeneratedValue
    private Long id;

    private LocalDate date;

    private Long sessions;

    private List<Candidate> electedCandidates = new ArrayList<>();

    private Long percentageOfSessionsAccountedFor;

}
