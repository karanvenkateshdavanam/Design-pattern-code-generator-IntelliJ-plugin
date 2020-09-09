package com.assignment1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class StrategyBuilder implements Build {
    private Strategy co= new Strategy();
    static final Logger logger = LoggerFactory.getLogger("StrategyBuilder");
    public void setintefaceName(String intefaceName){
        co.intefaceName = intefaceName;
    }
    public void setcontextclassName(String contextclassName){
        co.contextclassName = contextclassName;
    }
    public void setclassName(ArrayList<String> className){
        co.className = className;
    }
    public Strategy callPattern(){
        logger.info("Returning the pattern builder back to switch factory");
        return co;
    }
}