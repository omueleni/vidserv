package com.bskyb.internettv.exceptions;

/**
 * Created by kogbe on 06/06/2018.
 */
public class ApplicationFailureException extends  Exception {
    public ApplicationFailureException(String messsage, Throwable e){
        super(messsage,e);
    }
}
