package com.bandeira.api_eleicoes.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "tb_political_parties")
@Entity
public class PoliticalParty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    String name;

    @OneToMany(mappedBy = "politicalParty")
    private List<Candidate> candidates =  new ArrayList<>();

    private String uf;

    public PoliticalParty(String name, String uf) {
        this.name = name;
        this.uf = uf;
    }
}
