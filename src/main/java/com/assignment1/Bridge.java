package com.assignment1;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;

public class Bridge extends PatternGeneration {
    public String bridgeInterface;
    public ArrayList<String> bridgeClasses;
    public String implementorInterface;
    public ArrayList<String> implementorClasses;
    public String package_name;
    public String pathname;
    static final Logger logger = LoggerFactory.getLogger("Bridge");

    public Bridge(){
    }

    public void generatePattern(String pathname,String path){
        logger.info("Pattern is being constructed by the corresponding pattern maker");
        this.package_name = pathname;
        this.pathname = path;
        this.createBridgeInterface();
        this.createBridgeClasses();
        this.createImplementorInterface();
        this.createImplementorClasses();
    }

    public void createBridgeInterface(){
        MethodSpec methodName = MethodSpec.methodBuilder("operation")
                .returns(String.class)
                .addModifiers(Modifier.ABSTRACT,Modifier.PUBLIC)
                .build();
       TypeSpec interfaceName = TypeSpec.interfaceBuilder(bridgeInterface)
               .addModifiers(Modifier.PUBLIC)
               .addMethod(methodName)
               .build();
        this.createTypeFile(interfaceName,package_name,pathname);
    }
    public void createBridgeClasses(){
        ClassName bridgeName = ClassName.get(package_name,bridgeInterface);
        ClassName implementName = ClassName.get(package_name,implementorInterface);
        for(String c_name:bridgeClasses){
            FieldSpec fieldName = FieldSpec.builder(implementName,"imp",Modifier.PRIVATE)
                    .build();
            MethodSpec constructorName = MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(implementName,"imp")
                    .addStatement("this.imp=imp")
                    .build();
            MethodSpec methodName = MethodSpec.methodBuilder("operation")
                    .returns(String.class)
                    .addModifiers(Modifier.PUBLIC)
                    .addStatement("return \"$N:Delegating implementation to an implementor\"+ imp.operationImp()",c_name)
                    .build();
            TypeSpec className = TypeSpec.classBuilder(c_name)
                    .addModifiers(Modifier.PUBLIC)
                    .addSuperinterface(bridgeName)
                    .addField(fieldName)
                    .addMethod(constructorName)
                    .addMethod(methodName)
                    .build();
            this.createTypeFile(className,package_name,pathname);
        }

    }
    public void createImplementorInterface(){
        MethodSpec methodName = MethodSpec.methodBuilder("operationImp")
                .returns(String.class)
                .addModifiers(Modifier.ABSTRACT,Modifier.PUBLIC)
                .build();
        TypeSpec interfaceName = TypeSpec.interfaceBuilder(implementorInterface)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(methodName)
                .build();
        this.createTypeFile(interfaceName,package_name,pathname);
    }
    public void createImplementorClasses(){
        ClassName implementName = ClassName.get(package_name,implementorInterface);
        for(String c_name:implementorClasses){
            MethodSpec methodName = MethodSpec.methodBuilder("operationImp")
                    .addModifiers(Modifier.PUBLIC)
                    .returns(String.class)
                    .addStatement("return \"$N: Hello World!\"",c_name)
                    .build();
            TypeSpec className = TypeSpec.classBuilder(c_name)
                    .addModifiers(Modifier.PUBLIC)
                    .addSuperinterface(implementName)
                    .addMethod(methodName)
                    .build();
            logger.info("Sending the typespec to the file" );
            this.createTypeFile(className,package_name,pathname);

        }

    }


}
