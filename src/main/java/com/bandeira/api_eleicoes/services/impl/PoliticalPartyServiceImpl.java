package com.bandeira.api_eleicoes.services.impl;

import com.bandeira.api_eleicoes.dtos.CreatePoliticalPartyDTO;
import com.bandeira.api_eleicoes.dtos.PoliticalPartyResponse;
import com.bandeira.api_eleicoes.exceptions.PoliticalPartyNameAlreadyExists;
import com.bandeira.api_eleicoes.exceptions.PoliticalPartyNotFound;
import com.bandeira.api_eleicoes.model.PoliticalParty;
import com.bandeira.api_eleicoes.repositories.PoliticalPartyRepository;
import com.bandeira.api_eleicoes.services.PoliticalPartyService;
import com.bandeira.api_eleicoes.util.PoliticalPartyMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PoliticalPartyServiceImpl implements PoliticalPartyService{

    private final PoliticalPartyRepository politicalPartyRepository;

    private final PoliticalPartyMapper politicalPartyMapper;

    public PoliticalPartyServiceImpl(PoliticalPartyRepository politicalPartyRepository
            , PoliticalPartyMapper politicalPartyMapper) {
        this.politicalPartyRepository = politicalPartyRepository;
        this.politicalPartyMapper = politicalPartyMapper;
    }

    @Override
    public void createPoliticalParty(CreatePoliticalPartyDTO request){
        validatePoliticalPartyName(request.name());

        var politicalParty = politicalPartyMapper.toEntity(request);

        politicalPartyRepository.save(politicalParty);
    }

    @Override
    public PoliticalParty findByName(String name) {
         return politicalPartyRepository.findByName(name)
                 .orElseThrow(PoliticalPartyNotFound::new);
    }

    @Override
    public void validatePoliticalPartyName(String name){
        if(politicalPartyRepository.findByName(name).isPresent()){
            throw new PoliticalPartyNameAlreadyExists();
        }
    }

    @Override
    public void deleteById(Long id) {
        politicalPartyRepository.findById(id).orElseThrow(PoliticalPartyNotFound::new);
        politicalPartyRepository.deleteById(id);
    }

    @Override
    public List<PoliticalPartyResponse> filterByUf(String uf) {
        return politicalPartyRepository.findAll().stream()
                .filter(p -> p.getUf().equalsIgnoreCase(uf))
                .map(politicalPartyMapper::toDTO)
                .toList();
    }
}
