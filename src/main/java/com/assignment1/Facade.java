package com.assignment1;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;

public class Facade extends PatternGeneration {
    public String facadeInterfacedName;
    public ArrayList<String> subSystemClassName;
    public String package_name ;
    public String pathname;
    static final Logger logger = LoggerFactory.getLogger("Facade");

    public void generatePattern(String pathname,String path){
        logger.info("Pattern is being constructed by the corresponding pattern maker");
        this.package_name = pathname;
        this.pathname = path;
        this.createSubsystemClass();
        this.createFacadeClass();
    }

    public void createSubsystemClass(){
        for (String i_name : subSystemClassName){
            MethodSpec getMethod = MethodSpec.methodBuilder("task")
                    .addModifiers(Modifier.PUBLIC)
                    .returns(String.class)
                    .addStatement("return $S",i_name)
                    .build();
            TypeSpec className = TypeSpec.classBuilder(i_name)
                    .addModifiers(Modifier.PUBLIC)
                    .addMethod(getMethod)
                    .build();
            this.createTypeFile(className,package_name,pathname);
        }
    }
    public void createFacadeClass(){
        ArrayList<FieldSpec> fieldNameList = new ArrayList<>();
        FieldSpec field;
        ClassName type;
        String addCodeInitializer = "";
        String addReturnCode = "";
        for(String c_name:subSystemClassName){
            type = ClassName.get(package_name,c_name);
            field = FieldSpec.builder(type,c_name.toLowerCase(), Modifier.PRIVATE).build();
            fieldNameList.add(field);
            addCodeInitializer = addCodeInitializer + "this."+c_name.toLowerCase()+ "="+ "new "+c_name+"()"+";"+"\n";
            addReturnCode = addReturnCode +"+ "+c_name.toLowerCase()+"."+"task()";
        }

        MethodSpec constructorMethod = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addCode(addCodeInitializer)
                .build();
        MethodSpec facadeMethod = MethodSpec.methodBuilder("facadeTask")
                .addModifiers(Modifier.PUBLIC)
                .returns(String.class)
                .addCode("return "+"\"Facade Interacting with subsystems\""+addReturnCode+";")
                .build();
        TypeSpec className = TypeSpec.classBuilder(facadeInterfacedName)
                .addModifiers(Modifier.PUBLIC)
                .addFields(fieldNameList)
                .addMethod(constructorMethod)
                .addMethod(facadeMethod)
                .build();
        logger.info("Sending the typespec to the file" );
        this.createTypeFile(className,package_name,pathname);
        fieldNameList.clear();

    }
}
