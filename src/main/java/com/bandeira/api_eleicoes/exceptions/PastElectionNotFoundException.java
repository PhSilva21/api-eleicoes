package com.bandeira.api_eleicoes.exceptions;

public class PastElectionNotFoundException extends RuntimeException{


    public PastElectionNotFoundException(){
        super("PastElection not found");
    }

    public PastElectionNotFoundException(String message){
        super(message);
    }
}
