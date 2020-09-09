package com.assignment1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class FactoryBuilder implements Build {
    private Factory co= new Factory();
    static final Logger logger = LoggerFactory.getLogger("FactoryBuilder");
    public void setproductInterfacedName(String productInterfacedName){
        co.productInterfacedName = productInterfacedName;
    }
    public void setproductClassName(ArrayList<String> productClassName){
        co.productClassName = productClassName;
    }
    public void setabstractCreatorClassName(String abstractCreatorClassName){
        co.abstractCreatorClassName = abstractCreatorClassName;
    }
    public void setcreatorClassName(ArrayList<String> creatorClassName){
        co.creatorClassName = creatorClassName;
    }
    public Factory callPattern(){
        logger.info("Returning the pattern builder back to switch factory");
        return co;
    }
}
