package com.assignment1;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Paths;

public abstract class PatternGeneration {
     static final Logger logger = LoggerFactory.getLogger("PatternGeneration");
     public abstract void generatePattern(String pathname,String path);

     public void createTypeFile(TypeSpec typeName,String package_name,String pathname){
          JavaFile javaFile;
          javaFile = JavaFile.builder(package_name,typeName)
                  .addFileComment("")
                  .build();
          try {
               logger.info("writing to the output.java file");

               javaFile.writeTo(Paths.get(pathname));
          } catch (IOException ex) {
               logger.error("writing to the file error occured {}",ex.getMessage());
               System.out.println("An exception" + ex.getMessage());
          }
     }
}
