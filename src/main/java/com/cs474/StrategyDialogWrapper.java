package com.cs474;

import com.assignment1.PatternGeneration;
import com.assignment1.StrategyBuilder;
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

public class StrategyDialogWrapper extends DialogWrapper {
    private JPanel panel = new JPanel(new GridBagLayout());
    private JTextField txtInterfaceName = new JTextField();
    private JTextField txtContextClassName = new JTextField();
    private JTextField txtClassName = new JTextField();
    private JTextField txtPathName = new JTextField();
    private Project project;
    private PatternGeneration pattern;
    String path;
    static final Logger logger = LoggerFactory.getLogger("StrategyDialogWrapper");

    protected StrategyDialogWrapper(Project project, boolean canBeParent) {
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
        panel.add(label("Interface Name"),gb.nextLine().next().weightx(0.2));
        panel.add(txtInterfaceName,gb.nextLine().next().weightx(0.8));
        panel.add(label("Context Class Name"),gb.nextLine().next().weightx(0.2));
        panel.add(txtContextClassName,gb.nextLine().next().weightx(0.8));
        panel.add(label("Interface Concrete Class Names(separated by ',')"),gb.nextLine().next().weightx(0.2));
        panel.add(txtClassName,gb.nextLine().next().weightx(0.8));
        panel.add(label("Folder Name(generated files will be placed)"),gb.nextLine().next().weightx(0.2));
        panel.add(txtPathName,gb.nextLine().next().weightx(0.8));

        return panel;
    }

    @Override
    protected void doOKAction() {
        String interfaceName = txtInterfaceName.getText();
        String contextClassName = txtContextClassName.getText();
        String className = txtClassName.getText();
        String pathname = txtPathName.getText();
        if (interfaceName.equals("")||contextClassName.equals("")||className.equals("")
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
        findName.setSimpleName(interfaceName);
        findName.setSimpleName(contextClassName);
        findName.setListName(className);
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
        logger.info("User input taken in successfully");
        logger.info("Ok button clicked");
        StrategyBuilder strategy = new StrategyBuilder();
        strategy.setintefaceName(interfaceName);
        strategy.setcontextclassName(contextClassName);
        ArrayList<String> classNameList = new ArrayList<>(Arrays.asList(className.split("\\s*,\\s*")));
        strategy.setclassName(classNameList);
        pattern = strategy.callPattern();
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
