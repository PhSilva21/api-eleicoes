package com.bandeira.api_eleicoes.model;


import com.bandeira.api_eleicoes.model.enums.ElectionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
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

    private ElectionType electionType;

    private Long sessions;

    @OneToMany(mappedBy = "pastElections")
    private List<Candidate> electedCandidates = new ArrayList<>();

    private Long percentageOfSessionsAccountedFor;
}
