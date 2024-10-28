package com.bandeira.api_eleicoes.controllers;

import com.bandeira.api_eleicoes.dtos.CreatePoliticalPartyDTO;
import com.bandeira.api_eleicoes.dtos.PoliticalPartyResponse;
import com.bandeira.api_eleicoes.model.PoliticalParty;
import com.bandeira.api_eleicoes.services.PoliticalPartyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/political-parties")
public class PoliticalPartyController {

    private final PoliticalPartyService politicalPartyService;

    public PoliticalPartyController(PoliticalPartyService politicalPartyService) {
        this.politicalPartyService = politicalPartyService;
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createPoliticalParty(@RequestBody CreatePoliticalPartyDTO request) {
        politicalPartyService.createPoliticalParty(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{name}")
    public ResponseEntity<PoliticalParty> findByName(@PathVariable String name) {
        PoliticalParty response = politicalPartyService.findByName(name);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePoliticalParty(@PathVariable Long id) {
        politicalPartyService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/filter")
    public ResponseEntity<List<PoliticalPartyResponse>> filterByUf(@RequestParam String uf) {
        List<PoliticalPartyResponse> response = politicalPartyService.filterByUf(uf);
        return ResponseEntity.ok().body(response);
    }
}
