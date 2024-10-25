package com.bandeira.api_eleicoes.model;


import com.bandeira.api_eleicoes.model.enums.ElectionType;
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
@Table(name = "tb_pats_elections")
public class PastElections {

    @Id
    @GeneratedValue
    private Long id;

    private LocalDate date;

    private ElectionType electionType;

    private Long sessions;

    private List<Candidate> electedCandidates = new ArrayList<>();

    private Long percentageOfSessionsAccountedFor;
}
