package com.cs474;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NameClashTest {
    String switchClass;
    String templateClass;
    String implementingClasses;
    String varyMethod;
    String pathname;
    String TestFile;
    String TestFile1;
    NameClashAnalyzer check;
    int n;
    @Before
    public void init(){
        TestFile ="package template;\n" +
                "\n" +
                "public class SwitchClass {\n" +
                "  private Template method;\n" +
                "\n" +
                "  public SwitchClass(Template method) {\n" +
                "    this.method = method;\n" +
                "  }\n" +
                "\n" +
                "  public void operation() {\n" +
                "    method.operation();\n" +
                "  }\n" +
                "\n" +
                "  public void changeMethod(Template method) {\n" +
                "    this.method = method;\n" +
                "  }\n" +
                "}";
        TestFile1 ="package template;\n" +
                "\n" +
                "public class Test {\n" +
                "  private Template method;\n" +
                "\n" +
                "  public SwitchClass(Template method) {\n" +
                "    this.method = method;\n" +
                "  }\n" +
                "\n" +
                "  public void operation() {\n" +
                "    method.operation();\n" +
                "  }\n" +
                "\n" +
                "  public void changeMethod(Template method) {\n" +
                "    this.method = method;\n" +
                "  }\n" +
                "}";
        switchClass = "SwitchClass";
        templateClass = "Template";
        implementingClasses = "Implement1";
        varyMethod = "varyMethod";
        pathname = "template";
        check = new NameClashAnalyzer();
    }
    @Test
    public void Test(){
        check.ParsingFileToAST(TestFile);
        check.setScopeName(pathname);
        check.setSimpleName(switchClass);
        check.setSimpleName(templateClass);
        check.setListName(implementingClasses);
        check.CheckHashTable();
        n = check.getClashList().size();
        Assert.assertNotEquals(0,n);
    }
    @Test
    public void Test1(){
        check.ParsingFileToAST(TestFile1);
        check.setScopeName(pathname);
        check.setSimpleName(switchClass);
        check.setSimpleName(templateClass);
        check.setListName(implementingClasses);
        check.CheckHashTable();
        n = check.getClashList().size();
        Assert.assertEquals(0,n);
    }
}
