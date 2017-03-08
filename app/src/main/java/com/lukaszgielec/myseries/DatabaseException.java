package com.lukaszgielec.myseries;

/**
 * Created by Łukasz Gielec on 07.03.2017.
 */

public class DatabaseException extends Exception {

    private String mMessage;

    public DatabaseException(String message){
        mMessage = message;
    }

    public String getMyMessage(){
        return mMessage;
    }
}
