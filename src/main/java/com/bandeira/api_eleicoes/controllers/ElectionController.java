package com.bandeira.api_eleicoes.controllers;

import com.bandeira.api_eleicoes.dtos.AddCandidateToElectionDTO;
import com.bandeira.api_eleicoes.dtos.CloseSessionResponseDTO;
import com.bandeira.api_eleicoes.dtos.CreateElectionDTO;
import com.bandeira.api_eleicoes.model.Candidate;
import com.bandeira.api_eleicoes.model.PastElections;
import com.bandeira.api_eleicoes.services.ElectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/election")
public class ElectionController {

    private final ElectionService electionService;

    public ElectionController(ElectionService electionService) {
        this.electionService = electionService;
    }

    @Operation(description = "Operação para criar uma eleição")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Eleição criada com sucesso"),
            @ApiResponse(responseCode = "417", description = "Erro de validação de dados"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/create")
    public ResponseEntity<Void> createElection(@RequestBody @Valid CreateElectionDTO request) {
        electionService.createElection(request);
        return ResponseEntity.ok().build();
    }

    @Operation(description = "Operação para adicionar um candidato a uma eleição")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Candidato adicionado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Candidato não encontrado"),
            @ApiResponse(responseCode = "417", description = "Erro de validação de dados"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/add-candidate")
    public ResponseEntity<List<Candidate>> addCandidateToElection(
            @RequestBody @Valid AddCandidateToElectionDTO request) {
        List<Candidate> response = electionService.addCandidate(request);
        return ResponseEntity.ok().body(response);
    }

    @Operation(description = "Operação para transferir dados de eleição para eleiçoes passadas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dados transferidos com sucesso"),
            @ApiResponse(responseCode = "404", description = "Eleição não encontrada"),
            @ApiResponse(responseCode = "417", description = "Erro de validação de dados"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/toPast/{id}")
    public ResponseEntity<PastElections> toPastElection(@PathVariable Long id) {
        PastElections response = electionService.toPastElection(id);
        return ResponseEntity.ok().body(response);
    }

    @Operation(description = "Operação para encerrar uma seção")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Seção encerrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Eleição não encontrado"),
            @ApiResponse(responseCode = "417", description = "Erro de validação de dados"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/close-session/{id}")
    public ResponseEntity<CloseSessionResponseDTO> closeSession(@PathVariable Long id) {
        CloseSessionResponseDTO response = electionService.closeSession(id);
        return ResponseEntity.ok().body(response);
    }

    @Operation(description = "Operação para buscar candidatos eleitos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Candidatos retornados com sucesso"),
            @ApiResponse(responseCode = "417", description = "Erro de validação de dados"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/elected-candidates/{id}")
    public ResponseEntity<List<Candidate>> getElectedCandidates(@PathVariable Long id) {
        List<Candidate> response = electionService.getElectedCandidates(id);
        return ResponseEntity.ok().body(response);
    }

    @Operation(description = "Operação para buscar todos os candidatos de uma eleição")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Candidatos retornados com sucesso"),
            @ApiResponse(responseCode = "417", description = "Erro de validação de dados"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/all-candidates/{id}")
    public ResponseEntity<List<Candidate>> getAllCandidates(@PathVariable Long id) {
        List<Candidate> response = electionService.getAllCandidates(id);
        return ResponseEntity.ok().body(response);
    }
}
