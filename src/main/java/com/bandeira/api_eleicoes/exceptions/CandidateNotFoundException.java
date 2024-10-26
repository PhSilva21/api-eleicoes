package com.bandeira.api_eleicoes.exceptions;

public class CandidateNotFoundException extends RuntimeException{

    public CandidateNotFoundException(){
        super("Candidate not found");
    }

    public CandidateNotFoundException(String message){
        super(message);
    }
}
