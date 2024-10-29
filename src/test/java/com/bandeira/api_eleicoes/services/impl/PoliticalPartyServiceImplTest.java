package com.bandeira.api_eleicoes.services.impl;

import com.bandeira.api_eleicoes.dtos.CreatePoliticalPartyDTO;
import com.bandeira.api_eleicoes.dtos.PoliticalPartyResponse;
import com.bandeira.api_eleicoes.exceptions.CandidateNotFoundException;
import com.bandeira.api_eleicoes.exceptions.PoliticalPartyNameAlreadyExists;
import com.bandeira.api_eleicoes.exceptions.PoliticalPartyNotFound;
import com.bandeira.api_eleicoes.model.Candidate;
import com.bandeira.api_eleicoes.model.PoliticalParty;
import com.bandeira.api_eleicoes.model.enums.SituationCandidate;
import com.bandeira.api_eleicoes.repositories.PoliticalPartyRepository;
import com.bandeira.api_eleicoes.util.PoliticalPartyMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PoliticalPartyServiceImplTest {

    @InjectMocks
    PoliticalPartyServiceImpl politicalPartyService;

    @Mock
    PoliticalPartyRepository politicalPartyRepository;

    @Mock
    PoliticalPartyMapper politicalPartyMapper;

    @Captor
    ArgumentCaptor<PoliticalParty> politicalPartyArgumentCaptor;

    @Nested
    @DisplayName("Create Political Party Tests")
    class CreatePoliticalParty {

        PoliticalParty politicalParty = new PoliticalParty("Partido union", "SP");
        CreatePoliticalPartyDTO createPoliticalPartyDTO = new CreatePoliticalPartyDTO("Partido union", "SP");

        @Test
        @DisplayName("Should create a new political party successfully")
        void createPoliticalParty() {
            doReturn(Optional.empty()).when(politicalPartyRepository).findByName(createPoliticalPartyDTO.name());
            doReturn(politicalParty).when(politicalPartyMapper).toEntity(createPoliticalPartyDTO);
            doReturn(politicalParty).when(politicalPartyRepository).save(politicalPartyArgumentCaptor.capture());

            politicalPartyService.createPoliticalParty(createPoliticalPartyDTO);

            var politicalPartyCaptured = politicalPartyArgumentCaptor.getValue();

            assertEquals(createPoliticalPartyDTO.name(), politicalPartyCaptured.getName());
            assertEquals(createPoliticalPartyDTO.uf(), politicalPartyCaptured.getUf());

            verify(politicalPartyRepository, times(1)).findByName(createPoliticalPartyDTO.name());
            verify(politicalPartyMapper, times(1)).toEntity(createPoliticalPartyDTO);
            verify(politicalPartyRepository, times(1)).save(politicalParty);
        }

        @Test
        @DisplayName("Should throw exception when political party name already exists")
        void erroName() {
            doThrow(PoliticalPartyNameAlreadyExists.class).when(politicalPartyRepository).findByName(createPoliticalPartyDTO.name());

            assertThrows(PoliticalPartyNameAlreadyExists.class, () -> politicalPartyService.createPoliticalParty(createPoliticalPartyDTO));

            verify(politicalPartyRepository, times(1)).findByName(createPoliticalPartyDTO.name());
            verify(politicalPartyMapper, times(0)).toEntity(createPoliticalPartyDTO);
            verify(politicalPartyRepository, times(0)).save(politicalParty);
        }
    }

    @Nested
    @DisplayName("Find Political Party By Name Tests")
    class FindByName {

        PoliticalParty politicalParty = new PoliticalParty("Partido Exemplo", "SP");

        @Test
        @DisplayName("Should find political party by name successfully")
        void shouldFindCandidateByNAmeSuccessfully() {
            doReturn(Optional.of(politicalParty)).when(politicalPartyRepository).findByName(politicalParty.getName());

            var response = politicalPartyService.findByName(politicalParty.getName());

            assertNotNull(response);
        }

        @Test
        @DisplayName("Should throw PoliticalPartyNotFound when political party not found by name")
        void shouldThrowCandidateNotFoundExceptionWhenCandidateNotFoundByName() {
            doReturn(Optional.empty()).when(politicalPartyRepository).findByName(politicalParty.getName());

            assertThrows(PoliticalPartyNotFound.class, () -> politicalPartyService.findByName(politicalParty.getName()));
        }
    }

    @Nested
    @DisplayName("Validate Political Party Name Tests")
    class ValidatePoliticalPartyName {

        PoliticalParty politicalParty = new PoliticalParty("Partido Exemplo", "SP");

        @Test
        @DisplayName("Should validate that political party name does not exist")
        void validatePoliticalPartyName() {
            doReturn(Optional.empty()).when(politicalPartyRepository).findByName(politicalParty.getName());

            politicalPartyService.validatePoliticalPartyName(politicalParty.getName());
        }

        @Test
        @DisplayName("Should throw exception when political party name already exists")
        void erroValidate() {
            doReturn(Optional.of(politicalParty)).when(politicalPartyRepository).findByName(politicalParty.getName());

            assertThrows(PoliticalPartyNameAlreadyExists.class, () -> politicalPartyService.validatePoliticalPartyName(politicalParty.getName()));
        }
    }

    @Nested
    @DisplayName("Delete Political Party By ID Tests")
    class DeleteById {

        PoliticalParty politicalParty = new PoliticalParty("Partido Exemplo", "SP");

        @Test
        @DisplayName("Should delete political party by ID successfully")
        void shouldDeleteCandidateByIdSuccessfully() {
            doReturn(Optional.of(politicalParty)).when(politicalPartyRepository).findById(politicalParty.getId());
            doNothing().when(politicalPartyRepository).deleteById(politicalParty.getId());

            politicalPartyService.deleteById(politicalParty.getId());

            verify(politicalPartyRepository, times(1)).findById(politicalParty.getId());
            verify(politicalPartyRepository, times(1)).deleteById(politicalParty.getId());
        }

        @Test
        @DisplayName("Should throw PoliticalPartyNotFound when attempting to delete non-existent ID")
        void ShouldThrowCandidateNotFoundExceptionWhenCandidateNotFoundToDeleteByID() {
            doReturn(Optional.empty()).when(politicalPartyRepository).findById(politicalParty.getId());

            assertThrows(PoliticalPartyNotFound.class, () -> politicalPartyService.deleteById(politicalParty.getId()));

            verify(politicalPartyRepository, times(1)).findById(politicalParty.getId());
            verify(politicalPartyRepository, times(0)).deleteById(politicalParty.getId());
        }
    }

    @Nested
    @DisplayName("Filter Political Party By UF Tests")
    class FilterByUf {

        PoliticalParty politicalParty = new PoliticalParty("Partido Exemplo", "SP");

        PoliticalPartyResponse politicalPartyResponse = new PoliticalPartyResponse(
                "Nome do Partido", "SP", List.of(new Candidate("Nome do Candidato", "123456", SituationCandidate.FIRST_TURN, new PoliticalParty("Nome do Partido", "SP"), "Nome do Vice", "Coalizão e Federação"))
        );

        @Test
        @DisplayName("Should filter political party by UF successfully")
        void filterByUf() {
            var list = List.of(politicalParty);
            var listMapper = List.of(politicalPartyResponse);
            doReturn(list).when(politicalPartyRepository).findAll();
            doReturn(politicalPartyResponse).when(politicalPartyMapper).toDTO(politicalParty);

            var response = politicalPartyService.filterByUf(politicalParty.getUf());

            assertEquals(response.size(), listMapper.size());
        }
    }
}
