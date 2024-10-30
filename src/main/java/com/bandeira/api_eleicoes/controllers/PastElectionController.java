package com.bandeira.api_eleicoes.controllers;

import com.bandeira.api_eleicoes.dtos.FindElectionByUfAndYear;
import com.bandeira.api_eleicoes.dtos.UpdatePastElectionDTO;
import com.bandeira.api_eleicoes.model.Candidate;
import com.bandeira.api_eleicoes.model.PastElections;
import com.bandeira.api_eleicoes.services.PastElectionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/past-election")
public class PastElectionController {

    private final PastElectionService pastElectionService;

    public PastElectionController(PastElectionService pastElectionService) {
        this.pastElectionService = pastElectionService;
    }

    @GetMapping("/find-by-uf-age")
    public ResponseEntity<Optional<PastElections>> findByUfAndYear(
            @RequestBody FindElectionByUfAndYear request){
        var response = pastElectionService.findByUfAndYear(request);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/delete-by-id")
    public ResponseEntity<Void> deleteById(Long id){
        pastElectionService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/update")
    public ResponseEntity<Void> update(@RequestBody UpdatePastElectionDTO request){
        pastElectionService.updatePastElection(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/remove-candidate")
    public ResponseEntity<List<Candidate>> removeCandidate(@RequestBody Long id, String candidateRegistration){
        var response = pastElectionService.removeCandidateElected(id, candidateRegistration);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/add-candidate")
    public ResponseEntity<List<Candidate>> addCandidate(Long id, String candidateRegistration){
        var response = pastElectionService.addCandidateElected(id, candidateRegistration);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/find-by-id")
    public ResponseEntity<PastElections> findById(@RequestBody Long id){
        var response = pastElectionService.findById(id);
        return ResponseEntity.ok().body(response);
    }
}
