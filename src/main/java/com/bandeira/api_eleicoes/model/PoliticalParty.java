package com.bandeira.api_eleicoes.model;


import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Table(name = "tb_political_parties")
public class PoliticalParty {

    private Long id;

    private List<Candidate> candidates =  new ArrayList<>();

    private String uf;
}
