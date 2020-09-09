package com.assignment1;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;

public class Template extends PatternGeneration {
    public String switchClass;
    public String templateClass;
    public ArrayList<String> implementingClasses;
    public String varyMethod;
    public String package_name;
    public  String pathname;
    static final Logger logger = LoggerFactory.getLogger("Template");

    public void generatePattern(String pathname,String path){
        logger.info("Pattern is being constructed by the corresponding pattern maker");
        this.package_name = pathname;
        this.pathname = path;
        this.createSwitchClass();
        this.createTemplateClass();
        this.createImplementingClasses();
    }

    public void createSwitchClass(){
        ClassName type = ClassName.get(package_name,templateClass);
        FieldSpec fieldName = FieldSpec.builder(type,"method", Modifier.PRIVATE).build();
        MethodSpec constructorMethod = MethodSpec.constructorBuilder()
                .addParameter(type,"method")
                .addModifiers(Modifier.PUBLIC)
                .addStatement("this.method = method")
                .build();
        MethodSpec operationMethod = MethodSpec.methodBuilder("operation")
                .addModifiers(Modifier.PUBLIC)
                .addStatement("method.operation()")
                .build();
        MethodSpec changeMethod = MethodSpec.methodBuilder("changeMethod")
                .addParameter(type,"method")
                .addModifiers(Modifier.PUBLIC)
                .addStatement("this.method = method")
                .build();
        TypeSpec className = TypeSpec.classBuilder(switchClass)
                .addModifiers(Modifier.PUBLIC)
                .addField(fieldName)
                .addMethod(constructorMethod)
                .addMethod(operationMethod)
                .addMethod(changeMethod)
                .build();
        this.createTypeFile(className,package_name,pathname);

    }
    public void createTemplateClass(){
        MethodSpec abstractName = MethodSpec.methodBuilder(varyMethod)
                .addModifiers(Modifier.PUBLIC,Modifier.ABSTRACT)
                .returns(String.class)
                .build();
        MethodSpec methodName = MethodSpec.methodBuilder("operation")
                .addModifiers(Modifier.PUBLIC)
                .addStatement("String target = $N()",varyMethod)
                .addStatement("System.out.println(\"Invariant part of code\")")
                .addStatement("System.out.println(target)")
                .build();
        TypeSpec className = TypeSpec.classBuilder(templateClass)
                .addModifiers(Modifier.PUBLIC,Modifier.ABSTRACT)
                .addMethod(abstractName)
                .addMethod(methodName)
                .build();
        this.createTypeFile(className,package_name,pathname);
    }
    public void createImplementingClasses(){
        ClassName type = ClassName.get(package_name,templateClass);
        for(String c_name:implementingClasses){
            MethodSpec methodName = MethodSpec.methodBuilder(varyMethod)
                    .returns(String.class)
                    .addModifiers(Modifier.PUBLIC)
                    .addStatement("return \"$N implement variant part of code\"",c_name)
                    .build();
            TypeSpec className = TypeSpec.classBuilder(c_name)
                    .addModifiers(Modifier.PUBLIC)
                    .superclass(type)
                    .addMethod(methodName)
                    .build();
            logger.info("Sending the typespec to the file" );
            this.createTypeFile(className,package_name,pathname);
        }

    }



}
