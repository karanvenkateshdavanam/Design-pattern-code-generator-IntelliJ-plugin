package com.cs474;

import com.assignment1.FacadeBuilder;
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

public class FacadeDialogWrapper extends DialogWrapper {
    private JPanel panel = new JPanel(new GridBagLayout());
    private JTextField txtFacadeInterfacedName = new JTextField();
    private JTextField txtSubSystemClassName = new JTextField();
    private JTextField txtPathName = new JTextField();
    private PatternGeneration pattern;
    private Project project;
    private String path;
    static final Logger logger = LoggerFactory.getLogger("FacadeDialogWrapper");

    protected FacadeDialogWrapper(Project project, boolean canBeParent) {
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
        panel.add(label("Facade Interface Name"),gb.nextLine().next().weightx(0.2));
        panel.add(txtFacadeInterfacedName,gb.nextLine().next().weightx(0.8));
        panel.add(label("Sub-System Class Names(separated by ',')"),gb.nextLine().next().weightx(0.2));
        panel.add(txtSubSystemClassName,gb.nextLine().next().weightx(0.8));
        panel.add(label("Folder Name(generated files will be placed)"),gb.nextLine().next().weightx(0.2));
        panel.add(txtPathName,gb.nextLine().next().weightx(0.8));

        return panel;
    }

    @Override
    protected void doOKAction() {
        String facadeInterface = txtFacadeInterfacedName.getText();
        String subSystemClass = txtSubSystemClassName.getText();
        String pathname = txtPathName.getText();
        logger.info("Ok button clicked");
        logger.info("User input taken in successfully");
        if (facadeInterface.equals("")||subSystemClass.equals("")
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
        findName.setSimpleName(facadeInterface);
        findName.setListName(subSystemClass);
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
        FacadeBuilder facade = new FacadeBuilder();
        facade.setfacadeInterfacedName(facadeInterface);
        ArrayList<String> subSystemClassList = new ArrayList<>(Arrays.asList(subSystemClass.split("\\s*,\\s*")));
        facade.subSystemClassName(subSystemClassList);
        pattern = facade.callPattern();
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
