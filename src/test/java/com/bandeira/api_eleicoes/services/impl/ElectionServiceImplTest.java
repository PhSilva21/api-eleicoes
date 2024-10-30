package com.bandeira.api_eleicoes.services.impl;

import com.bandeira.api_eleicoes.dtos.AddCandidateToElectionDTO;
import com.bandeira.api_eleicoes.dtos.CloseSessionResponseDTO;
import com.bandeira.api_eleicoes.dtos.CreateElectionDTO;
import com.bandeira.api_eleicoes.dtos.FindElectionByUfAndYear;
import com.bandeira.api_eleicoes.exceptions.CandidateNotFoundException;
import com.bandeira.api_eleicoes.exceptions.ElectionAlreadyExists;
import com.bandeira.api_eleicoes.exceptions.ElectionNotFound;
import com.bandeira.api_eleicoes.model.Candidate;
import com.bandeira.api_eleicoes.model.Election;
import com.bandeira.api_eleicoes.model.PastElections;
import com.bandeira.api_eleicoes.model.PoliticalParty;
import com.bandeira.api_eleicoes.model.enums.ElectionTurn;
import com.bandeira.api_eleicoes.model.enums.ElectionType;
import com.bandeira.api_eleicoes.model.enums.SituationCandidate;
import com.bandeira.api_eleicoes.repositories.CandidateRepository;
import com.bandeira.api_eleicoes.repositories.ElectionRepository;
import com.bandeira.api_eleicoes.repositories.PastElectionsRepository;
import com.bandeira.api_eleicoes.services.CandidateService;
import com.bandeira.api_eleicoes.util.ElectionMapper;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ElectionServiceImplTest {

    @InjectMocks
    ElectionServiceImpl electionService;

    @Mock
    ElectionRepository electionRepository;

    @Mock
    ElectionMapper electionMapper;

    @Mock
    PastElectionsRepository pastElectionsRepository;

    @Mock
    CandidateService candidateService;

    @Mock
    CandidateRepository candidateRepository;

    @Captor
    ArgumentCaptor<Election> electionArgumentCaptor;

    @Captor
    ArgumentCaptor<PastElections> pastElectionsArgumentCaptor;

    @Nested
    @DisplayName("Create Election")
    class CreateElection {

        CreateElectionDTO createElectionDTO = new CreateElectionDTO(LocalDate.now(), "SP",
                ElectionTurn.FIRST_TURN, ElectionType.MUNICIPAL_ELECTION, 100.0);

        Election election = new Election(LocalDate.now(), "SP", ElectionTurn.FIRST_TURN,
                ElectionType.MUNICIPAL_ELECTION, 100.0);

        @Test
        @DisplayName("Create Election")
        void createElection() {
            var list = new ArrayList<>();
            doReturn(list)
                    .when(electionRepository).findAll();
            doReturn(election).when(electionMapper)
                    .toEntity(createElectionDTO);
            doReturn(election)
                    .when(electionRepository).save(electionArgumentCaptor.capture());

            electionService.createElection(createElectionDTO);

            var electionCaptured = electionArgumentCaptor.getValue();

            assertEquals(createElectionDTO.date(), electionCaptured.getDate());
            assertEquals(createElectionDTO.uf(), electionCaptured.getUf());
            assertEquals(createElectionDTO.electionTurn(), electionCaptured.getElectionTurn());
            assertEquals(createElectionDTO.electionType(), electionCaptured.getElectionType());
            assertEquals(createElectionDTO.sessions(), electionCaptured.getTotalSessions());

            verify(electionRepository, times(1))
                    .findAll();
            verify(electionRepository, times(1))
                    .save(election);
        }

        @Test
        @DisplayName("Error - Election Already Exists")
        void errorList(){
            var list = List.of(election);
            doReturn(list)
                    .when(electionRepository).findAll();

            assertThrows(ElectionAlreadyExists.class,
                    () -> electionService.createElection(createElectionDTO));

            verify(electionRepository, times(1))
                    .findAll();
            verify(electionRepository, times(0))
                    .save(election);
        }
    }

    @Nested
    @DisplayName("Validate Election")
    class ValidateElection {

        Election election = new Election(LocalDate.now(), "SP", ElectionTurn.FIRST_TURN,
                ElectionType.MUNICIPAL_ELECTION, 100.0);

        @Test
        @DisplayName("Validate Election")
        void validateElection() {
            var list = List.of();
            doReturn(list)
                    .when(electionRepository).findAll();

            electionService.validateElection(election.getUf(), election.getDate(),
                    election.getElectionType());

            verify(electionRepository, times(1))
                    .findAll();
        }

        @Test
        @DisplayName("Error - Election Already Exists")
        void error() {
            var list = List.of(election);
            doReturn(list)
                    .when(electionRepository).findAll();

            assertThrows(ElectionAlreadyExists.class,
                    () -> electionService.validateElection(election.getUf(), election.getDate(),
                            election.getElectionType()));

            verify(electionRepository, times(1))
                    .findAll();
        }
    }

    @Nested
    @DisplayName("Move to Past Election")
    class ToPastElection {

        Election election = new Election(LocalDate.now(), "SP", ElectionTurn.FIRST_TURN,
                ElectionType.MUNICIPAL_ELECTION, 100.00);

        PastElections pastElections = new PastElections(LocalDate.now(), "SP",
                ElectionTurn.FIRST_TURN, ElectionType.MUNICIPAL_ELECTION, 100.0,
                50.0, List.of(), List.of(), 100.00,
                LocalDateTime.now());

        @Test
        @DisplayName("Move to Past Election")
        void toPastElection() {
            doReturn(Optional.of(election))
                    .when(electionRepository).findById(election.getId());
            doReturn(pastElections)
                    .when(electionMapper).toPastElections(election);
            doReturn(pastElections)
                    .when(pastElectionsRepository).save(pastElectionsArgumentCaptor.capture());
            doNothing().when(electionRepository).deleteById(election.getId());

            var response = electionService.toPastElection(election.getId());

            var pasElectionCaptured = pastElectionsArgumentCaptor.getValue();

            election.setPercentageOfSectionsTotaled(100.00);

            assertEquals(election.getId(), pasElectionCaptured.getId());
            assertEquals(election.getDate(), pasElectionCaptured.getDate());
            assertEquals(election.getUf(), pasElectionCaptured.getUf());
            assertEquals(election.getElectionTurn(), pasElectionCaptured.getElectionTurn());
            assertEquals(election.getElectionType(), pasElectionCaptured.getElectionType());
            assertEquals(election.getTotalSessions(), pasElectionCaptured.getTotalSessions());
            assertEquals(election.getCandidates(), pasElectionCaptured.getCandidates());
            assertEquals(election.getElectedCandidates(), pasElectionCaptured.getElectedCandidates());
            assertEquals(election.getPercentageOfSectionsTotaled(),
                    pasElectionCaptured.getPercentageOfSectionsTotaled());
            assertEquals(election.getPercentageOfSectionsTotaled(),
                    pasElectionCaptured.getPercentageOfSectionsTotaled());

            verify(electionRepository, times(2))
                    .findById(election.getId());
            verify(electionMapper, times(1))
                    .toPastElections(election);
            verify(pastElectionsRepository, times(1))
                    .save(pastElections);
            verify(electionRepository, times(1))
                    .deleteById(election.getId());
        }
    }

    @Nested
    @DisplayName("Close Session")
    class CloseSession {

        Election election = new Election(LocalDate.now(), "SP", ElectionTurn.FIRST_TURN,
                ElectionType.MUNICIPAL_ELECTION, 100.00);

        CloseSessionResponseDTO closeSessionResponseDTO = new CloseSessionResponseDTO(
                "50,00", LocalDateTime.now());

        @Test
        @DisplayName("Close Session")
        void closeSession() {
            doReturn(Optional.of(election))
                    .when(electionRepository).findById(election.getId());
            doReturn(election)
                    .when(electionRepository).save(electionArgumentCaptor.capture());
            election.setRemainingSessions(51.00);
            election.setTotalSessions(100.00);

            var response = electionService.closeSession(election.getId());

            assertEquals(response.percentage(), closeSessionResponseDTO.percentage());
        }

        @Test
        @DisplayName("Error - Election Not Found")
        void errorElection(){
            doReturn(Optional.empty())
                    .when(electionRepository).findById(election.getId());

            assertThrows(ElectionNotFound.class,
                    () -> electionService.closeSession(election.getId()));
        }
    }

    @Nested
    @DisplayName("Add Candidate")
    class AddCandidate {

        Election election = new Election(LocalDate.now(), "SP", ElectionTurn.FIRST_TURN,
                ElectionType.MUNICIPAL_ELECTION, 100.00);

        PoliticalParty politicalParty = new PoliticalParty("Example Party", "SP");

        Candidate candidate = new Candidate("Candidate Name", "123456", SituationCandidate.FIRST_TURN,
                politicalParty, "Vice Candidate", "Example Coalition");

        AddCandidateToElectionDTO addCandidateToElectionDTO = new AddCandidateToElectionDTO(
                candidate.getCandidateRegistration(), election.getId());

        @Test
        @DisplayName("Add Candidate")
        void addCandidate() {
            doReturn(Optional.of(election))
                    .when(electionRepository).findById(election.getId());
            doReturn(candidate)
                    .when(candidateService).findById(candidate.getId());
            doReturn(election)
                    .when(electionRepository).save(electionArgumentCaptor.capture());

            electionService.addCandidate(addCandidateToElectionDTO);

            assertTrue(election.getCandidates().contains(candidate));

            verify(electionRepository, times(1))
                    .findById(election.getId());
            verify(candidateService, times(1))
                    .findById(candidate.getId());
            verify(electionRepository, times(1))
                    .save(election);
        }

        @Test
        @DisplayName("Error - Candidate Not Found")
        void errorCandidate() {
            doReturn(Optional.empty())
                    .when(candidateService).findById(candidate.getId());

            assertThrows(CandidateNotFoundException.class,
                    () -> electionService.addCandidate(addCandidateToElectionDTO));

            verify(candidateService, times(1))
                    .findById(candidate.getId());
        }

        @Test
        @DisplayName("Error - Election Not Found")
        void errorElection() {
            doReturn(Optional.empty())
                    .when(electionRepository).findById(election.getId());

            assertThrows(ElectionNotFound.class,
                    () -> electionService.addCandidate(addCandidateToElectionDTO));

            verify(electionRepository, times(1))
                    .findById(election.getId());
        }
    }

    @Nested
    @DisplayName("Get Elected Candidates")
    class GetElectedCandidates {

        Election election = new Election(LocalDate.now(), "SP", ElectionTurn.FIRST_TURN,
                ElectionType.MUNICIPAL_ELECTION, 100.00);

        @Test
        @DisplayName("Get Elected Candidates")
        void getElectedCandidates() {
            doReturn(Optional.of(election))
                    .when(electionRepository).findById(election.getId());

            electionService.getElectedCandidates(election.getId());

            verify(electionRepository, times(1))
                    .findById(election.getId());
        }

        @Test
        @DisplayName("Error - Election Not Found")
        void errorElected() {
            doReturn(Optional.empty())
                    .when(electionRepository).findById(election.getId());

            assertThrows(ElectionNotFound.class,
                    () -> electionService.getElectedCandidates(election.getId()));

            verify(electionRepository, times(1))
                    .findById(election.getId());
        }
    }

    @Nested
    @DisplayName("Get All Candidates")
    class GetAllCandidates {

        Election election = new Election(LocalDate.now(), "SP", ElectionTurn.FIRST_TURN,
                ElectionType.MUNICIPAL_ELECTION, 100.00);

        @Test
        @DisplayName("Get All Candidates")
        void getAllCandidates() {
            doReturn(Optional.of(election))
                    .when(electionRepository).findById(election.getId());

            electionService.getAllCandidates(election.getId());

            verify(electionRepository, times(1))
                    .findById(election.getId());
        }

        @Test
        @DisplayName("Error - Election Not Found")
        void errorElected() {
            doReturn(Optional.empty())
                    .when(electionRepository).findById(election.getId());

            assertThrows(ElectionNotFound.class,
                    () -> electionService.getAllCandidates(election.getId()));

            verify(electionRepository, times(1))
                    .findById(election.getId());
        }
    }

    @Nested
    @DisplayName("Find by ID")
    class FindById {

        Election election = new Election(LocalDate.now(), "SP", ElectionTurn.FIRST_TURN,
                ElectionType.MUNICIPAL_ELECTION, 100.00);

        @Test
        @DisplayName("Find by ID")
        void findById() {
            doReturn(Optional.of(election))
                    .when(electionRepository).findById(election.getId());

            var result = electionService.findById(election.getId());

            assertEquals(election.getId(), result.getId());
            assertEquals(election.getDate(), result.getDate());
            assertEquals(election.getUf(), result.getUf());
            assertEquals(election.getElectionTurn(), result.getElectionTurn());
            assertEquals(election.getElectionType(), result.getElectionType());

            verify(electionRepository, times(1))
                    .findById(election.getId());
        }

        @Test
        @DisplayName("Error - Election Not Found")
        void notFound() {
            doReturn(Optional.empty())
                    .when(electionRepository).findById(election.getId());

            assertThrows(ElectionNotFound.class,
                    () -> electionService.findById(election.getId()));

            verify(electionRepository, times(1))
                    .findById(election.getId());
        }
    }

    @Nested
    @DisplayName("Delete by ID")
    class DeleteById {

        Election election = new Election(LocalDate.now(), "SP", ElectionTurn.FIRST_TURN,
                ElectionType.MUNICIPAL_ELECTION, 100.00);

        @Test
        @DisplayName("Delete by ID")
        void deleteById() {
            doReturn(Optional.of(election))
                    .when(electionRepository).findById(election.getId());
            doNothing()
                    .when(electionRepository).deleteById(election.getId());

            electionService.deleteById(election.getId());

            verify(electionRepository, times(1))
                    .findById(election.getId());
            verify(electionRepository, times(1))
                    .deleteById(election.getId());
        }

        @Test
        @DisplayName("Error - Election Not Found")
        void notFound() {
            doReturn(Optional.empty())
                    .when(electionRepository).findById(election.getId());

            assertThrows(ElectionNotFound.class,
                    () -> electionService.deleteById(election.getId()));

            verify(electionRepository, times(1))
                    .findById(election.getId());
        }
    }

    @Nested
    @DisplayName("Calculate Percentage")
    class Calculation {

        Election election = new Election(LocalDate.now(), "SP", ElectionTurn.FIRST_TURN,
                ElectionType.MUNICIPAL_ELECTION, 100.00);

        @Test
        @DisplayName("Calculate Percentage")
        void calculation() {
            election.setRemainingSessions(51.00);
            election.setTotalSessions(100.00);

            var result = electionService.calculation(election.getTotalSessions()
                    , election.getRemainingSessions(), election);

            assertEquals("51,00", result);
        }
    }
}
