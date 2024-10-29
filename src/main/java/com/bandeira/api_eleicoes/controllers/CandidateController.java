package com.bandeira.api_eleicoes.controllers;

import com.bandeira.api_eleicoes.dtos.CreateCandidateDTO;
import com.bandeira.api_eleicoes.dtos.CreateCandidateResponse;
import com.bandeira.api_eleicoes.dtos.UpdateCandidateDTO;
import com.bandeira.api_eleicoes.model.Candidate;
import com.bandeira.api_eleicoes.services.CandidateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/candidate")
public class CandidateController {

    private final CandidateService candidateService;

    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @Operation(description = "Operação para criar um candidadto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Candidato criado com sucesso"),
            @ApiResponse(responseCode = "417", description = "Erro de validação de dados"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/create")
    public ResponseEntity<CreateCandidateResponse> createCandidate(
            @ModelAttribute @Valid CreateCandidateDTO request,
            @RequestParam("file") MultipartFile file) throws Exception {
        var response = candidateService.createCandidate(request, file);
        return ResponseEntity.ok().body(response);
    }

    @Operation(description = "Operação para buscar candidato por id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Candidato encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Candidato não encontrado"),
            @ApiResponse(responseCode = "417", description = "Erro de validação de dados"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<Candidate> getCandidateById(@PathVariable Long id) {
        Candidate response = candidateService.findById(id);
        return ResponseEntity.ok().body(response);
    }

    @Operation(description = "Operação para buscar candidato por nome")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Candidato encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Candidato não encontrado"),
            @ApiResponse(responseCode = "417", description = "Erro de validação de dados"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/find-by-name")
    public ResponseEntity<Candidate> getCandidateByName(
            @RequestParam @Param("name") String name) {
        Candidate response = candidateService.findByName(name);
        return ResponseEntity.ok().body(response);
    }

    @Operation(description = "Operação para atualizar candidato")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Candidato atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Candidato não encontrado"),
            @ApiResponse(responseCode = "417", description = "Erro de validação de dados"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PutMapping("/update")
    public ResponseEntity<Void> updateCandidate(@RequestBody @Valid UpdateCandidateDTO request,
                                                @RequestParam("file") MultipartFile file) throws Exception {
        candidateService.updateCandidate(request, file);
        return ResponseEntity.ok().build();
    }

    @Operation(description = "Operação para deletar candidato")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Candidato deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Candidato não encontrado"),
            @ApiResponse(responseCode = "417", description = "Erro de validação de dados"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCandidate(@PathVariable Long id) {
        candidateService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
