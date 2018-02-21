package com.steammachine.org.gralde.plugins.version.change;

import com.steammachine.common.utils.commonutils.CommonUtils;
import com.steammachine.org.gralde.plugins.ChangeNotifier;
import com.steammachine.org.junit5.extensions.expectedexceptions.Expected;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.steammachine.org.gralde.plugins.ChangeNotifier.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ChangeNotifierTests {

    @Test
    void belongsToRoot10() {
        File root = new File(CommonUtils.getAbsoluteResourcePath(getClass(), "res"));
        File file = new File(CommonUtils.getAbsoluteResourcePath(getClass(), "res/resource_file1.txt"));
        File file2 = new File(CommonUtils.getAbsoluteResourcePath(getClass(), "res/subpath/resource_file2.txt"));
        File file3 = new File(CommonUtils.getAbsoluteResourcePath(getClass(), "res2/resource_file3.txt"));

        assertEquals(true, belongsToRoot(root, root));
        assertEquals(true, belongsToRoot(root, file));
        assertEquals(true, belongsToRoot(root, file2));
        assertEquals(false, belongsToRoot(root, file3));
    }

    @Test
    void belongsToRoot20() {
        Path root = new File(CommonUtils.getAbsoluteResourcePath(getClass(), "res")).toPath();
        Path file = new File(CommonUtils.getAbsoluteResourcePath(getClass(), "res/resource_file1.txt")).toPath();
        Path file2 = new File(CommonUtils.getAbsoluteResourcePath(getClass(), "res/subpath/resource_file2.txt")).toPath();
        Path file3 = new File(CommonUtils.getAbsoluteResourcePath(getClass(), "res2/resource_file3.txt")).toPath();


        assertEquals(true, pathBelongsToRoot(root, root));
        assertEquals(true, pathBelongsToRoot(root, file));
        assertEquals(true, pathBelongsToRoot(root, file2));
        assertEquals(false, pathBelongsToRoot(root, file3));
    }

    @Test
    void subtract10() {
        Path root = new File(CommonUtils.getAbsoluteResourcePath(getClass(), "res")).toPath();
        Path file = new File(CommonUtils.getAbsoluteResourcePath(getClass(), "res/resource_file1.txt")).toPath();
        Path file2 = new File(CommonUtils.getAbsoluteResourcePath(getClass(), "res/subpath/resource_file2.txt")).toPath();
        Path file3 = new File(CommonUtils.getAbsoluteResourcePath(getClass(), "res2/resource_file3.txt")).toPath();

        assertEquals(Paths.get(""), subtract(root, root));
        assertEquals(Paths.get("resource_file1.txt"), subtract(root, file));
        assertEquals(Paths.get("subpath", "resource_file2.txt"), subtract(root, file2));
    }

    @Test
    @Expected(expected = IllegalStateException.class)
    void subtract20() {
        Path root = new File(CommonUtils.getAbsoluteResourcePath(getClass(), "res")).toPath();
        Path file3 = new File(CommonUtils.getAbsoluteResourcePath(getClass(), "res2/resource_file3.txt")).toPath();
        subtract(root, file3);
    }


}
