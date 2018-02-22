package com.steammachine.org.gralde.plugins.version.change

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

import static com.steammachine.common.utils.commonutils.CommonUtils.getAbsoluteResourcePath

class TGCheck {


    @Test
    void checkHash20() {
        Project project = ProjectBuilder.builder().build()
        ChangeNotifier notifier = project.getTasks().create("changenotifier", ChangeNotifier.class)

        notifier.rootDirectory = getAbsoluteResourcePath(TGCheck, "res")
        notifier.files = getAbsoluteResourcePath(TGCheck, "res/resource_file.txt")

        notifier.hashStorage(PropertyStorage.class) {
            file = project.file(getAbsoluteResourcePath(TGCheck, "res/resource_props.properties"))
            propertyName = "hash"
        }
        notifier.versionStorage(PropertyStorage.class) {
            file = project.file(getAbsoluteResourcePath(TGCheck, "res/resource_props.properties"))
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

        System.setProperty("VER", "inc")
        notifier.process()
    }


}
