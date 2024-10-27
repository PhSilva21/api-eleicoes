package com.bandeira.api_eleicoes.exceptions;

public class ElectionNotFound extends RuntimeException{

    public ElectionNotFound(){
        super("Election not found");
    }

    public ElectionNotFound(String msg){
        super(msg);
    }
}
