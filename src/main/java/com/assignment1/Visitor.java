package com.assignment1;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;

public class Visitor extends PatternGeneration {
    public String visitorAbstractClass;
    public String visitorClass;
    public String visitableAbstractClass;
    public ArrayList<String> visitableClasses;
    public String package_name;
    public String pathname;
    static final Logger logger = LoggerFactory.getLogger("Visitor");


    public void generatePattern(String pathname,String path){
        logger.info("Pattern is being constructed by the corresponding pattern maker");
        this.package_name = pathname;
        this.pathname = path;
        this.createAbstractVisitorClass();
        this.createVisitorClass();
        this.createAbstractVisitableClass();
        this.createVisitableClass();
    }

    public void createAbstractVisitorClass(){
        ArrayList<MethodSpec> methodNameList = new ArrayList<>();
        for (String c_name:visitableClasses){
            ClassName className = ClassName.get(package_name,c_name);
            MethodSpec getAbstractMethod = MethodSpec.methodBuilder("visit"+c_name)
                    .addParameter(className,"e")
                    .addModifiers(Modifier.PUBLIC,Modifier.ABSTRACT)
                    .build();
            methodNameList.add(getAbstractMethod);
        }
        TypeSpec className = TypeSpec.classBuilder(visitorAbstractClass)
                .addModifiers(Modifier.PUBLIC,Modifier.ABSTRACT)
                .addMethods(methodNameList)
                .build();
        this.createTypeFile(className,package_name,pathname);
        methodNameList.clear();
    }
    public void createVisitorClass(){
        ArrayList<MethodSpec> methodNameList = new ArrayList<>();
        ClassName superName = ClassName.get(package_name,visitorAbstractClass);
        for (String c_name:visitableClasses) {
            ClassName className = ClassName.get(package_name, c_name);
            MethodSpec getAbstractMethod = MethodSpec.methodBuilder("visit" + c_name)
                    .addParameter(className, "e")
                    .addModifiers(Modifier.PUBLIC)
                    .addStatement("System.out.println(\"$N:Visit $N" + "\"" +"+"+"e." + "operation" + c_name+ "()" + ")", visitorClass, c_name)
                    .build();
            methodNameList.add(getAbstractMethod);
        }
            TypeSpec classVisitName = TypeSpec.classBuilder(visitorClass)
                    .addModifiers(Modifier.PUBLIC)
                    .superclass(superName)
                    .addMethods(methodNameList)
                    .build();
            this.createTypeFile(classVisitName,package_name,pathname);
            methodNameList.clear();
        }

    public void createAbstractVisitableClass(){
        ClassName className = ClassName.get(package_name,visitorAbstractClass);
        MethodSpec getAbstractMethod = MethodSpec.methodBuilder("accept")
                    .addParameter(className,"visitor")
                    .addModifiers(Modifier.PUBLIC,Modifier.ABSTRACT)
                    .build();
        TypeSpec classVisitableName = TypeSpec.classBuilder(visitableAbstractClass)
                .addModifiers(Modifier.PUBLIC,Modifier.ABSTRACT)
                .addMethod(getAbstractMethod)
                .build();
        this.createTypeFile(classVisitableName,package_name,pathname);

    }
    public void createVisitableClass(){
        ClassName className = ClassName.get(package_name,visitorAbstractClass);
        ClassName superName = ClassName.get(package_name,visitableAbstractClass);
        for(String c_name:visitableClasses){
            MethodSpec getExtendMethod = MethodSpec.methodBuilder("accept")
                    .addParameter(className,"visitor")
                    .addModifiers(Modifier.PUBLIC)
                    .addStatement("visitor.visit$N(this)",c_name)
                    .build();
            MethodSpec getVisitableMethod = MethodSpec.methodBuilder("operation"+c_name)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(String.class)
                    .addStatement("return  \"Hello from $N!!\"",c_name)
                    .build();
            TypeSpec classVisitableName = TypeSpec.classBuilder(c_name)
                    .addModifiers(Modifier.PUBLIC)
                    .superclass(superName)
                    .addMethod(getExtendMethod)
                    .addMethod(getVisitableMethod)
                    .build();
            logger.info("Sending the typespec to the file" );
            this.createTypeFile(classVisitableName,package_name,pathname);

        }

    }

}
