package com.assignment1;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;

public class AbstractFactory extends PatternGeneration {
    public ArrayList<String> productInterfacedName;
    public int productClassNum;
    public String abstractFactoryName;
    public String factoryClassName;
    public String package_name;
    public String pathname;
    static final Logger logger = LoggerFactory.getLogger("AbstractFactory");


    public void generatePattern(String pathname,String path) {
        logger.info("Pattern is being constructed by the corresponding pattern maker");
        this.package_name = pathname;
        this.pathname = path;
        this.createProductInterface();
        this.generateProductClass();
        this.createFactoryInterface();
        this.createFactoryClass();
    }

    public void createFactoryClass(){
        ClassName type;
        String method_name,class_name,return_name;
        ArrayList<MethodSpec> methodNameList = new ArrayList<>();
        ClassName itype = ClassName.get(package_name, abstractFactoryName);
        for(int i=1;i<=productClassNum;i++) {
            for (String i_name : productInterfacedName) {
                type = ClassName.get(package_name, i_name);
                method_name = "create" + i_name ;
                return_name = i_name + i ;
                MethodSpec methodName = MethodSpec.methodBuilder(method_name)
                        .addModifiers(Modifier.PUBLIC).returns(type).addStatement("return new $N()",return_name).build();
                methodNameList.add(methodName);
            }
            class_name = factoryClassName + i;
            TypeSpec className = TypeSpec.classBuilder(class_name)
                    .addModifiers(Modifier.PUBLIC)
                    .addSuperinterface(itype)
                    .addMethods(methodNameList)
                    .build();
            this.createTypeFile(className,package_name,pathname);
            methodNameList.clear();
        }
    }

    public void createFactoryInterface(){
        ArrayList<MethodSpec> methodNameList = new ArrayList<>();
        for(String i_name:productInterfacedName){
            ClassName type = ClassName.get(package_name,i_name);
            String method_name = "create" + i_name;
            MethodSpec methodName = MethodSpec.methodBuilder(method_name)
                    .addModifiers(Modifier.PUBLIC,Modifier.ABSTRACT).returns(type).build();
            methodNameList.add(methodName);
        }
        TypeSpec interfaceName = TypeSpec
                .interfaceBuilder(abstractFactoryName)
                .addModifiers(Modifier.PUBLIC)
                .addMethods(methodNameList).build();
        this.createTypeFile(interfaceName,package_name,pathname);
    }

    public void generateProductClass(){
        for (String i_name : productInterfacedName){
            ClassName interface_name = ClassName.get(package_name,i_name);
            for(int i=1;i<=productClassNum;i++){
                String class_name = i_name + i;
                MethodSpec getDepartureMethod = MethodSpec.methodBuilder("printName")
                        .addModifiers(Modifier.PUBLIC)
                        .addStatement("$T.out.println($S)", System.class, i_name+i)
                        .build();
                TypeSpec className = TypeSpec.classBuilder(class_name)
                        .addSuperinterface(interface_name)
                        .addMethod(getDepartureMethod)
                        .build();
                this.createTypeFile(className,package_name,pathname);
            }
        }
    }

    public void createProductInterface() {
        for (String i_name : productInterfacedName) {
            TypeSpec interfaceName = TypeSpec
                    .interfaceBuilder(i_name)
                    .addModifiers(Modifier.PUBLIC)
                    .addMethod(MethodSpec
                            .methodBuilder("printName")
                            .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                            .build()).build();
            logger.info("Sending the typespec to the file" );
            this.createTypeFile(interfaceName,package_name,pathname);
        }
    }
}