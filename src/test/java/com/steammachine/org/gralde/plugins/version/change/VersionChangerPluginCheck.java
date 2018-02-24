package com.steammachine.org.gralde.plugins.version.change;

import org.gradle.api.internal.project.DefaultProject;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VersionChangerPluginCheck {


    @Test
    void constantChacks() {
        assertEquals("version_manager", VersionChangerPlugin.TASK_NAME);
    }


    @Test
    void changeNotifierApplication() {
        DefaultProject project = (DefaultProject) ProjectBuilder.builder().build();
        project.getPluginManager().apply(VersionChangerPlugin.class);
        project.evaluate();

        assertTrue(project.getTasks().getByName("version_manager") instanceof ChangeNotifier);
        assertTrue(project.getPlugins().getAt("com.steammachine.org.gralde.plugins.version.change") instanceof VersionChangerPlugin);
    }

    @Test
    void changeNotifierApplication20() {
        DefaultProject project = (DefaultProject) ProjectBuilder.builder().build();
        project.getPluginManager().apply("com.steammachine.org.gralde.plugins.version.change");
        project.evaluate();
        assertTrue(project.getTasks().getByName("version_manager") instanceof ChangeNotifier);
        assertTrue(project.getPlugins().getAt("com.steammachine.org.gralde.plugins.version.change") instanceof VersionChangerPlugin);
    }


}