package com.steammachine.org.gralde.plugins.version.change
//package com.steammachine.org.gralde.plugins.version.change
//
//import org.gradle.api.DefaultTask
//import org.gradle.api.Project
//import org.gradle.api.Task
//import org.gradle.api.file.FileCollection
//import org.gradle.api.internal.file.UnionFileCollection
//import org.gradle.api.plugins.JavaBasePlugin
//import org.gradle.api.tasks.Copy
//import org.gradle.util.ConfigureUtil
//
///**
// * Created by Vladimir Bogodukhov on 20/12/17.
// *
// *
// *
// * {@see <a href="https://docs.gradle.org/3.4.1/userguide/java_plugin.html"> information about plugin creation  </a>}
// * {@see <a href="https://docs.gradle.org/3.4.1/userguide/java_plugin.html#sec:java_tasks"> information about plugin creation </a>}
// *
// * @author Vladimir Bogodukhov
// */
//class Resources {
//
//    private final Project project
//    private final Set<Copy> copies = []
//    private final Task copyresources;
//
//    private static final String PROCESS_RESOURCES = "processResources"
//
//    public static final List<String> DEFAULT_COPY_PATTERN = Collections.unmodifiableList(['**/**resource*'])
//    public static final String COPY_RESOURCES_TASK = "copyresources"
//    public static final String DEFAULT_COPY_CLASS_RESOURCES_TASK = "defaultclassresources"
//    public static final String DEFAULT_COPY_TEST_RESOURCES_TASK = "defaultcopyresources"
//
//
//    Resources(Project project) {
//        this.project = project
//        this.copyresources = project.tasks.create(COPY_RESOURCES_TASK, DefaultTask) {
//            t -> t.group = JavaBasePlugin.BUILD_TASK_NAME
//        }
//
//        copy(DEFAULT_COPY_CLASS_RESOURCES_TASK) {
//            t ->
//                t.group = JavaBasePlugin.BUILD_TASK_NAME
//                t.from project.file('src/main/java')
//                t.into project.file('build/classes/main')
//                t.include DEFAULT_COPY_PATTERN
//        }
//
//        copy(DEFAULT_COPY_TEST_RESOURCES_TASK) {
//            t ->
//                t.group = JavaBasePlugin.BUILD_TASK_NAME
//                t.from project.file('src/test/java')
//                t.into project.file('build/classes/test')
//                t.include DEFAULT_COPY_PATTERN
//        }
//    }
//
////    Resources copy(String name, Consumer<Copy> consumer) {
////        Task task = project.tasks.findByName(name)
////        if (task != null) {
////            if (task instanceof Copy) {
////                /* in this case we have a task  already defined in project */
////                copies.add((task as Copy))
////                (task as Copy).group = JavaBasePlugin.BUILD_TASK_NAME
////                consumer.accept(task as Copy)
////                (task as Copy).group = JavaBasePlugin.BUILD_TASK_NAME
////
////                return this;
////            }
////            throw new IllegalStateException("task with name $name is already defined and has different type $task.getClass().name")
////        }
////
////        Copy copy = (Copy) project.tasks.create(type: Copy, name: name);
////        copies.add(copy)
////        copy.group = JavaBasePlugin.BUILD_TASK_NAME
////        consumer.accept(copy)
////        copy.group = JavaBasePlugin.BUILD_TASK_NAME
////        this
////    }
//
//    Resources copy(String name, Closure<Copy> closure) {
//        Task task = project.tasks.findByName(name)
//        if (task != null) {
//            if (task instanceof Copy) {
//                /* in this case we have a task  already defined in project */
//                copies.add((task as Copy))
//                (task as Copy).group = JavaBasePlugin.BUILD_TASK_NAME
//                closure.accept(task as Copy)
//                (task as Copy).group = JavaBasePlugin.BUILD_TASK_NAME
//
//                return this;
//            }
//            throw new IllegalStateException("task with name $name is already defined and has different type $task.getClass().name")
//        }
//
//        Copy copy = (Copy) project.tasks.create(type: Copy, name: name);
//        copies.add(copy)
//        copy.group = JavaBasePlugin.BUILD_TASK_NAME
//        ConfigureUtil.configure(closure, copy)
//        copy.group = JavaBasePlugin.BUILD_TASK_NAME
//        this
//    }
//
//    void projectEvaluated() {
//        project.tasks.getByPath(PROCESS_RESOURCES).dependsOn.add(copyresources)
//        copyresources.dependsOn.addAll(copies);
//    }
//
//    @Override
//    String toString() {
//        return "Resources(" + "copy tasks=" + copies + ") "
//    }
//}
