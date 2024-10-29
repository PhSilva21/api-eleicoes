package com.bandeira.api_eleicoes.controllers;

import com.bandeira.api_eleicoes.dtos.CreateCandidateDTO;
import com.bandeira.api_eleicoes.dtos.CreateCandidateResponse;
import com.bandeira.api_eleicoes.dtos.UpdateCandidateDTO;
import com.bandeira.api_eleicoes.model.Candidate;
import com.bandeira.api_eleicoes.services.CandidateService;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/candidates")
public class CandidateController {

    private final CandidateService candidateService;

    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @PostMapping("/create")
    public ResponseEntity<CreateCandidateResponse> createCandidate(
            @ModelAttribute CreateCandidateDTO request,
            @RequestParam("file") MultipartFile file) throws Exception {
        var response = candidateService.createCandidate(request, file);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<Candidate> getCandidateById(@PathVariable Long id) {
        Candidate response = candidateService.findById(id);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/find-by-name")
    public ResponseEntity<Candidate> getCandidateByName(
            @RequestParam @Param("name") String name) {
        Candidate response = candidateService.findByName(name);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/update")
    public ResponseEntity<Void> updateCandidate(@RequestBody UpdateCandidateDTO request,
                                                @RequestParam("file") MultipartFile file) throws Exception {
        candidateService.updateCandidate(request, file);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCandidate(@PathVariable Long id) {
        candidateService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
