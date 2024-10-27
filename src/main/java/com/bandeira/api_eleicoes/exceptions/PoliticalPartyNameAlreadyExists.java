package com.bandeira.api_eleicoes.exceptions;

public class PoliticalPartyNameAlreadyExists extends RuntimeException{

    public PoliticalPartyNameAlreadyExists(){
        super("Political party name already exists");
    }

    public PoliticalPartyNameAlreadyExists(String msg){
        super(msg);
    }

}
