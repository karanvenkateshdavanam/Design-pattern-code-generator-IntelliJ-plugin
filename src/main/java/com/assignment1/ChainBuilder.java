package com.assignment1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class ChainBuilder implements Build {
    private Chain co= new Chain();
    static final Logger logger = LoggerFactory.getLogger("ChainBuilder");
    public void sethandlerAbstractClass(String handlerAbstractClass){
        co.handlerAbstractClass = handlerAbstractClass;
    }
    public void setrequestHandlingClasses(ArrayList<String> requestHandlingClasses){
        co.requestHandlingClasses = requestHandlingClasses;
    }
    public void sethandleRequestMethod(String handleRequestMethod){
        co.handleRequestMethod = handleRequestMethod;
    }
    public Chain callPattern(){
        logger.info("Returning the pattern builder back to switch factory");
        return co;
    }
}