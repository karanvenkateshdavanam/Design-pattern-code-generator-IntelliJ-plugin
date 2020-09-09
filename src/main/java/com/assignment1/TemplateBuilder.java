package com.assignment1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class TemplateBuilder implements Build {
    private Template co= new Template();
    static final Logger logger = LoggerFactory.getLogger("TemplateBuilder");
    public void setswitchClass(String switchClass){
        co.switchClass = switchClass;
    }
    public void setimplementingClasses(ArrayList<String> implementingClasses){
        co.implementingClasses = implementingClasses;
    }
    public void settemplateClass(String templateClass){
        co.templateClass = templateClass;
    }
    public void setvaryMethod(String varyMethod){
        co.varyMethod = varyMethod;
    }
    public Template callPattern(){
        logger.info("Returning the pattern builder back to switch factory");
        return co;
    }
}
