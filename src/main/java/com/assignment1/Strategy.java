package com.assignment1;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;

public class Strategy extends PatternGeneration {

    public String intefaceName;
    public ArrayList<String> className;
    public String contextclassName;
    public String package_name;
    public String pathname;
    static final Logger logger = LoggerFactory.getLogger("Strategy");




   public void generatePattern(String pathname,String path) {
        logger.info("Pattern is being constructed by the corresponding pattern maker");
        this.package_name = pathname;
        this.pathname = path;
        this.createinterface();
        this.generateclass();
        this.contextclass();
    }

    public void contextclass(){
        ClassName className1 = ClassName.get(package_name,intefaceName);
        MethodSpec constructorSpec = MethodSpec.constructorBuilder()
                .addParameter(className1, "quack")
                .addStatement("this.quack = quack")
                .addModifiers(Modifier.PUBLIC).build();
        MethodSpec routemethodSpec = MethodSpec.methodBuilder("operation")
                .addStatement("quack.algorithm()")
                .addModifiers(Modifier.PUBLIC).build();
        MethodSpec setmethodSpec = MethodSpec.methodBuilder("setStrategy")
                .addParameter(className1, "quack")
                .addStatement("this.quack = quack")
                .addModifiers(Modifier.PUBLIC).build();
        TypeSpec className = TypeSpec.classBuilder(contextclassName)
                .addModifiers(Modifier.PUBLIC)
                .addField(className1, "quack", Modifier.PRIVATE)
                .addMethod(constructorSpec)
                .addMethod(routemethodSpec)
                .addMethod(setmethodSpec)
                .build();
        this.createTypeFile(className,package_name,pathname);

    }

    public void generateclass() {
        ClassName className1 = ClassName.get(package_name,intefaceName);
        // for classname in array of class
        for (String c_name:className) {
            MethodSpec getDepartureMethod = MethodSpec.methodBuilder("algorithm")
                    .addModifiers(Modifier.PUBLIC)
                    .addStatement("//Implement the code")
                    .build();
            TypeSpec className = TypeSpec.classBuilder(c_name)
                    .addSuperinterface(className1)
                    .addMethod(getDepartureMethod)
                    .build();
            this.createTypeFile(className,package_name,pathname);

        }

    }

    public void createinterface() {
       TypeSpec className = TypeSpec
               .interfaceBuilder(intefaceName)
               .addModifiers(Modifier.PUBLIC)
               .addMethod(MethodSpec
               .methodBuilder("algorithm")
               .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                       .build()).build();
        logger.info("Sending the typespec to the file" );
        this.createTypeFile(className,package_name,pathname);
    }


}
