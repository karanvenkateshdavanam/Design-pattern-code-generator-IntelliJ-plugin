package com.cs474;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PatternSelectFactory implements ToolWindowFactory {
    static final Logger logger = LoggerFactory.getLogger("PatternSelectFactory");
    // Create the tool window content.
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        logger.info("Patternwindow Class getting initialized");
        PatternWindow patternWindow = new PatternWindow(project);
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(patternWindow.getContent(), "", false);
        logger.info("Docker window getting initialized");
        toolWindow.getContentManager().addContent(content);

     }
}
