package com.assignment1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class FacadeBuilder implements Build {
    private Facade co= new Facade();
    static final Logger logger = LoggerFactory.getLogger("Builder Pattern");
    public void setfacadeInterfacedName(String facadeInterfacedName){
        co.facadeInterfacedName = facadeInterfacedName;
    }
    public void subSystemClassName(ArrayList<String> subSystemClassName){
        co.subSystemClassName = subSystemClassName;
    }
    public Facade callPattern(){
        logger.info("Returning the pattern builder back to switch factory");
        return co;
    }
}