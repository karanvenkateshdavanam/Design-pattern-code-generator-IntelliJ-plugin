package com.cs474;

import com.assignment1.AbstractFactoryBuilder;
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

public class AbstractFactoryDialogWrapper extends DialogWrapper {
    private JPanel panel = new JPanel(new GridBagLayout());
    private JTextField txtFactoryName = new JTextField();
    private JTextField txtProductInterface = new JTextField();
    private JTextField txtProductClassNum = new JTextField();
    private JTextField txtPathName = new JTextField();
    private PatternGeneration pattern;
    private Project project;
    private String path;
    static final Logger logger = LoggerFactory.getLogger("AbstractFactoryDialogWrapper");

    protected AbstractFactoryDialogWrapper(Project project, boolean canBeParent) {
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
        panel.add(label("Factory Class Name"),gb.nextLine().next().weightx(0.2));
        panel.add(txtFactoryName,gb.nextLine().next().weightx(0.8));
        panel.add(label("Product Interface Names(separated by ',')"),gb.nextLine().next().weightx(0.2));
        panel.add(txtProductInterface,gb.nextLine().next().weightx(0.8));
        panel.add(label("Number of Concrete classes"),gb.nextLine().next().weightx(0.2));
        panel.add(txtProductClassNum,gb.nextLine().next().weightx(0.8));
        panel.add(label("Folder Name(generated files will be placed)"),gb.nextLine().next().weightx(0.2));
        panel.add(txtPathName,gb.nextLine().next().weightx(0.8));
        logger.info("Creating the dialog message message");
        return panel;
    }

    @Override
    protected void doOKAction() {
        String factoryName = txtFactoryName.getText();
        String productInterfacedName = txtProductInterface.getText();
        String num = txtProductClassNum.getText();
        String pathname = txtPathName.getText();
        logger.info("Ok button clicked");
        if (productInterfacedName.equals("")||num.equals("")
                || pathname.equals("")||factoryName.equals(""))
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
        findName.setName(factoryName,num);
        findName.setNameNum(productInterfacedName,num);
        findName.GetProjectFiles();
        checkList=findName.getClashList();
        if(checkList.size()!=0){
            for (int i = 0; i < checkList.size(); i++) {
                builder.append(checkList.get(i)+" ");

            }
            Messages.showErrorDialog((Project)null, "Please Change the values of pathname/packageName "+
                    " Since Types of "+builder+"  exists in the package", "Name Clash Error");
            return;
        }
        logger.info("User input taken in successfully");
        int productClassNum = Integer.parseInt(num);
        AbstractFactoryBuilder abstractFactory = new AbstractFactoryBuilder();
        ArrayList<String> productInterfacedNameList = new ArrayList<>(Arrays.asList(productInterfacedName.split("\\s*,\\s*")));
        abstractFactory.setproductInterfacedName(productInterfacedNameList);
        abstractFactory.setproductClassNum(productClassNum);
        abstractFactory.setFactoryName(factoryName);
        pattern = abstractFactory.callPattern();
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
