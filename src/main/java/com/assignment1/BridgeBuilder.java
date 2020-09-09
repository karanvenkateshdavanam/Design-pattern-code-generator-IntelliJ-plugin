package com.assignment1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class BridgeBuilder implements Build {
    private Bridge co= new Bridge();
    static final Logger logger = LoggerFactory.getLogger("Builder Pattern");
    public void setBridgeInterface(String bridgeInterface){
        co.bridgeInterface = bridgeInterface;
    }
    public void setBridgeClasses(ArrayList<String> bridgeClasses){
        co.bridgeClasses = bridgeClasses;
    }
    public void setImplementorInterface(String implementorInterface){
        co.implementorInterface = implementorInterface;
    }
    public void setimplementorClasses(ArrayList<String> implementorClasses){
        co.implementorClasses = implementorClasses;
    }
    public Bridge callPattern(){
        logger.info("Returning the pattern builder back to switch factory");
        return co;
    }
}
