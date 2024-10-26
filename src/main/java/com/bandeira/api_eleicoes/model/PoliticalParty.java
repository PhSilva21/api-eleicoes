package com.bandeira.api_eleicoes.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Table(name = "tb_political_parties")
@Entity
public class PoliticalParty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @OneToMany(mappedBy = "politicalParty")
    private List<Candidate> candidates =  new ArrayList<>();

    private String uf;
}
