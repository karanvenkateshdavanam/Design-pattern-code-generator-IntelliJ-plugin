package com.cs474;

import com.assignment1.ChainBuilder;
import com.assignment1.PatternGeneration;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.components.JBLabel;
import com.intellij.uiDesigner.core.AbstractLayout;
import com.intellij.util.ui.GridBag;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

import static com.intellij.util.ui.JBUI.insets;

public class ChainDialogWrapper extends DialogWrapper {
    private JPanel panel = new JPanel(new GridBagLayout());
    private JTextField txtHandlerAbstractClass = new JTextField();
    private JTextField txtHandleRequestMethod = new JTextField();
    private JTextField txtRequestHandlingClasses = new JTextField();
    private JTextField txtPathName = new JTextField();
    private PatternGeneration pattern;
    private Project project;
    String path;
    static final Logger logger = LoggerFactory.getLogger("ChainDialogWrapper");

    protected ChainDialogWrapper(Project project, boolean canBeParent) {
        super(project, canBeParent);
        init();
        setTitle("Class generator");
        this.project = project;
        this.path = project.getBasePath()+"/src";
    }


    @Override
    protected JComponent createCenterPanel() {
        GridBag gb = new GridBag().setDefaultInsets(insets(0, 0, AbstractLayout.DEFAULT_VGAP, AbstractLayout.DEFAULT_HGAP))
                .setDefaultWeightX(1.0)
                .setDefaultFill(GridBagConstraints.HORIZONTAL);

        panel.setPreferredSize(new Dimension(400,200));
        panel.add(label("Handler Abstract Class Name"),gb.nextLine().next().weightx(0.2));
        panel.add(txtHandlerAbstractClass,gb.nextLine().next().weightx(0.8));
        panel.add(label("Handle Request Method Name"),gb.nextLine().next().weightx(0.2));
        panel.add(txtHandleRequestMethod,gb.nextLine().next().weightx(0.8));
        panel.add(label("Request Handling Class Names(separated by ',')"),gb.nextLine().next().weightx(0.2));
        panel.add(txtRequestHandlingClasses,gb.nextLine().next().weightx(0.8));
        panel.add(label("Folder Name(generated files will be placed)"),gb.nextLine().next().weightx(0.2));
        panel.add(txtPathName,gb.nextLine().next().weightx(0.8));

        return panel;
    }

    @Override
    protected void doOKAction() {
        String handlerAbstractClass = txtHandlerAbstractClass.getText();
        String handleRequestMethod = txtHandleRequestMethod.getText();
        String requestHandlingClasses = txtRequestHandlingClasses.getText();
        String pathname = txtPathName.getText();
        if (handlerAbstractClass.equals("")||handleRequestMethod.equals("")||requestHandlingClasses.equals("")
                || pathname.equals(""))
        {
            Messages.showErrorDialog((Project)null, "Please fill all the text fields "
                    , "Text box empty");
            return;
        }
        ArrayList<String> checkList;
        final StringBuilder builder = new StringBuilder();
        NameClashAnalyzer findName = new NameClashAnalyzer();
        findName.setProjectName(project);
        findName.setScopeName(pathname);
        findName.setSimpleName(handlerAbstractClass);
        findName.setListName(requestHandlingClasses);
        findName.GetProjectFiles();
        checkList=findName.getClashList();
        if(checkList.size()!=0){
            for (int i = 0; i < checkList.size(); i++) {
                builder.append(checkList.get(i)+" ");

            }
            Messages.showErrorDialog((Project)null, "Please Change the values of "+builder+
                    " since Types of such name existing in the package or change the pathname/packageName ", "Name Clash Error");
            return;
        }
        logger.info("Ok button clicked");
        logger.info("User input taken in successfully");
        ChainBuilder chain = new ChainBuilder();
        chain.sethandlerAbstractClass(handlerAbstractClass);
        chain.sethandleRequestMethod(handleRequestMethod);
        ArrayList<String> requestHandlingClassesList = new ArrayList<>(Arrays.asList(requestHandlingClasses.split("\\s*,\\s*")));
        chain.setrequestHandlingClasses(requestHandlingClassesList);
        pattern = chain.callPattern();
        pattern.generatePattern(pathname,path);
        super.doOKAction();
    }

    private JComponent label(String text){
        JBLabel label = new JBLabel(text);
        label.setComponentStyle(UIUtil.ComponentStyle.SMALL);
        label.setFontColor(UIUtil.FontColor.BRIGHTER);
        label.setBorder(JBUI.Borders.empty(0,5,2,0));
        return label;
    }
}
