package com.bandeira.api_eleicoes.exceptions;

public class ElectionAlreadyExists extends RuntimeException{

    public ElectionAlreadyExists(){
        super("Election already exists");
    }

    public ElectionAlreadyExists(String msg){
        super(msg);
    }
}
