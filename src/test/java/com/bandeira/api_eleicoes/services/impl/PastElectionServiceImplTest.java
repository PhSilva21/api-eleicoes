package com.bandeira.api_eleicoes.services.impl;

import com.bandeira.api_eleicoes.dtos.FindElectionByUfAndYear;
import com.bandeira.api_eleicoes.dtos.UpdateCandidateDTO;
import com.bandeira.api_eleicoes.dtos.UpdatePastElectionDTO;
import com.bandeira.api_eleicoes.dtos.UploadResponse;
import com.bandeira.api_eleicoes.exceptions.CandidateNotFoundException;
import com.bandeira.api_eleicoes.exceptions.PastElectionNotFoundException;
import com.bandeira.api_eleicoes.model.Candidate;
import com.bandeira.api_eleicoes.model.PastElections;
import com.bandeira.api_eleicoes.model.PoliticalParty;
import com.bandeira.api_eleicoes.model.enums.ElectionTurn;
import com.bandeira.api_eleicoes.model.enums.ElectionType;
import com.bandeira.api_eleicoes.model.enums.SituationCandidate;
import com.bandeira.api_eleicoes.repositories.PastElectionsRepository;
import com.bandeira.api_eleicoes.services.CandidateService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class PastElectionServiceImplTest {

    @InjectMocks
    PastElectionServiceImpl pastElectionService;

    @Mock
    PastElectionsRepository pastElectionsRepository;

    @Mock
    CandidateService candidateService;

    @Captor
    ArgumentCaptor<PastElections> pastElectionsArgumentCaptor;

    @Nested
    class FindByUfAndYear {

        PoliticalParty politicalParty = new PoliticalParty("Partido Exemplo", "SP");

        Candidate candidate = new Candidate("Nome do Candidato", "123456", SituationCandidate.FIRST_TURN,
                politicalParty, "Nome do Vice", "Coalizão e Federação Exemplo");

        PastElections pastElections = new PastElections(LocalDate.of(2017, 2, 27)
                , "RS", ElectionTurn.FIRST_TURN, ElectionType.MUNICIPAL_ELECTION, 100.0
                , 50.0, List.of(candidate), List.of(candidate)
                , 75.0, LocalDateTime.now());

        FindElectionByUfAndYear findByUfAndYear = new FindElectionByUfAndYear("RS", 2017);

        @Test
        @DisplayName("Should find election by UF and year successfully")
        void shouldFindElectionByUfAndYearSuccessfully() {
            var electionList = List.of(pastElections);
            doReturn(electionList)
                    .when(pastElectionsRepository).findAll();

            var response = pastElectionService.findByUfAndYear(findByUfAndYear);

            assertNotNull(response);
        }

        @Test
        @DisplayName("Should throw PastElectionNotFoundException when election not found by UF and year")
        void shouldThrowPastElectionNotFoundExceptionWhenElectionNotFoundByUfAndYear(){
            var electionList = List.of(pastElections);
            pastElections.setUf("SP");
            doReturn(electionList)
                    .when(pastElectionsRepository).findAll();

            assertThrows(PastElectionNotFoundException.class,
                    () -> pastElectionService.findByUfAndYear(findByUfAndYear));
        }
    }

    @Nested
    class DeleteById {

        PoliticalParty politicalParty = new PoliticalParty("Partido Exemplo", "SP");

        Candidate candidate = new Candidate("Nome do Candidato", "123456", SituationCandidate.FIRST_TURN,
                politicalParty, "Nome do Vice", "Coalizão e Federação Exemplo");

        PastElections pastElections = new PastElections(LocalDate.of(2017, 2, 27)
                , "RS", ElectionTurn.FIRST_TURN, ElectionType.MUNICIPAL_ELECTION, 100.0
                , 50.0, List.of(candidate), List.of(candidate)
                , 75.0, LocalDateTime.now());

        @Test
        @DisplayName("Should delete candidate by ID successfully")
        void shouldDeletePastElectionByIdSuccessfully() {
            doReturn(Optional.of(pastElections))
                    .when(pastElectionsRepository).findById(pastElections.getId());
            doNothing()
                    .when(pastElectionsRepository)
                    .deleteById(pastElections.getId());

            pastElectionService.deleteById(candidate.getId());

            verify(pastElectionsRepository, times(1))
                    .findById(pastElections.getId());
            verify(pastElectionsRepository, times(1))
                    .deleteById(pastElections.getId());
        }

        @Test
        @DisplayName("Should throw CandidateNotFoundException when candidate not found to delete by ID")
        void ShouldThrowPastElectionNotFoundExceptionWhenCandidateNotFoundToDeleteByID() {
            doReturn(Optional.empty())
                    .when(pastElectionsRepository)
                    .findById(pastElections.getId());

            assertThrows(PastElectionNotFoundException.class,
                    () -> pastElectionService.deleteById(candidate.getId()));

            verify(pastElectionsRepository, times(1))
                    .findById(candidate.getId());
            verify(pastElectionsRepository, times(0))
                    .deleteById(pastElections.getId());
        }
    }

    @Nested
    class Update {

        UpdatePastElectionDTO updatePastElectionDTO = new UpdatePastElectionDTO(1L
                , LocalDate.of(2022, 10, 2), "SP", ElectionTurn.FIRST_TURN
                , ElectionType.MUNICIPAL_ELECTION, 10000.0, 500.0
                , 95.0, LocalDateTime.now());

        PoliticalParty politicalParty = new PoliticalParty("Partido Exemplo", "SP");

        Candidate candidate = new Candidate("Nome do Candidato", "123456", SituationCandidate.FIRST_TURN,
                politicalParty, "Nome do Vice", "Coalizão e Federação Exemplo");

        PastElections pastElections = new PastElections(LocalDate.of(2017, 2, 27)
                , "RS", ElectionTurn.FIRST_TURN, ElectionType.MUNICIPAL_ELECTION, 100.0
                , 50.0, List.of(candidate), List.of(candidate)
                , 75.0, LocalDateTime.now());

        @Test
        @DisplayName("Should update past election successfully")
        void shouldUpdatePastElectionSuccessfully() {
            doReturn(Optional.of(pastElections))
                    .when(pastElectionsRepository).findById(updatePastElectionDTO.pastElectionID());
            doReturn(pastElections)
                    .when(pastElectionsRepository).save(pastElectionsArgumentCaptor.capture());

            pastElectionService.updatePastElection(updatePastElectionDTO);

            var electionCaptured = pastElectionsArgumentCaptor.getValue();

            assertEquals(updatePastElectionDTO.date(), electionCaptured.getDate());
            assertEquals(updatePastElectionDTO.uf(), electionCaptured.getUf());
            assertEquals(updatePastElectionDTO.electionTurn(), electionCaptured.getElectionTurn());
            assertEquals(updatePastElectionDTO.electionType(), electionCaptured.getElectionType());
            assertEquals(updatePastElectionDTO.totalSessions(), electionCaptured.getTotalSessions());
            assertEquals(updatePastElectionDTO.remainingSessions(),
                    electionCaptured.getRemainingSessions());
            assertEquals(updatePastElectionDTO.percentageOfSectionsTotaled(),
                    electionCaptured.getPercentageOfSectionsTotaled());
            assertEquals(updatePastElectionDTO.latestUpdate(), electionCaptured.getLatestUpdate());

            verify(pastElectionsRepository, times(1))
                    .findById(updatePastElectionDTO.pastElectionID());
            verify(pastElectionsRepository, times(1))
                    .save(pastElections);
        }

        @Test
        @DisplayName("Should throw exception when file error occurs during update")
        void shouldThrowExceptionWhenFileErrorOccursDuringUpdate() throws Exception {
            doReturn(Optional.empty())
                    .when(pastElectionsRepository).findById(updatePastElectionDTO.pastElectionID());

            assertThrows(PastElectionNotFoundException.class,
                    () -> pastElectionService.updatePastElection(updatePastElectionDTO));

            verify(pastElectionsRepository, times(1))
                    .findById(updatePastElectionDTO.pastElectionID());
            verify(pastElectionsRepository, times(0))
                    .save(pastElections);
        }

    }

    @Nested
    class RemoveCandidateElected {

        PoliticalParty politicalParty = new PoliticalParty("Partido Exemplo", "SP");

        Candidate candidate = new Candidate("Nome do Candidato", "123456"
                , SituationCandidate.FIRST_TURN, politicalParty, "Nome do Vice"
                , "Coalizão e Federação Exemplo");

        Candidate candidate2 = new Candidate("Nome do Candidato", "ges52g", SituationCandidate.FIRST_TURN,
                politicalParty, "Nome do Vice", "Coalizão e Federação Exemplo");

        PastElections pastElections = new PastElections(LocalDate.of(2017, 2, 27)
                , "RS", ElectionTurn.FIRST_TURN, ElectionType.MUNICIPAL_ELECTION, 100.0
                , 50.0, List.of(candidate), List.of(candidate, candidate2)
                , 75.0, LocalDateTime.now());

        @Test
        @DisplayName("Should remove elected candidate successfully")
        void shouldRemoveElectedCandidateSuccessfully() {
            var list = List.of(candidate);
            doReturn(Optional.of(pastElections))
                    .when(pastElectionsRepository).findById(pastElections.getId());
            doReturn(candidate).when(candidateService)
                    .findByCandidateRegistration(candidate.getCandidateRegistration());

            var response = pastElectionService.removeCandidateElected(
                    pastElections.getId(), candidate.getCandidateRegistration());

            assertEquals(response.size(), list.size());

            verify(pastElectionsRepository, times(1))
                    .findById(pastElections.getId());
            verify(candidateService, times(1))
                    .findByCandidateRegistration(candidate.getCandidateRegistration());
            verify(pastElectionsRepository, times(1))
                    .save(pastElections);
        }

        @Test
        @DisplayName("Should throw PastElectionNotFoundException when election not found to remove candidate")
        void shouldThrowPastElectionNotFoundExceptionWhenElectionNotFoundToRemoveCandidate(){
            doReturn(Optional.empty())
                    .when(pastElectionsRepository).findById(pastElections.getId());

            assertThrows(PastElectionNotFoundException.class,
                    () -> pastElectionService.removeCandidateElected(
                            pastElections.getId(), candidate.getCandidateRegistration()));

            verify(pastElectionsRepository, times(1))
                    .findById(pastElections.getId());
            verify(candidateService, times(0))
                    .findByCandidateRegistration(candidate.getCandidateRegistration());
            verify(pastElectionsRepository, times(0))
                    .save(pastElections);
        }

        @Test
        @DisplayName("Should throw CandidateNotFoundException when candidate not found to remove")
        void shouldThrowCandidateNotFoundExceptionWhenCandidateNotFoundToRemove(){
            doReturn(Optional.of(pastElections))
                    .when(pastElectionsRepository).findById(pastElections.getId());
            doThrow(CandidateNotFoundException.class).when(candidateService)
                    .findByCandidateRegistration(candidate.getCandidateRegistration());

            assertThrows(CandidateNotFoundException.class,
                    () -> pastElectionService.removeCandidateElected(
                            pastElections.getId(), candidate.getCandidateRegistration()));

            verify(pastElectionsRepository, times(1))
                    .findById(pastElections.getId());
            verify(candidateService, times(1))
                    .findByCandidateRegistration(candidate.getCandidateRegistration());
            verify(pastElectionsRepository, times(0))
                    .save(pastElections);
        }
    }

    @Nested
    class AddCandidate {

        PoliticalParty politicalParty = new PoliticalParty("Partido Exemplo", "SP");

        Candidate candidate = new Candidate("Nome do Candidato", "123456"
                , SituationCandidate.FIRST_TURN, politicalParty, "Nome do Vice"
                , "Coalizão e Federação Exemplo");

        Candidate candidate2 = new Candidate("Nome do Candidato", "ges52g", SituationCandidate.FIRST_TURN,
                politicalParty, "Nome do Vice", "Coalizão e Federação Exemplo");

        PastElections pastElections = new PastElections(LocalDate.of(2017, 2, 27)
                , "RS", ElectionTurn.FIRST_TURN, ElectionType.MUNICIPAL_ELECTION, 100.0
                , 50.0, List.of(candidate), List.of(candidate)
                , 75.0, LocalDateTime.now());

        @Test
        @DisplayName("Should add candidate successfully")
        void shouldAddCandidateSuccessfully() {
            var list = List.of(candidate, candidate2);
            doReturn(Optional.of(pastElections))
                    .when(pastElectionsRepository).findById(pastElections.getId());
            doReturn(candidate).when(candidateService)
                    .findByCandidateRegistration(candidate.getCandidateRegistration());

            var response = pastElectionService.addCandidateElected(
                    pastElections.getId(), candidate.getCandidateRegistration());

            assertEquals(response.size(), list.size());

            verify(pastElectionsRepository, times(1))
                    .findById(pastElections.getId());
            verify(candidateService, times(1))
                    .findByCandidateRegistration(candidate.getCandidateRegistration());
            verify(pastElectionsRepository, times(1))
                    .save(pastElections);
        }

        @Test
        @DisplayName("Should throw PastElectionNotFoundException when election not found to add candidate")
        void shouldThrowPastElectionNotFoundExceptionWhenElectionNotFoundToAddCandidate(){
            doReturn(Optional.empty())
                    .when(pastElectionsRepository).findById(pastElections.getId());

            assertThrows(PastElectionNotFoundException.class,
                    () -> pastElectionService.addCandidateElected(
                            pastElections.getId(), candidate.getCandidateRegistration()));

            verify(pastElectionsRepository, times(1))
                    .findById(pastElections.getId());
            verify(candidateService, times(0))
                    .findByCandidateRegistration(candidate.getCandidateRegistration());
            verify(pastElectionsRepository, times(0))
                    .save(pastElections);
        }

        @Test
        @DisplayName("Should throw CandidateNotFoundException when candidate not found to add")
        void shouldThrowCandidateNotFoundExceptionWhenCandidateNotFoundToAdd(){
            doReturn(Optional.of(pastElections))
                    .when(pastElectionsRepository).findById(pastElections.getId());
            doThrow(CandidateNotFoundException.class).when(candidateService)
                    .findByCandidateRegistration(candidate.getCandidateRegistration());

            assertThrows(CandidateNotFoundException.class,
                    () -> pastElectionService.addCandidateElected(
                            pastElections.getId(), candidate.getCandidateRegistration()));

            verify(pastElectionsRepository, times(1))
                    .findById(pastElections.getId());
            verify(candidateService, times(1))
                    .findByCandidateRegistration(candidate.getCandidateRegistration());
            verify(pastElectionsRepository, times(0))
                    .save(pastElections);
        }
    }

    @Nested
    class FindById {

        PoliticalParty politicalParty = new PoliticalParty("Partido Exemplo", "SP");

        Candidate candidate = new Candidate("Nome do Candidato", "123456"
                , SituationCandidate.FIRST_TURN, politicalParty, "Nome do Vice"
                , "Coalizão e Federação Exemplo");

        PastElections pastElections = new PastElections(LocalDate.of(2017, 2, 27)
                , "RS", ElectionTurn.FIRST_TURN, ElectionType.MUNICIPAL_ELECTION, 100.0
                , 50.0, List.of(candidate), List.of(candidate)
                , 75.0, LocalDateTime.now());

        @Test
        @DisplayName("Should find past election by ID successfully")
        void shouldFindPastElectionByIdSuccessfully() {
            doReturn(Optional.of(pastElections))
                    .when(pastElectionsRepository).findById(pastElections.getId());

            var response = pastElectionService.findById(pastElections.getId());

            assertNotNull(response);
        }

        @Test
        @DisplayName("Should throw PastElectionNotFoundException when candidate not found by ID")
        void ShouldThrowPastElectionNotFoundExceptionWhenCandidateNotFoundByID() {
            doReturn(Optional.empty())
                    .when(pastElectionsRepository).findById(pastElections.getId());

            assertThrows(PastElectionNotFoundException.class,
                    () -> pastElectionService.findById(pastElections.getId()));
        }
    }
}