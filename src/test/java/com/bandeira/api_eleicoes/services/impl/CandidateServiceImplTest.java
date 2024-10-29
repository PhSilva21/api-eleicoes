package com.bandeira.api_eleicoes.services.impl;

import com.bandeira.api_eleicoes.dtos.CreateCandidateDTO;
import com.bandeira.api_eleicoes.dtos.UpdateCandidateDTO;
import com.bandeira.api_eleicoes.dtos.UploadResponse;
import com.bandeira.api_eleicoes.exceptions.CandidateNotFoundException;
import com.bandeira.api_eleicoes.exceptions.PoliticalPartyNotFound;
import com.bandeira.api_eleicoes.model.Candidate;
import com.bandeira.api_eleicoes.model.PoliticalParty;
import com.bandeira.api_eleicoes.model.enums.SituationCandidate;
import com.bandeira.api_eleicoes.repositories.CandidateRepository;
import com.bandeira.api_eleicoes.services.CandidateService;
import com.bandeira.api_eleicoes.services.PoliticalPartyService;
import com.bandeira.api_eleicoes.services.UploadService;
import com.bandeira.api_eleicoes.util.RandomString;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.util.Optional;

import static com.bandeira.api_eleicoes.util.RandomString.generateRandomString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CandidateServiceTest {

    @InjectMocks
    CandidateServiceImpl candidateServiceImpl;

    @Mock
    CandidateRepository candidateRepository;

    @Mock
    PoliticalPartyService politicalPartyService;

    @Mock
    UploadService uploadService;

    @Captor
    ArgumentCaptor<Candidate> candiateArgumentCaptor;

    @Nested
    class CreateCandidate {

        PoliticalParty politicalParty = new PoliticalParty("Partido Exemplo", "SP");

        Candidate candidate = new Candidate("Nome do Candidato", "123456", SituationCandidate.FIRST_TURN,
                politicalParty, "Nome do Vice", "Coalizão e Federação Exemplo");

        CreateCandidateDTO candidateDTO = new CreateCandidateDTO(
                "Nome do Candidato", "Nome do Vice", "Partido Exemplo"
                , "Coalizão e Federação Exemplo");

        byte[] content = "Conteúdo do arquivo de exemplo".getBytes();

        MockMultipartFile file = new MockMultipartFile("file", "example.txt"
                , "text/plain", content);

        PutObjectResponse response = PutObjectResponse.builder().build();
        String location = "https://bucket-name.s3.region.amazonaws.com/file-key";
        UploadResponse uploadResponse = new UploadResponse(response, null, location);

        String code = "ABC123";

        @Test
        @DisplayName("Should create candidate successfully")
        void createCandidate() throws Exception {
            mockStatic(RandomString.class);
            when(generateRandomString(6)).thenReturn(code);
            doReturn(politicalParty)
                    .when(politicalPartyService).findByName(candidateDTO.politicalPartyName());
            doReturn(uploadResponse)
                    .when(uploadService).uploadFile(file);
            doReturn(candidate)
                    .when(candidateRepository).save(candiateArgumentCaptor.capture());

            var response = candidateServiceImpl.createCandidate(candidateDTO, file);

            var candidateCaptured = candiateArgumentCaptor.getValue();

            assertEquals(candidateDTO.name(), candidateCaptured.getName());
            assertEquals(candidateCaptured.getCandidateRegistration(), code);
            assertEquals(candidateCaptured.getSituationCandidate(), SituationCandidate.FIRST_TURN);
            assertEquals(candidateDTO.vice(), candidateCaptured.getVice());
            assertEquals(candidateDTO.politicalPartyName(), candidateCaptured.getPoliticalParty().getName());
            assertEquals(candidateDTO.coalitionAndFederation(), candidateCaptured.getCoalitionAndFederation());

            verify(RandomString.class, times(1));
            RandomString.generateRandomString(6);
            verify(politicalPartyService, times(1))
                    .findByName(candidateDTO.politicalPartyName());
            verify(uploadService, times(1))
                    .uploadFile(file);
            verify(candidateRepository, times(1))
                    .save(candiateArgumentCaptor.getValue());
        }

        @Test
        @DisplayName("Should throw PoliticalPartyNotFound exception")
        void shouldThrowPoliticalPartyNotFoundException() throws Exception {
            mockStatic(RandomString.class);
            when(generateRandomString(6)).thenReturn(code);
            doThrow(PoliticalPartyNotFound.class)
                    .when(politicalPartyService).findByName(candidateDTO.politicalPartyName());

            assertThrows(PoliticalPartyNotFound.class,
                    () -> candidateServiceImpl.createCandidate(candidateDTO, file));

            verify(RandomString.class, times(1));
            RandomString.generateRandomString(6);
            verify(politicalPartyService, times(1))
                    .findByName(candidateDTO.politicalPartyName());
            verify(uploadService, times(0))
                    .uploadFile(file);
            verify(candidateRepository, times(0))
                    .save(candidate);
        }

        @Test
        @DisplayName("Test error when trying to create candidate with file error")
        void errorWhenTryingToCreateCandidateWithFileError() throws Exception {
            mockStatic(RandomString.class);
            when(generateRandomString(6)).thenReturn(code);
            doReturn(politicalParty)
                    .when(politicalPartyService).findByName(candidateDTO.politicalPartyName());
            doThrow(IOException.class)
                    .when(uploadService).uploadFile(file);

            assertThrows(IOException.class,
                    () -> candidateServiceImpl.createCandidate(candidateDTO, file));

            verify(RandomString.class, times(1));
            RandomString.generateRandomString(6);
            verify(politicalPartyService, times(1))
                    .findByName(candidateDTO.politicalPartyName());
            verify(uploadService, times(1))
                    .uploadFile(file);
            verify(candidateRepository, times(0))
                    .save(candidate);
        }

        @Test
        @DisplayName("Test generic error when trying to create candidate")
        void genericError() throws Exception {
            mockStatic(RandomString.class);
            when(generateRandomString(6)).thenReturn(code);
            doReturn(politicalParty)
                    .when(politicalPartyService).findByName(candidateDTO.politicalPartyName());
            doThrow(Exception.class)
                    .when(uploadService).uploadFile(file);

            assertThrows(Exception.class,
                    () -> candidateServiceImpl.createCandidate(candidateDTO, file));

            verify(RandomString.class, times(1));
            RandomString.generateRandomString(6);
            verify(politicalPartyService, times(1))
                    .findByName(candidateDTO.politicalPartyName());
            verify(uploadService, times(1))
                    .uploadFile(file);
            verify(candidateRepository, times(0))
                    .save(candidate);
        }
    }

    @Nested
    class UpdateCandidate {

        PoliticalParty politicalParty = new PoliticalParty("Partido Exemplo", "SP");

        Candidate candidate = new Candidate("Nome do Candidato", "123456", SituationCandidate.FIRST_TURN,
                politicalParty, "Nome do Vice", "Coalizão e Federação Exemplo");

        UpdateCandidateDTO updateCandidate = new UpdateCandidateDTO(1L, "Nome do Candidato"
                , SituationCandidate.FIRST_TURN, "Nome do Vice", "Partido Exemplo"
                , "Coalizão e Federação Exemplo"
        );

        byte[] content = "Conteúdo do arquivo de exemplo".getBytes();

        MockMultipartFile file = new MockMultipartFile("file", "example.txt"
                , "text/plain", content);

        PutObjectResponse response = PutObjectResponse.builder().build();
        String location = "https://bucket-name.s3.region.amazonaws.com/file-key";
        UploadResponse uploadResponse = new UploadResponse(response, null, location);

        @Test
        @DisplayName("Test successful candidate update")
        void updateCandidate() throws Exception {
            doReturn(Optional.of(candidate))
                    .when(candidateRepository).findById(updateCandidate.id());
            doReturn(uploadResponse)
                    .when(uploadService).uploadFile(file);
            doReturn(politicalParty)
                    .when(politicalPartyService).findByName(updateCandidate.politicalPartyName());
            doReturn(candidate)
                    .when(candidateRepository).save(candiateArgumentCaptor.capture());

            candidateServiceImpl.updateCandidate(updateCandidate, file);

            var candidateCaptured = candiateArgumentCaptor.getValue();

            assertEquals(updateCandidate.name(), candidateCaptured.getName());
            assertEquals(uploadResponse.location(), candidateCaptured.getPhotoPath());
            assertEquals(updateCandidate.situationCandidate(), candidateCaptured.getSituationCandidate());
            assertEquals(updateCandidate.vice(), candidateCaptured.getVice());
            assertEquals(politicalParty, candidateCaptured.getPoliticalParty());
            assertEquals(updateCandidate.coalitionAndFederation()
                    , candidateCaptured.getCoalitionAndFederation());

            verify(candidateRepository, times(1))
                    .findById(updateCandidate.id());
            verify(uploadService, times(1))
                    .uploadFile(file);
            verify(politicalPartyService, times(1))
                    .findByName(updateCandidate.politicalPartyName());
            verify(candidateRepository, times(2))
                    .save(candidate);
        }

        @Test
        @DisplayName("Should throw exception when file error occurs during update")
        void shouldThrowExceptionWhenFileErrorOccursDuringUpdate() throws Exception {
            doReturn(Optional.of(candidate))
                    .when(candidateRepository).findById(updateCandidate.id());
            doThrow(Exception.class)
                    .when(uploadService).uploadFile(file);

            assertThrows(Exception.class,
                    () -> candidateServiceImpl.updateCandidate(updateCandidate, file));

            verify(candidateRepository, times(1))
                    .findById(updateCandidate.id());
            verify(uploadService, times(1))
                    .uploadFile(file);
            verify(politicalPartyService, times(0))
                    .findByName(updateCandidate.politicalPartyName());
            verify(candidateRepository, times(0))
                    .save(candidate);
        }

        @Test
        @DisplayName("Test error when trying to update candidate with file error")
        void errorWhenTryingToUpdateCandidateWithFileError() throws Exception {
            doReturn(Optional.of(candidate))
                    .when(candidateRepository).findById(updateCandidate.id());
            doThrow(IOException.class)
                    .when(uploadService).uploadFile(file);

            assertThrows(IOException.class,
                    () -> candidateServiceImpl.updateCandidate(updateCandidate, file));

            verify(candidateRepository, times(1))
                    .findById(updateCandidate.id());
            verify(uploadService, times(1))
                    .uploadFile(file);
            verify(politicalPartyService, times(0))
                    .findByName(updateCandidate.politicalPartyName());
            verify(candidateRepository, times(0))
                    .save(candidate);
        }

    }

    @Nested
    class SetPhotoPath {

        PoliticalParty politicalParty = new PoliticalParty("Partido Exemplo", "SP");

        Candidate candidate = new Candidate("Nome do Candidato", "123456", SituationCandidate.FIRST_TURN,
                politicalParty, "Nome do Vice", "Coalizão e Federação Exemplo");

        byte[] content = "Conteúdo do arquivo de exemplo".getBytes();

        MockMultipartFile file = new MockMultipartFile("file", "example.txt", "text/plain", content);

        PutObjectResponse response = PutObjectResponse.builder().build();
        String location = "https://bucket-name.s3.region.amazonaws.com/file-key";
        UploadResponse uploadResponse = new UploadResponse(response, null, location);

        @Test
        @DisplayName("Should set candidate photo path successfully")
        void setPhotoPath() throws Exception {
            doReturn(uploadResponse)
                    .when(uploadService).uploadFile(file);
            doReturn(candidate)
                    .when(candidateRepository).save(candiateArgumentCaptor.capture());

            var response = candidateServiceImpl.setPhotoPath(file, candidate);

            var candidateCaptured = candiateArgumentCaptor.getValue();

            assertEquals(candidateCaptured.getPhotoPath(), uploadResponse.location());

            verify(uploadService, times(1))
                    .uploadFile(file);
            verify(candidateRepository, times(1))
                    .save(candidate);
        }

        @DisplayName("Should throw exception when file upload fails")
        void shouldThrowExceptionWhenFileUploadFails() throws Exception {
            doThrow(Exception.class)
                    .when(uploadService).uploadFile(file);

            assertThrows(Exception.class,
                    () -> candidateServiceImpl.setPhotoPath(file, candidate));

            verify(uploadService, times(1))
                    .uploadFile(file);
            verify(candidateRepository, times(0))
                    .save(candidate);
        }

        @Test
        @DisplayName("Should throw IOException when file upload has IOException")
        void shouldThrowIOExceptionWhenFileUploadHasIOException() throws Exception {
            doThrow(IOException.class)
                    .when(uploadService).uploadFile(file);

            assertThrows(IOException.class,
                    () -> candidateServiceImpl.setPhotoPath(file, candidate));

            verify(uploadService, times(1))
                    .uploadFile(file);
            verify(candidateRepository, times(0))
                    .save(candidate);
        }
    }

    @Nested
    class GenerateCandidateRegistration {

        PoliticalParty politicalParty = new PoliticalParty("Partido Exemplo", "SP");

        Candidate candidate = new Candidate("Nome do Candidato", "123456", SituationCandidate.FIRST_TURN,
                politicalParty, "Nome do Vice", "Coalizão e Federação Exemplo");

        String code = "dffd15";

        @Test
        @DisplayName("Should create candidate registration successfully")
        void generateCandidateRegistration() {
            mockStatic(RandomString.class);
            when(generateRandomString(6)).thenReturn(code);
            doReturn(Optional.empty())
                    .when(candidateRepository).findByCandidateRegistration(code);

            var response = candidateServiceImpl.generateCandidateRegistration();

            assertEquals(response, code);

        }
    }

    @Nested
    class FindByCandidateRegistration {

        PoliticalParty politicalParty = new PoliticalParty("Partido Exemplo", "SP");

        Candidate candidate = new Candidate("Nome do Candidato", "123456", SituationCandidate.FIRST_TURN,
                politicalParty, "Nome do Vice", "Coalizão e Federação Exemplo");

        @Test
        @DisplayName("Should find candidate by registration successfully")
        void findByCandidateRegistration(){
            doReturn(Optional.of(candidate))
                    .when(candidateRepository).findByCandidateRegistration("d2162s");

            var response = candidateServiceImpl.findByCandidateRegistration("d2162s");

            assertNotNull(response);
        }

        @Test
        @DisplayName("Should throw CandidateNotFoundException when candidate not found by registration")
        void ShouldThrowCandidateNotFoundExceptionWhenCandidateNotFoundByRegistration() {
            doReturn(Optional.empty())
                    .when(candidateRepository).findByCandidateRegistration("d2162s");

            assertThrows(CandidateNotFoundException.class,
                    () -> candidateServiceImpl.findByCandidateRegistration("d2162s"));
        }
    }

    @Nested
    class FindById {

        PoliticalParty politicalParty = new PoliticalParty("Partido Exemplo", "SP");

        Candidate candidate = new Candidate("Nome do Candidato", "123456", SituationCandidate.FIRST_TURN,
                politicalParty, "Nome do Vice", "Coalizão e Federação Exemplo");

        @Test
        @DisplayName("Should find candidate by ID successfully")
        void shouldFindCandidateByIdSuccessfully() {
            doReturn(Optional.of(candidate))
                    .when(candidateRepository).findById(candidate.getId());

            var response = candidateServiceImpl.findById(candidate.getId());

            assertNotNull(response);
        }

        @Test
        @DisplayName("Should throw CandidateNotFoundException when candidate not found by ID")
        void ShouldThrowCandidateNotFoundExceptionWhenCandidateNotFoundByID() {
            doReturn(Optional.empty())
                    .when(candidateRepository).findById(candidate.getId());

            assertThrows(CandidateNotFoundException.class,
                    () -> candidateServiceImpl.findById(candidate.getId()));
        }
    }

    @Nested
    class FindByName {
        PoliticalParty politicalParty = new PoliticalParty("Partido Exemplo", "SP");

        Candidate candidate = new Candidate("Nome do Candidato", "123456", SituationCandidate.FIRST_TURN,
                politicalParty, "Nome do Vice", "Coalizão e Federação Exemplo");

        @Test
        @DisplayName("Should find candidate by name successfully")
        void shouldFindCandidateByNAmeSuccessfully() {
            doReturn(Optional.of(candidate))
                    .when(candidateRepository).findByName(candidate.getName());

            var response = candidateServiceImpl.findByName(candidate.getName());

            assertNotNull(response);
        }

        @Test
        @DisplayName("Should throw CandidateNotFoundException when candidate not found by name")
        void shouldThrowCandidateNotFoundExceptionWhenCandidateNotFoundByName() {
            doReturn(Optional.empty())
                    .when(candidateRepository).findByName(candidate.getName());

            assertThrows(CandidateNotFoundException.class,
                    () -> candidateServiceImpl.findByName(candidate.getName()));
        }
    }

    @Nested
    class DeleteById {

        PoliticalParty politicalParty = new PoliticalParty("Partido Exemplo", "SP");

        Candidate candidate = new Candidate("Nome do Candidato", "123456", SituationCandidate.FIRST_TURN,
                politicalParty, "Nome do Vice", "Coalizão e Federação Exemplo");
        @Test
        @DisplayName("Should delete candidate by ID successfully")
        void shouldDeleteCandidateByIdSuccessfully() {
            doReturn(Optional.of(candidate))
                    .when(candidateRepository).findById(candidate.getId());
            doNothing()
                    .when(candidateRepository)
                    .deleteById(candidate.getId());

            candidateServiceImpl.deleteById(candidate.getId());

            verify(candidateRepository, times(1))
                    .findById(candidate.getId());
            verify(candidateRepository, times(1))
                    .deleteById(candidate.getId());
        }

        @Test
        @DisplayName("Should throw CandidateNotFoundException when candidate not found to delete by ID")
        void ShouldThrowCandidateNotFoundExceptionWhenCandidateNotFoundToDeleteByID() {
            doReturn(Optional.empty())
                    .when(candidateRepository)
                    .findById(candidate.getId());

            assertThrows(CandidateNotFoundException.class,
                    () -> candidateServiceImpl.deleteById(candidate.getId()));

            verify(candidateRepository, times(1))
                    .findById(candidate.getId());
            verify(candidateRepository, times(0))
                    .save(candidate);
        }
    }
}