package com.steammachine.org.gralde.plugins.version.change;

import org.gradle.api.internal.project.DefaultProject;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VersionChangerPluginCheck {


    @Test
    void nameIntegrityChacks() {
        assertEquals("com.steammachine.org.gralde.plugins.version.change.VersionChangerPlugin",
                VersionChangerPlugin.class.getName());
    }

    @Test
    void constantChacks() {
        assertEquals("version_manager", VersionChangerPlugin.TASK_NAME);
        assertEquals("publish.properties", VersionChangerPlugin.DAFAULT_PROPERTY_FILE_NAME);
        assertEquals("src", VersionChangerPlugin.DEFAULT_SOURCE_DIRECTORY);
    }


    @Test
    void changeNotifierApplication() {
        DefaultProject project = (DefaultProject) ProjectBuilder.builder().build();
        project.getPluginManager().apply(VersionChangerPlugin.class);
        project.evaluate();

        assertTrue(project.getTasks().getByName("version_manager") instanceof ChangeNotifier);
        assertTrue(project.getPlugins().getAt("com.steammachine.org.gradle.plugins.version.change") instanceof VersionChangerPlugin);
    }

    @Test
    void changeNotifierApplication20() {
        DefaultProject project = (DefaultProject) ProjectBuilder.builder().build();
        project.getPluginManager().apply("com.steammachine.org.gradle.plugins.version.change");
        project.evaluate();
        assertTrue(project.getTasks().getByName("version_manager") instanceof ChangeNotifier);
        assertTrue(project.getPlugins().getAt("com.steammachine.org.gradle.plugins.version.change") instanceof VersionChangerPlugin);
    }



}