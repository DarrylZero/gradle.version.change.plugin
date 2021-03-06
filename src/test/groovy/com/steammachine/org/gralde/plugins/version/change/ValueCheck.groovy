package com.steammachine.org.gralde.plugins.version.change

import org.gradle.api.Project
import org.gradle.api.internal.project.DefaultProject
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Assert
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

import java.util.function.Consumer

import static com.steammachine.common.utils.commonutils.CommonUtils.getAbsoluteResourcePath

class ValueCheck {


    @Test
    void checkHash10() {
        Project project = ProjectBuilder.builder().build()
        ChangeNotifier notifier = project.getTasks().create("changenotifier", ChangeNotifier.class)

        notifier.rootDirectory = getAbsoluteResourcePath(ValueCheck, "res")
        notifier.files = getAbsoluteResourcePath(ValueCheck, "res/resource_file.txt")

        notifier.hashStorage(PropertyStorage.class) {
            file = project.file(getAbsoluteResourcePath(ValueCheck, "res/resource_props.properties"))
            propertyName = "hash"
        }
        notifier.versionStorage(PropertyStorage.class) {
            file = project.file(getAbsoluteResourcePath(ValueCheck, "res/resource_props.properties"))
            propertyName = "version"
        }

        Assertions.assertEquals(null, notifier.hashStorage.value)
        notifier.hashStorage.value = "11"
        Assertions.assertEquals("11", notifier.hashStorage.value)
        notifier.hashStorage.write()
        Assertions.assertEquals("11", notifier.hashStorage.value)
        notifier.hashStorage.value = "12"
        notifier.hashStorage.read()
        Assertions.assertEquals("11", notifier.hashStorage.value)
    }

    static class TestLogger implements Consumer<String> {
        final List<String> logItems = []
        @Override
        void accept(String s) {
            logItems.add(s)
        }

        boolean has(String data) {
            logItems.stream().filter{
                it.contains data
            }.findFirst().isPresent()
        }
    }

    @Test
    void checkHash20() {
        DefaultProject project = ProjectBuilder.builder().build() as DefaultProject
        ChangeNotifier notifier = project.getTasks().create("changenotifier", ChangeNotifier.class)

        notifier.rootDirectory = getAbsoluteResourcePath(ValueCheck, "res")
        notifier.files = getAbsoluteResourcePath(ValueCheck, "res/resource_file.txt")

        notifier.hashStorage(PropertyStorage.class) {
            file = project.file(getAbsoluteResourcePath(ValueCheck, "res/resource_props2.properties"))
            propertyName = "hash"
        }
        notifier.versionStorage(PropertyStorage.class) {
            file = project.file(getAbsoluteResourcePath(ValueCheck, "res/resource_props2.properties"))
            propertyName = "version"
        }

        System.setProperty("action", "check")

        project.evaluate()

        def logger = new TestLogger()
        notifier.addLogger(logger)
        notifier.process()
        Assert.assertTrue(logger.has("not changed"))
    }

    @Test
    void checkInc10() {
        DefaultProject project = ProjectBuilder.builder().build() as DefaultProject
        ChangeNotifier notifier = project.getTasks().create("changenotifier", ChangeNotifier.class)

        notifier.rootDirectory = getAbsoluteResourcePath(ValueCheck, "res")
        notifier.files = getAbsoluteResourcePath(ValueCheck, "res/resource_file.txt")

        def path = getAbsoluteResourcePath(ValueCheck, "res/resource_props2.properties")

        notifier.hashStorage(PropertyStorage.class) {
            file = project.file(path)
            propertyName = "hash"
        }
        notifier.versionStorage(PropertyStorage.class) {
            file = project.file(path)
            propertyName = "version"
        }

        System.setProperty("action", "nextversion")
        project.evaluate()

        def properties = new Properties()
        new FileInputStream(path).withCloseable {
            properties.load(it)
        }
        Assertions.assertEquals("1.0.0", properties.getProperty('version'))

        def logger = new TestLogger()
        notifier.addLogger(logger)
        notifier.process()

        properties = new Properties()
        new FileInputStream(path).withCloseable {
            properties.load(it)
        }

        Assertions.assertEquals("1.0.0", properties.getProperty('version'))
    }

    @Test
    void checkInc20() {
        DefaultProject project = ProjectBuilder.builder().build() as DefaultProject
        ChangeNotifier notifier = project.getTasks().create("changenotifier", ChangeNotifier.class)

        notifier.rootDirectory = getAbsoluteResourcePath(ValueCheck, "res")
        notifier.files = getAbsoluteResourcePath(ValueCheck, "res/resource_file.txt")

        def path = getAbsoluteResourcePath(ValueCheck, "res/resource_props3.properties")

        notifier.hashStorage(PropertyStorage.class) {
            file = project.file(path)
            propertyName = "hash"
        }
        notifier.versionStorage(PropertyStorage.class) {
            file = project.file(path)
            propertyName = "version"
        }

        System.setProperty("action", "nextversion")
        project.evaluate()

        new FileOutputStream(path).withCloseable {
            def prop = new Properties()
            prop.setProperty("version", "1.0.0")
            prop.store(it, "")
        }
        def properties = new Properties()
        new FileInputStream(path).withCloseable {
            properties.load(it)
        }

        Assertions.assertEquals("1.0.0", properties.getProperty('version'))

        def logger = new TestLogger()
        notifier.addLogger(logger)
        notifier.process()

        properties = new Properties()
        new FileInputStream(path).withCloseable {
            properties.load(it)
        }
        Assertions.assertEquals("1.0.1", properties.getProperty('version'))
    }

    @Test
    void checkInc30() {
        DefaultProject project = ProjectBuilder.builder().build() as DefaultProject
        ChangeNotifier notifier = project.getTasks().create("changenotifier", ChangeNotifier.class)

        notifier.rootDirectory = getAbsoluteResourcePath(ValueCheck, "res")
        notifier.files = getAbsoluteResourcePath(ValueCheck, "res/resource_file.txt")

        def path = getAbsoluteResourcePath(ValueCheck, "res/resource_props3.properties")

        notifier.hashStorage(PropertyStorage.class) {
            file = project.file(path)
            propertyName = "hash"
        }
        notifier.versionStorage(PropertyStorage.class) {
            file = project.file(path)
            propertyName = "version"
        }

        System.setProperty("action", "nextversion")
        project.evaluate()

        new FileOutputStream(path).withCloseable {
            def prop = new Properties()
            prop.setProperty("version", "1.0.0")
            prop.store(it, "")
        }
        def properties = new Properties()
        new FileInputStream(path).withCloseable {
            properties.load(it)
        }

        Assertions.assertEquals("1.0.0", properties.getProperty('version'))

        def logger = new TestLogger()
        notifier.addLogger(logger)
        notifier.process()

        properties = new Properties()
        new FileInputStream(path).withCloseable {
            properties.load(it)
        }
        Assertions.assertEquals("1.0.1", properties.getProperty('version'))
    }


}
