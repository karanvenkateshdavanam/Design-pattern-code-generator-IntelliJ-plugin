package com.cs474;

import com.assignment1.PatternGeneration;
import com.assignment1.VisitorBuilder;
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

public class VisitorDialogWrapper extends DialogWrapper {
    private JPanel panel = new JPanel(new GridBagLayout());
    private JTextField txtVisitorAbstractClass = new JTextField();
    private JTextField txtVisitorClass = new JTextField();
    private JTextField txtVisitableAbstractClass = new JTextField();
    private JTextField txtVisitableClasses = new JTextField();
    private JTextField txtPathName = new JTextField();
    private Project project;
    private PatternGeneration pattern;
    String path;

    protected VisitorDialogWrapper(Project project, boolean canBeParent) {
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
        panel.add(label("Visitor Abstract Class Name"),gb.nextLine().next().weightx(0.2));
        panel.add(txtVisitorAbstractClass,gb.nextLine().next().weightx(0.8));
        panel.add(label("Visitor Class Name"),gb.nextLine().next().weightx(0.2));
        panel.add(txtVisitorClass,gb.nextLine().next().weightx(0.8));
        panel.add(label("Visitable Abstract Class Name"),gb.nextLine().next().weightx(0.2));
        panel.add(txtVisitableAbstractClass,gb.nextLine().next().weightx(0.8));
        panel.add(label("Visitable Class Names(separated by ',')"),gb.nextLine().next().weightx(0.2));
        panel.add(txtVisitableClasses,gb.nextLine().next().weightx(0.8));
        panel.add(label("Folder Name(generated files will be placed)"),gb.nextLine().next().weightx(0.2));
        panel.add(txtPathName,gb.nextLine().next().weightx(0.8));

        return panel;
    }

    @Override
    protected void doOKAction() {
        String visitorAbstractClass = txtVisitorAbstractClass.getText();
        String visitorClass = txtVisitorClass.getText();
        String visitableAbstractClass = txtVisitableAbstractClass.getText();
        String visitableClasses = txtVisitableClasses.getText();
        String pathname = txtPathName.getText();
        if (visitorAbstractClass.equals("")||visitorClass.equals("")||visitableAbstractClass.equals("")||visitableClasses.equals("")
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
        findName.setSimpleName(visitorAbstractClass);
        findName.setSimpleName(visitorClass);
        findName.setSimpleName(visitableAbstractClass);
        findName.setListName(visitableClasses);
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
        VisitorBuilder visitor = new VisitorBuilder();
        visitor.setvisitorAbstractClass(visitorAbstractClass);
        visitor.setvisitorClass(visitorClass);
        visitor.setvisitableAbstractClass(visitableAbstractClass);
        ArrayList<String> visitableClassesList = new ArrayList<>(Arrays.asList(visitableClasses.split("\\s*,\\s*")));
        visitor.setvisitableClasses(visitableClassesList);
        pattern = visitor.callPattern();
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
