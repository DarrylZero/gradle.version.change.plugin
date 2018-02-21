package com.steammachine.org.gralde.plugins.version.change
//package com.steammachine.org.gralde.plugins.version.change
//
//import org.gradle.api.Plugin
//import org.gradle.api.Project
//
///**
// *
// * {@link ResourceCopyPlugin}
// *        com.steammachine.org.gralde.plugins.resource.plugin.ResourceCopyPlugin
// */
//class ResourceCopyPlugin implements Plugin<Project> {
//
//    public static final String EXTENSION_NAME = "copyresources"
//    public static final int VERSION = 1
//    public static final int SUB_VERSION = 5
//    public static final String START_MESSAGE = "applying plugin [version $VERSION.$SUB_VERSION ]"
//
//    void apply(Project project) {
//        // println START_MESSAGE + " for the project ($project.name [$project.displayName])"
//        project.pluginManager.apply('java')
//
//        /* Фаза конструирования */
//        Resources copyresources = project.extensions.create(EXTENSION_NAME, Resources, project)
//
//        project.afterEvaluate {
//            /* Фаза настиройки после того как проект сконфигурирован */
//            copyresources.projectEvaluated()
//        }
//    }
//
//}
//
//
