package com.cs474;

import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.indexing.FileBasedIndex;
import org.eclipse.jdt.core.dom.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Hashtable;

public class NameClashAnalyzer {
    private ArrayList<String> checkList = new ArrayList<>();
    private ArrayList<String> clashList = new ArrayList<>();
    private ArrayList<String> list;
    private String scopeName;
    private Project project;
    static final Logger logger = LoggerFactory.getLogger("NameClashAnalyzer");
    Hashtable<String,ArrayList<String>> storeClassNames = new Hashtable<>();

    public void setProjectName(Project projectName){
        this.project = projectName;
    }
    public void setSimpleName(String simpleName){
        checkList.add(simpleName);
    }

    public void setListName(String listName){
        ArrayList<String> typeList = new ArrayList<>(Arrays.asList(listName.split("\\s*,\\s*")));
        checkList.addAll(typeList);
    }

    public void setName(String listName, String num){
        String typeName;
        checkList.add(listName);
        int num1 = Integer.parseInt(num);
        for(int i=1;i<=num1;i++){
            typeName = listName+i;
            checkList.add(typeName);
        }

    }

    public void setNameNum(String listName, String num){
        String typeName;
        ArrayList<String> typeList = new ArrayList<>(Arrays.asList(listName.split("\\s*,\\s*")));
        checkList.addAll(typeList);
        int num1 = Integer.parseInt(num);
        for(int i=1;i<=num1;i++) {
            for (String i_name : typeList) {
                typeName = i_name + i;
                checkList.add(typeName);
            }
        }
    }


    public void setScopeName(String scopeName){
        this.scopeName = scopeName;
    }

    public ArrayList<String> getClashList(){
        return clashList;
    }

    /**This component uses Project Model elements and PSI elements to get all the .java files by taking into account
     * Module Scope of the open project.
     * The .java as stored as virtual files and the contents of each virtual files is sent to one more method ParsingFileToAST().
     */
    public void GetProjectFiles(){
        Module finalMod = null;
        String nameProject = project.getName();
        ModuleManager x = ModuleManager.getInstance(project);
        Module[] b= x.getModules();
        for (Module c:b) {
            if(c.getName().equals(nameProject)){
                finalMod = c;
            }

        }
        assert finalMod != null;
        final Collection<VirtualFile>  javaFiles = FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, JavaFileType.INSTANCE,
                GlobalSearchScope.moduleScope(finalMod));
        for (VirtualFile virtualFile: javaFiles) {
            final PsiFile file = PsiManager.getInstance(project).findFile(virtualFile);
            logger.info(String.valueOf(file));
            if (file instanceof PsiJavaFile) {
                FileDocumentManager documentManager = FileDocumentManager.getInstance();
                Document document = documentManager.getDocument(virtualFile);
                String contents = document.getText();
                logger.info(contents);
                logger.info(((PsiJavaFile) file).getPackageName());
                this.ParsingFileToAST(contents);
            }
        }
        this.CheckHashTable();
    }

    /** This method mainly uses ASTVisitor class from Eclipse AST parser to parse the contents of the files that sent from GetProjectFiles().
     * This method extracts the TypeDeclarartion nodes from each file and stores in the hastable(storeClassNames) while taking into account
     * the package scope.
     * */
    public void ParsingFileToAST(String fileContents){
        ASTParser parser = ASTParser.newParser(AST.JLS13);
        parser.setSource(fileContents.toCharArray());
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
        cu.accept(new ASTVisitor() {
            String packageName="";

            public boolean visit(TypeDeclaration node) {
                String className=node.getName().getFullyQualifiedName();
                if (packageName!="") {
                    logger.info(packageName + "." + className);
                }
                else {
                    logger.info(className);
                }
                if (storeClassNames.containsKey(packageName)) {
                    list = storeClassNames.get(packageName);
                    list.add(className);
                } else {
                    list = new ArrayList<String>();
                    list.add(className);
                    storeClassNames.put(packageName,list);
                }


                return false;
            }

            public boolean visit(PackageDeclaration node) {
                packageName=node.getName().getFullyQualifiedName();
                return false;
            }
        });

    }

    /** This creates a new hashtable that contains all the names of the Types that have name clash by taking into account the module scope
     * and package scope.
     * This hashtable is returned to the dialog wrappers through getter method.
     * */
    public void CheckHashTable(){
        for (String checkName:checkList) {
            if (storeClassNames.containsKey(scopeName)) {
                if (storeClassNames.get(scopeName).contains(checkName)) {
                    clashList.add(checkName);
                }
            }
        }

    }


}
