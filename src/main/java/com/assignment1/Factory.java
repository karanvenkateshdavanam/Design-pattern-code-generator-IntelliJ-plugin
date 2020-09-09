package com.assignment1;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;

public class Factory extends PatternGeneration{
    public String productInterfacedName;
    public String abstractCreatorClassName;
    public ArrayList<String> productClassName;
    public ArrayList<String>  creatorClassName;
    public String package_name;
    public  String pathname;
    static final Logger logger = LoggerFactory.getLogger("Factory");

    public void generatePattern(String pathname,String path) {
        logger.info("Pattern is being constructed by the corresponding pattern maker");
        this.package_name = pathname;
        this.pathname = path;
        this.createProductInterface();
        this.generateProductClass();
        this.abstractCreatorClass();
        this.generateCreatorClass();
    }

    public void generateCreatorClass(){
        ClassName className1 = ClassName.get(package_name,productInterfacedName);
        ClassName className2 = ClassName.get(package_name,abstractCreatorClassName);
        int i = 1;
        for (String c_name:creatorClassName){
            MethodSpec getFactoryMethod = MethodSpec.methodBuilder("factoryMethod")
                    .addModifiers(Modifier.PUBLIC)
                    .returns(className1)
                    .addStatement("return new $T$L()",className1,i)
                    .build();
            TypeSpec className = TypeSpec.classBuilder(c_name)
                    .superclass(className2)
                    .addMethod(getFactoryMethod)
                    .build();
            this.createTypeFile(className,package_name,pathname);
            i = i + 1;
        }
    }

    public void abstractCreatorClass(){
        ClassName className1 = ClassName.get(package_name,productInterfacedName);
        FieldSpec productField
                = FieldSpec.builder(className1, productInterfacedName.toLowerCase(), Modifier.PRIVATE)
                .build();
        MethodSpec getAbstractMethod = MethodSpec.methodBuilder("factoryMethod")
                .addModifiers(Modifier.PUBLIC,Modifier.ABSTRACT)
                .returns(className1)
                .build();
        MethodSpec getOperationMethod = MethodSpec.methodBuilder("operation")
                .addModifiers(Modifier.PUBLIC)
                .addStatement("$N=$N();",productInterfacedName.toLowerCase(),"factoryMethod")
                .addStatement("$N.printName();",productInterfacedName.toLowerCase())
                .build();
        TypeSpec className = TypeSpec.classBuilder(abstractCreatorClassName)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addField(productField)
                .addMethod(getAbstractMethod)
                .addMethod(getOperationMethod)
                .build();
        this.createTypeFile(className,package_name,pathname);
    }
    public void createProductInterface() {
        TypeSpec interfaceName = TypeSpec
                .interfaceBuilder(productInterfacedName)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(MethodSpec
                        .methodBuilder("printName")
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .build()).build();
        this.createTypeFile(interfaceName,package_name,pathname);
    }

    public void generateProductClass(){
        ClassName className1 = ClassName.get(package_name,productInterfacedName);
        for (String c_name:productClassName) {
            MethodSpec getDepartureMethod = MethodSpec.methodBuilder("printName")
                    .addModifiers(Modifier.PUBLIC)
                    .addStatement("$T.out.println($S)", System.class, c_name)
                    .build();
            TypeSpec className = TypeSpec.classBuilder(c_name)
                    .addSuperinterface(className1)
                    .addMethod(getDepartureMethod)
                    .build();
            logger.info("Sending the typespec to the file" );
            this.createTypeFile(className,package_name,pathname);

        }
    }

}
