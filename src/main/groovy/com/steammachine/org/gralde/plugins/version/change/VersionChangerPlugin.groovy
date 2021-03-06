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

    public static final String TASK_NAME = 'version_manager'
    public static final String DAFAULT_PROPERTY_FILE_NAME = 'publish.properties'
    public static final String DEFAULT_SOURCE_DIRECTORY = 'src'

    void apply(Project project) {

        project.tasks.create(TASK_NAME, ChangeNotifier) {
            rootDirectory = project.file(DEFAULT_SOURCE_DIRECTORY)
            files = project.fileTree(DEFAULT_SOURCE_DIRECTORY)

            hashStorage(PropertyStorage.class) {
                file = project.file(DAFAULT_PROPERTY_FILE_NAME)
                propertyName = 'hash'
            }

            versionStorage(PropertyStorage.class) {
                file = project.file(DAFAULT_PROPERTY_FILE_NAME)
                propertyName = 'version'
            }
        }
    }

}


