package com.assignment1;

import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;

public class Chain extends PatternGeneration {
    public String handlerAbstractClass;
    public ArrayList<String> requestHandlingClasses;
    public String handleRequestMethod;
    public String package_name;
    public String pathname;


    public void generatePattern(String pathname,String path){
        this.package_name = pathname;
        this.pathname = path;
        this.createAbstractHandlerClass();
        this.createRequestHandlingClass();
    }

    public void createAbstractHandlerClass(){
        ClassName className = ClassName.get(package_name,handlerAbstractClass);
        FieldSpec field = FieldSpec.builder(className,"successor", Modifier.PRIVATE).build();
        ArrayList<MethodSpec> methodNameList = new ArrayList<>();
        MethodSpec constructorMethod = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .build();
        methodNameList.add(constructorMethod);
        MethodSpec constructorMethod1 = MethodSpec.constructorBuilder()
                .addParameter(className,"successor")
                .addModifiers(Modifier.PUBLIC)
                .addStatement("this.successor = successor")
                .build();
        methodNameList.add(constructorMethod1);
        CodeBlock codeBlock = CodeBlock.builder()
                .beginControlFlow("if (successor != null)")
                .addStatement("successor.$N()",handleRequestMethod)
                .endControlFlow()
                .build();
        MethodSpec forwardingMethod = MethodSpec.methodBuilder(handleRequestMethod)
                .addModifiers(Modifier.PUBLIC)
                .addComment("Forwarding to successor(if any)")
                .addCode(codeBlock)
                .build();
        methodNameList.add(forwardingMethod);
        MethodSpec checkMethod = MethodSpec.methodBuilder("check")
                .addModifiers(Modifier.PUBLIC)
                .returns(Boolean.class)
                .addComment("Checking condition if receiver can handle it....")
                .addStatement("return false")
                .build();
        methodNameList.add(checkMethod);
        TypeSpec handlerClassName = TypeSpec.classBuilder(handlerAbstractClass)
                .addModifiers(Modifier.PUBLIC,Modifier.ABSTRACT)
                .addField(field)
                .addMethods(methodNameList)
                .build();
        this.createTypeFile(handlerClassName,package_name,pathname);
        methodNameList.clear();
    }
    public void createRequestHandlingClass(){
        ClassName superName = ClassName.get(package_name,handlerAbstractClass);
        for(String c_name:requestHandlingClasses){
            MethodSpec constructorMethod = MethodSpec.constructorBuilder()
                    .addParameter(superName,"successor")
                    .addModifiers(Modifier.PUBLIC)
                    .addStatement("super(successor)")
                    .build();

            CodeBlock codeBlock = CodeBlock.builder()
                    .addStatement("System.out.println(\"$N handling the request\")", c_name)
                    .build();
           if(requestHandlingClasses.get(requestHandlingClasses.size() - 1) != c_name) {
                codeBlock = CodeBlock.builder()
                       .beginControlFlow("if (check())")
                       .addStatement("System.out.println(\"$N handling the request\")", c_name)
                       .endControlFlow()
                       .beginControlFlow("else")
                       .addStatement("System.out.println(\"$N passing the request\")", c_name)
                       .addStatement("super.$N()", handleRequestMethod)
                       .endControlFlow()
                       .build();
           }
            MethodSpec overrideMethod = MethodSpec.methodBuilder(handleRequestMethod)
                    .addModifiers(Modifier.PUBLIC)
                    .addCode(codeBlock)
                    .build();
            TypeSpec className = TypeSpec.classBuilder(c_name)
                    .superclass(superName)
                    .addModifiers(Modifier.PUBLIC)
                    .addMethod(constructorMethod)
                    .addMethod(overrideMethod)
                    .build();
            this.createTypeFile(className,package_name,pathname);
        }

    }


}
