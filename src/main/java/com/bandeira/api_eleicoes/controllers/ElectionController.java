package com.bandeira.api_eleicoes.controllers;

import com.bandeira.api_eleicoes.dtos.AddCandidateToElectionDTO;
import com.bandeira.api_eleicoes.dtos.CloseSessionResponseDTO;
import com.bandeira.api_eleicoes.dtos.CreateElectionDTO;
import com.bandeira.api_eleicoes.dtos.FilterElectionByUfAndTurn;
import com.bandeira.api_eleicoes.model.Candidate;
import com.bandeira.api_eleicoes.model.Election;
import com.bandeira.api_eleicoes.model.PastElections;
import com.bandeira.api_eleicoes.services.ElectionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/elections")
public class ElectionController {

    private final ElectionService electionService;

    public ElectionController(ElectionService electionService) {
        this.electionService = electionService;
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createElection(@RequestBody CreateElectionDTO request) {
        electionService.createElection(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/add-candidate")
    public ResponseEntity<List<Candidate>> addCandidateToElection(@RequestBody AddCandidateToElectionDTO request) {
        List<Candidate> response = electionService.addCandidate(request);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/toPast/{id}")
    public ResponseEntity<PastElections> toPastElection(@PathVariable Long id) {
        PastElections response = electionService.toPastElection(id);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/close-session/{id}")
    public ResponseEntity<CloseSessionResponseDTO> closeSession(@PathVariable Long id) {
        CloseSessionResponseDTO response = electionService.closeSession(id);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/filter")
    public ResponseEntity<Election> findElectionByUfAndTurn(@RequestBody FilterElectionByUfAndTurn request) {
        Optional<Election> response = electionService.findByUfAndTurn(request);
        return response.map(ResponseEntity::ok)
                       .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/elected-candidates/{id}")
    public ResponseEntity<List<Candidate>> getElectedCandidates(@PathVariable Long id) {
        List<Candidate> response = electionService.getElectedCandidates(id);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/all-candidates/{id}")
    public ResponseEntity<List<Candidate>> getAllCandidates(@PathVariable Long id) {
        List<Candidate> response = electionService.getAllCandidates(id);
        return ResponseEntity.ok().body(response);
    }
}
