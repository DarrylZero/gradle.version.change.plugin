package com.steammachine.org.gralde.plugins.version.change

import org.gradle.api.Plugin
import org.gradle.api.Project

import static com.steammachine.common.utils.commonutils.CommonUtils.getAbsoluteResourcePath
import static com.steammachine.common.utils.commonutils.CommonUtils.getAbsoluteResourcePath

/**
 *
 * {@link com.steammachine.org.gralde.plugins.version.change.VersionChangerPlugin}
 *        com.steammachine.org.gralde.plugins.version.change.VersionChangerPlugin
 */
class VersionChangerPlugin implements Plugin<Project> {

    public static final String TASK_NAME = "version_manager"

    void apply(Project project) {

        project.tasks.create(TASK_NAME, ChangeNotifier) {
            rootDirectory = project.file('src')
            files = project.fileTree('src')

            hashStorage(PropertyStorage.class) {
                file = project.file('publish.properties')
                propertyName = 'hash'
            }

            versionStorage(PropertyStorage.class) {
                file = project.file('publish.properties')
                propertyName = 'version'
            }
        }
    }

}


