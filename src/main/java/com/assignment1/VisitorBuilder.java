package com.assignment1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class VisitorBuilder implements Build {
    private Visitor co= new Visitor();
    static final Logger logger = LoggerFactory.getLogger("VisitorBuilder");
    public void setvisitorAbstractClass(String visitorAbstractClass){
        co.visitorAbstractClass = visitorAbstractClass;
    }
    public void setvisitorClass(String visitorClass){
        co.visitorClass = visitorClass;
    }
    public void setvisitableAbstractClass(String visitableAbstractClass){
        co.visitableAbstractClass = visitableAbstractClass;
    }
    public void setvisitableClasses(ArrayList<String> visitableClasses){
        co.visitableClasses = visitableClasses;
    }
    public Visitor callPattern(){
        logger.info("Returning the pattern builder back to switch factory");
        return co;
    }
}
