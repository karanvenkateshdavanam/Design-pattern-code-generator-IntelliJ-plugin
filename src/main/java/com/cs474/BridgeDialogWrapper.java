package com.cs474;

import com.assignment1.BridgeBuilder;
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

public class BridgeDialogWrapper extends DialogWrapper {
    private JPanel panel = new JPanel(new GridBagLayout());
    private JTextField txtBridgeInterface = new JTextField();
    private JTextField txtBridgeClasses = new JTextField();
    private JTextField txtImplementorInterface = new JTextField();
    private JTextField txtImplementorClasses = new JTextField();
    private JTextField txtPathName = new JTextField();
    private PatternGeneration pattern;
    private String path;
    private Project project;
    static final Logger logger = LoggerFactory.getLogger("BridgeDialogWrapper");

    protected BridgeDialogWrapper(Project project, boolean canBeParent) {
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
        panel.add(label("Bridge Interface Name"),gb.nextLine().next().weightx(0.2));
        panel.add(txtBridgeInterface,gb.nextLine().next().weightx(0.8));
        panel.add(label("Bridge Class Names(separated by ',')"),gb.nextLine().next().weightx(0.2));
        panel.add(txtBridgeClasses,gb.nextLine().next().weightx(0.8));
        panel.add(label("Implementor Interface Name"),gb.nextLine().next().weightx(0.2));
        panel.add(txtImplementorInterface,gb.nextLine().next().weightx(0.8));
        panel.add(label("Implementor Class Names(separated by ',')"),gb.nextLine().next().weightx(0.2));
        panel.add(txtImplementorClasses,gb.nextLine().next().weightx(0.8));
        panel.add(label("Folder Name(generated files will be placed)"),gb.nextLine().next().weightx(0.2));
        panel.add(txtPathName,gb.nextLine().next().weightx(0.8));

        return panel;
    }

    @Override
    protected void doOKAction() {
        String bridgeInterface = txtBridgeInterface.getText();
        String bridgeClasses = txtBridgeClasses.getText();
        String implementorInterface = txtImplementorInterface.getText();
        String implementorClasses = txtImplementorClasses.getText();
        String pathname = txtPathName.getText();
        if (bridgeInterface.equals("")||bridgeClasses.equals("")||implementorInterface.equals("")||implementorClasses.equals("")
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
        findName.setSimpleName(bridgeInterface);
        findName.setSimpleName(implementorInterface);
        findName.setListName(bridgeClasses);
        findName.setListName(implementorClasses);
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
        BridgeBuilder bridge = new BridgeBuilder();
        bridge.setBridgeInterface(bridgeInterface);
        ArrayList<String> bridgeClassesList = new ArrayList<>(Arrays.asList(bridgeClasses.split("\\s*,\\s*")));
        bridge.setBridgeClasses(bridgeClassesList);
        bridge.setImplementorInterface(implementorInterface);
        ArrayList<String> implementorClassesList = new ArrayList<>(Arrays.asList(implementorClasses.split("\\s*,\\s*")));
        bridge.setimplementorClasses(implementorClassesList);
        pattern = bridge.callPattern();
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
