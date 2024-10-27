package com.bandeira.api_eleicoes.exceptions;

public class PoliticalPartyNotFound extends RuntimeException{

    public PoliticalPartyNotFound(){
        super("Political party nt found");
    }

    public PoliticalPartyNotFound(String msg){
        super(msg);
    }
}
