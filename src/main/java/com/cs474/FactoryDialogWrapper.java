package com.cs474;

import com.assignment1.FactoryBuilder;
import com.assignment1.PatternGeneration;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import com.intellij.ui.components.JBLabel;
import com.intellij.uiDesigner.core.AbstractLayout;
import com.intellij.util.ui.GridBag;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

import static com.intellij.util.ui.JBUI.insets;

public class FactoryDialogWrapper extends DialogWrapper {
    private JPanel panel = new JPanel(new GridBagLayout());
    private JTextField txtProductInterface = new JTextField();
    private JTextField txtProductClass = new JTextField();
    private JTextField txtAbstractCreatorClass = new JTextField();
    private JTextField txtCreatorClass = new JTextField();
    private JTextField txtPathName = new JTextField();
    private Project project;
    private PatternGeneration pattern;
    String path;

    protected FactoryDialogWrapper(Project project, boolean canBeParent) {
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
        panel.add(label("Product Interface Name"),gb.nextLine().next().weightx(0.2));
        panel.add(txtProductInterface,gb.nextLine().next().weightx(0.8));
        panel.add(label("Product Class Names(separated by ',')"),gb.nextLine().next().weightx(0.2));
        panel.add(txtProductClass,gb.nextLine().next().weightx(0.8));
        panel.add(label("Abstract Creator Class Name"),gb.nextLine().next().weightx(0.2));
        panel.add(txtAbstractCreatorClass,gb.nextLine().next().weightx(0.8));
        panel.add(label("Creator Class Names(separated by ',')"),gb.nextLine().next().weightx(0.2));
        panel.add(txtCreatorClass,gb.nextLine().next().weightx(0.8));
        panel.add(label("Folder Name(generated files will be placed)"),gb.nextLine().next().weightx(0.2));
        panel.add(txtPathName,gb.nextLine().next().weightx(0.8));
        return panel;
    }

    @Override
    protected void doOKAction() {
        String productInterface = txtProductInterface.getText();
        String productClasses = txtProductClass.getText();
        String abstractCreator = txtAbstractCreatorClass.getText();
        String creatorClasses = txtCreatorClass.getText();
        String pathname = txtPathName.getText();
        if (productInterface.equals("")||productClasses.equals("")||abstractCreator.equals("")||creatorClasses.equals("")
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
        findName.setSimpleName(productInterface);
        findName.setSimpleName(abstractCreator);
        findName.setListName(productClasses);
        findName.setListName(creatorClasses);
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
        FactoryBuilder factory = new FactoryBuilder();
        factory.setproductInterfacedName(productInterface);
        ArrayList<String> productClassesList = new ArrayList<>(Arrays.asList(productClasses.split("\\s*,\\s*")));
        factory.setproductClassName(productClassesList);
        factory.setabstractCreatorClassName(abstractCreator);
        ArrayList<String> creatorClassesList = new ArrayList<>(Arrays.asList(creatorClasses.split("\\s*,\\s*")));
        factory.setcreatorClassName(creatorClassesList);
        pattern = factory.callPattern();
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
