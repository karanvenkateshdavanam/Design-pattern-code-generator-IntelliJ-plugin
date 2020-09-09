package com.cs474;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Hashtable;

public class CheckHashTableTest {
    String testFile;
    String testFile1;
    String testFile2;
    String testFile3;
    String negativeTestFile;
    NameClashAnalyzer check;
    Hashtable<String, ArrayList<String>> storeTypeData;
    ArrayList<String> classList;
    ArrayList<String> classList1;

    /** There are three test cases to check if the hashtable used to store the type information(class/interface names) of the existing .java files in
     * in the current project is parsed properly using the ParsingFileToAST()(this is a method which uses eclipse AST parser using ASTVisitor)
     * and stored in storeClassNames multi-value hashtable.
     */
    @Before
    public void init(){
        classList = new ArrayList<>();
        classList1 = new ArrayList<>();
        classList.add("Handler");
        classList.add("Element2");
        classList1.add("Element");
        classList1.add("Element1");
        storeTypeData = new Hashtable<>();
        check = new NameClashAnalyzer();
        testFile = "package chain;\n" +
                "\n" +
                "import java.lang.Boolean;\n" +
                "\n" +
                "public abstract class Handler {\n" +
                "  private Handler successor;\n" +
                "\n" +
                "  public Handler() {\n" +
                "  }\n" +
                "\n" +
                "  public Handler(Handler successor) {\n" +
                "    this.successor = successor;\n" +
                "  }\n" +
                "\n" +
                "  public void handleRequest() {\n" +
                "    // Forwarding to successor(if any)\n" +
                "    if (successor != null) {\n" +
                "      successor.handleRequest();\n" +
                "    }\n" +
                "  }\n" +
                "\n" +
                "  public Boolean check() {\n" +
                "    // Checking condition if receiver can handle it....\n" +
                "    return false;\n" +
                "  }\n" +
                "}";
        testFile1 ="package visitor;\n" +
                "\n" +
                "public abstract class Element {\n" +
                "  public abstract void accept(Visitor visitor);\n" +
                "}";
        testFile2 ="package visitor;\n" +
                "\n" +
                "public abstract class Element1 {\n" +
                "  public abstract void accept(Visitor visitor);\n" +
                "}";
        testFile3 ="package chain;\n" +
                "\n" +
                "public abstract class Element2 {\n" +
                "  public abstract void accept(Visitor visitor);\n" +
                "}";
        negativeTestFile = "Empty";
    }
   @Test
   public void Test(){
        check.ParsingFileToAST(testFile);
        storeTypeData = check.storeClassNames;
        Assert.assertNotEquals(0,storeTypeData.size());

   }
    @Test
    public void Test1(){
        check.ParsingFileToAST(negativeTestFile);
        storeTypeData = check.storeClassNames;
        Assert.assertEquals(0,storeTypeData.size());
    }
    @Test
    public void Test2(){
        check.ParsingFileToAST(testFile);
        check.ParsingFileToAST(testFile1);
        check.ParsingFileToAST(testFile2);
        check.ParsingFileToAST(testFile3);
        storeTypeData = check.storeClassNames;
        Assert.assertEquals(classList,storeTypeData.get("chain"));
        Assert.assertEquals(classList1,storeTypeData.get("visitor"));
    }

}
