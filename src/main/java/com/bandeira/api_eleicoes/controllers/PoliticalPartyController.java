package com.bandeira.api_eleicoes.controllers;

import com.bandeira.api_eleicoes.dtos.CreatePoliticalPartyDTO;
import com.bandeira.api_eleicoes.dtos.PoliticalPartyResponse;
import com.bandeira.api_eleicoes.model.PoliticalParty;
import com.bandeira.api_eleicoes.services.PoliticalPartyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/political-parties")
public class PoliticalPartyController {

    private final PoliticalPartyService politicalPartyService;

    public PoliticalPartyController(PoliticalPartyService politicalPartyService) {
        this.politicalPartyService = politicalPartyService;
    }

    @Operation(description = "Operação para criar partido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Partido criado com sucesso"),
            @ApiResponse(responseCode = "417", description = "Erro de validação de dados"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/create")
    public ResponseEntity<Void> createPoliticalParty(@RequestBody CreatePoliticalPartyDTO request) {
        politicalPartyService.createPoliticalParty(request);
        return ResponseEntity.ok().build();
    }

    @Operation(description = "Operação para buscar partido por nome")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Partido encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Partido não encontrado"),
            @ApiResponse(responseCode = "417", description = "Erro de validação de dados"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/{name}")
    public ResponseEntity<PoliticalParty> findByName(@PathVariable String name) {
        PoliticalParty response = politicalPartyService.findByName(name);
        return ResponseEntity.ok().body(response);
    }

    @Operation(description = "Operação para deletar partido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Partido deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Partido não encontrado"),
            @ApiResponse(responseCode = "417", description = "Erro de validação de dados"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePoliticalParty(@PathVariable Long id) {
        politicalPartyService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @Operation(description = "Operação para buscar partidos por uf")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Partidos retornados com sucesso"),
            @ApiResponse(responseCode = "417", description = "Erro de validação de dados"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/filter")
    public ResponseEntity<List<PoliticalPartyResponse>> filterByUf(@RequestParam String uf) {
        List<PoliticalPartyResponse> response = politicalPartyService.filterByUf(uf);
        return ResponseEntity.ok().body(response);
    }
}
