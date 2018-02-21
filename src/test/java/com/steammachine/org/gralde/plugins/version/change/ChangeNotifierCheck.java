package com.steammachine.org.gralde.plugins.version.change;

import com.steammachine.org.gralde.plugins.ChangeNotifier;
import com.steammachine.org.junit5.extensions.expectedexceptions.Expected;
import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.steammachine.common.utils.commonutils.CommonUtils.getAbsoluteResourcePath;
import static com.steammachine.org.gralde.plugins.ChangeNotifier.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ChangeNotifierCheck {

    @Test
    void belongsToRoot10() {
        File root = new File(getAbsoluteResourcePath(getClass(), "res"));
        File file = new File(getAbsoluteResourcePath(getClass(), "res/resource_file1.txt"));
        File file2 = new File(getAbsoluteResourcePath(getClass(), "res/subpath/resource_file2.txt"));
        File file3 = new File(getAbsoluteResourcePath(getClass(), "res2/resource_file3.txt"));

        assertEquals(true, belongsToRoot(root, root));
        assertEquals(true, belongsToRoot(file, root));
        assertEquals(true, belongsToRoot(file2, root));
        assertEquals(false, belongsToRoot(file3, root));
    }

    @Test
    void belongsToRoot20() {
        Path root = new File(getAbsoluteResourcePath(getClass(), "res")).toPath();
        Path file = new File(getAbsoluteResourcePath(getClass(), "res/resource_file1.txt")).toPath();
        Path file2 = new File(getAbsoluteResourcePath(getClass(), "res/subpath/resource_file2.txt")).toPath();
        Path file3 = new File(getAbsoluteResourcePath(getClass(), "res2/resource_file3.txt")).toPath();

        assertEquals(true, pathBelongsToRoot(root, root));
        assertEquals(true, pathBelongsToRoot(file, root));
        assertEquals(true, pathBelongsToRoot(file2, root));
        assertEquals(false, pathBelongsToRoot(file3, root));
    }

    @Test
    void difference10() {
        Path root = new File(getAbsoluteResourcePath(getClass(), "res")).toPath();
        Path file = new File(getAbsoluteResourcePath(getClass(), "res/resource_file1.txt")).toPath();
        Path file2 = new File(getAbsoluteResourcePath(getClass(), "res/subpath/resource_file2.txt")).toPath();

        assertEquals(Paths.get(""), difference(root, root));
        assertEquals(Paths.get("resource_file1.txt"), difference(file, root));
        assertEquals(Paths.get("subpath", "resource_file2.txt"), difference(file2, root));
    }

    @Test
    @Expected(expected = IllegalStateException.class)
    void difference20() {
        Path root = new File(getAbsoluteResourcePath(getClass(), "res")).toPath();
        Path file3 = new File(getAbsoluteResourcePath(getClass(), "res2/resource_file3.txt")).toPath();
        difference(root, file3);
    }

    @Test
    @Expected(expected = IllegalStateException.class)
    void difference30() {
        Path file = new File(getAbsoluteResourcePath(getClass(), "res/resource_file1.txt")).toPath();
        Path file2 = new File(getAbsoluteResourcePath(getClass(), "res2/resource_file3.txt")).toPath();
        difference(file, file2);
    }

    @Test
    @Expected(expected = IllegalStateException.class)
    void difference40() {
        Path file = new File(getAbsoluteResourcePath(getClass(), "res/resource_file1.txt")).toPath();
        Path file2 = new File(getAbsoluteResourcePath(getClass(), "res2/resource_file3.txt")).toPath();
        difference(file2, file);
    }

    @Test
    void fullCycle10() {
        Project project = ProjectBuilder.builder().build();
        ChangeNotifier notifier = project.getTasks().create("changenotifier", ChangeNotifier.class);
        notifier.setRootDirectory(getAbsoluteResourcePath(getClass(), "res"));
        notifier.setFiles(getAbsoluteResourcePath(getClass(), "res/resource_file1.txt"),
                getAbsoluteResourcePath(getClass(), "res/subpath/resource_file2.txt"));
        assertEquals("IDqhT8cn8donae361rcB8A==", notifier.calculateHash());
    }


}
