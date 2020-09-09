package com.assignment1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class AbstractFactoryBuilder implements Build {
    private AbstractFactory co= new AbstractFactory();
    static final Logger logger = LoggerFactory.getLogger("AbstractFactoryBuilder");

    public void setproductInterfacedName(ArrayList<String> productInterfacedName){
        co.productInterfacedName = productInterfacedName;
    }

    public void setFactoryName(String factoryName){
        co.abstractFactoryName = factoryName;
        co.factoryClassName = factoryName;
    }

    public void setproductClassNum(int productClassNum){
        co.productClassNum = productClassNum;
    }
    public AbstractFactory callPattern(){
        logger.info("Returning the pattern builder back to switch factory");
        return co;
    }
}
