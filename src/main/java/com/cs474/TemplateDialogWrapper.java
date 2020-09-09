package com.cs474;

import com.assignment1.PatternGeneration;
import com.assignment1.TemplateBuilder;
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

public class TemplateDialogWrapper extends DialogWrapper {
    private JPanel panel = new JPanel(new GridBagLayout());
    private JTextField txtSwitchClass = new JTextField();
    private JTextField txtTemplateClass = new JTextField();
    private JTextField txtImplementingClasses = new JTextField();
    private JTextField txtVaryMethod = new JTextField();
    private JTextField txtPathName = new JTextField();
    private Project project;
    private PatternGeneration pattern;
    String path;

    protected TemplateDialogWrapper(Project project, boolean canBeParent) {
        super(project, canBeParent);
        init();
        setTitle("Class generator");
        this.project=project;
        this.path = project.getBasePath()+"/src";
    }


    @Override
    protected JComponent createCenterPanel() {
        GridBag gb = new GridBag().setDefaultInsets(insets(0, 0, AbstractLayout.DEFAULT_VGAP, AbstractLayout.DEFAULT_HGAP))
                .setDefaultWeightX(1.0)
                .setDefaultFill(GridBagConstraints.HORIZONTAL);

        panel.setPreferredSize(new Dimension(400,200));
        panel.add(label("Switch Class Name"),gb.nextLine().next().weightx(0.2));
        panel.add(txtSwitchClass,gb.nextLine().next().weightx(0.8));
        panel.add(label("Template Class Name"),gb.nextLine().next().weightx(0.2));
        panel.add(txtTemplateClass,gb.nextLine().next().weightx(0.8));
        panel.add(label("Implementing Class Names(separated by ',')"),gb.nextLine().next().weightx(0.2));
        panel.add(txtImplementingClasses,gb.nextLine().next().weightx(0.8));
        panel.add(label("Variant Method Name"),gb.nextLine().next().weightx(0.2));
        panel.add(txtVaryMethod,gb.nextLine().next().weightx(0.8));
        panel.add(label("Folder Name(generated files will be placed)"),gb.nextLine().next().weightx(0.2));
        panel.add(txtPathName,gb.nextLine().next().weightx(0.8));

        return panel;
    }

    @Override
    protected void doOKAction() {
        String switchClass = txtSwitchClass.getText();
        String templateClass = txtTemplateClass.getText();
        String implementingClasses = txtImplementingClasses.getText();
        String varyMethod = txtVaryMethod.getText();
        String pathname = txtPathName.getText();
        if (switchClass.equals("")||templateClass.equals("")||implementingClasses.equals("")||varyMethod.equals("")
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
        findName.setSimpleName(switchClass);
        findName.setSimpleName(templateClass);
        findName.setListName(implementingClasses);
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
        TemplateBuilder template = new TemplateBuilder();
        template.setswitchClass(switchClass);
        ArrayList<String> implementingClassesList = new ArrayList<>(Arrays.asList(implementingClasses.split("\\s*,\\s*")));
        template.setimplementingClasses(implementingClassesList);
        template.settemplateClass(templateClass);
        template.setvaryMethod(varyMethod);
        pattern = template.callPattern();
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
