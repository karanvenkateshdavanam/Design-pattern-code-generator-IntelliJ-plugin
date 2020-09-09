package com.cs474;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

public class PatternWindow {
    private JRadioButton bridgeRadioButton;
    private JPanel userEntryPanel;
    private JRadioButton facadeRadioButton;
    private JButton submitButton;
    private JRadioButton factoryRadioButton;
    private JRadioButton visitorRadioButton;
    private JRadioButton templateRadioButton;
    private JRadioButton strategyRadioButton;
    private JRadioButton abstractFactoryRadioButton;
    private JRadioButton chainRadioButton;
    private DialogWrapper wrapper;
    static final Logger logger = LoggerFactory.getLogger("PatternWindow");


    public PatternWindow(Project project) {

        submitButton.addActionListener(e -> {
              if(bridgeRadioButton.isSelected()){
                  logger.info("Bridge button selected");
                  wrapper = new BridgeDialogWrapper(project,true);
                  wrapper.show();
              }
              else if(facadeRadioButton.isSelected()){
                  logger.info("facade button selected");
                  wrapper = new FacadeDialogWrapper(project,true);
                  wrapper.show();
              }
              else if(factoryRadioButton.isSelected()){
                  logger.info("factory button selected");
                  wrapper = new FactoryDialogWrapper(project,true);
                  wrapper.show();
              }
              else if(visitorRadioButton.isSelected()){
                  logger.info("visitor button selected");
                  wrapper = new VisitorDialogWrapper(project,true);
                  wrapper.show();
              }
              else if(templateRadioButton.isSelected()){
                  logger.info("template button selected");
                  wrapper = new TemplateDialogWrapper(project,true);
                  wrapper.show();
              }
              else if(strategyRadioButton.isSelected()){
                  logger.info("strategy button selected");
                  wrapper = new StrategyDialogWrapper(project,true);
                  wrapper.show();
              }
              else if(abstractFactoryRadioButton.isSelected()){
                  logger.info("abstractfactory button selected");
                  wrapper = new AbstractFactoryDialogWrapper(project,true);
                  wrapper.show();
              }
              else if(chainRadioButton.isSelected()){
                  logger.info("chain button selected");
                  wrapper = new ChainDialogWrapper(project,true);
                  wrapper.show();
              }
        });
    }
    public JPanel getContent() {
        return userEntryPanel;
    }
}
