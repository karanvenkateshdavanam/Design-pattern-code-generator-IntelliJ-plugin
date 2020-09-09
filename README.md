# Homework 3 : Implementation of the name clashing functionality in the IntelliJ plugin with the design pattern code generator
### Karan Venkatesh Davanam 
### kdavan2@uic.edu 673740735
---
### Gradle command to run the project:
- gradlew clean
- gradlew build (build the project)
- gradlew runIde (to run the application in IDE mode)
- gradlew test (run the testcases filtered )

### Sample Input and steps to work with plugin is present in the "Steps to check for name clash.docx"

### Test cases executed when running the "gradlew test" command:
- com.cs474.CheckHashTableTest (contains 3 test cases)
  The details of the existing Type(Class/Interface) in the open project is stored inside a hashtable. Test cases are written to check if the hashtable is populating properly and test cases have been written to check if the Eclipse AST parser is parsing the Strings correctly.

- com.cs474.NameClashTest (contains 2 test cases)
  This test case checks if the integration between the Dialogwrapper and the NameClashAnalyzer Class works as expected. The object the that is returned from the NameClashAnalyzer to the Dialog wrapper is a hastable datastructure that contains the names of the Type that have nameclash. 

### Tools and libraries used:
- Intellij
- SLF4J -- Logback
- Junit
- Gradle
- JavaPoet (For Assignment1)
- DialogWrapper
- ToolWindowFactory
- Eclipse AST parser and PSI Elements(For Assignment 3)
---
### Code Implementation
As part of the Assignment I have integrated NameClashAnalyzer class which contains the following methods and this class is called by each design pattern DialogWrapper.
- Setter and Getter for each variable declared in the class

- GetProjectFiles():
  This component uses Project Model elements and PSI elements to get all the .java files by taking into account Module Scope of the open project. The .java as stored as virtual files and the contents of each virtual files is sent to one more method ParsingFileToAST().

- ParsingFileToAST():
  This method mainly uses ASTVisitor class from Eclipse AST parser to parse the contents of the files that sent from GetProjectFiles(). This method extracts the TypeDeclarartion nodes from each file and stores in the hastable(storeClassNames) while taking into account the package scope.

- CheckHashTable():
  This creates a new hashtable that contains all the names of the Types that have name clash by taking into account the module scope and package scope. This hashtable is returned to the dialogwrappers through getter method. 

Changes done in DialogWrapper class for a particular pattern:
A new Dialog box is added to inform the users of the plugin to change the names of the Types that have name clash in that package scope.
---
### Scope Consideration:
- Same Module and Same Package Scope:
 If the name of the Class/Interface given by user is same as any of the Type present then name clash will occur.
- Same Module and Different Package Scope:
   If the name of the Class/Interface given by user is same as any of the Type present(different package) then name clash won't occur due to different package scope.
- Different Module and Same Package Scope:
 If the name of the Class/Interface given by user is same as any of the Type present then name clash won't occur due to different module scope.(package scope here won't be considered)

If the user wants to export the same Types from different scopes into a single java file then it is upto to the programmer/user to resolve the name clash by providing fully qualified names.

---
### Result
The plugin checks the name clash based upon the user selection for below patterns:
- Bridge
- Facade
- Factory
- Visitor
- Template
- AbstractFactory
- Strategy
- Chain
---
### Assumptions:
- The Module Scope considered is the module with the same name as the project. So all the generated patterns are kept in the module where the name of the module is same as the project.(Main Module)
- Any other module present in the project would be of different module scope.
- Each pattern generated have there own folder where they are generated . This folder is assumed to be the package scope of the generated pattern and the module scope would be the module that has the same as the project.
---
### Thinks to improve upon:
If Assignment 1 and Assignment 2 were modularized and planned properly then the refactoring for Assignment 3 might have been easier.

