package com.bandeira.api_eleicoes.dtos;

import com.bandeira.api_eleicoes.model.Candidate;

import java.util.List;
import java.util.Map;

public record PoliticalPartyResponse(

        String name,

        String uf,

        List<Candidate> candidates
) {
}
