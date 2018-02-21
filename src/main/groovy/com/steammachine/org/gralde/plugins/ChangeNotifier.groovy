package com.steammachine.org.gralde.plugins

import com.steammachine.common.lazyeval.LazyEval
import com.steammachine.common.utils.commonutils.CommonUtils
import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.file.FileTree
import org.gradle.api.internal.file.UnionFileCollection
import org.gradle.api.logging.LogLevel
import org.gradle.api.tasks.TaskAction

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.security.MessageDigest

class ChangeNotifier extends DefaultTask {

    private static final Comparator<File> COMPARATOR = new Comparator<File>() {
        @Override
        int compare(File f1, File f2) {
            return f1.absolutePath.compareTo(f2.absolutePath)
        }
    }
    private static final Path ZERO_PATH = Paths.get("")

    UnionFileCollection files

    File rootDirectory


    FileCollection getFiles() {
        return files
    }

    private FileCollection ensureFiles() {
        files = files ? files : new UnionFileCollection()
    }

    void setFiles(String searchclasspath) {
        getLogger().log(LogLevel.DEBUG, "void setFiles(String searchclasspath) ")
        ensureFiles().add(project.files(searchclasspath))
    }

    void setFiles(FileCollection searchclasspath) {
        getLogger().log(LogLevel.DEBUG, "void setFiles(FileCollection searchclasspath) ")
        ensureFiles().add(searchclasspath)
    }

    void setFiles(File searchclasspath) {
        getLogger().log(LogLevel.DEBUG, "void setFiles(File searchclasspath) ")
        ensureFiles().add(project.files(searchclasspath))
    }

    void setFiles(FileTree searchclasspath) {
        getLogger().log(LogLevel.DEBUG, "void setFiles(FileTree searchclasspath) ")
        ensureFiles().add(searchclasspath)
    }

    void setRootDirectory(File rootDirectory) {
        Objects.requireNonNull(rootDirectory)
        this.rootDirectory = rootDirectory;
    }

    @TaskAction
    protected void watch() {
        if (!rootDirectory) {
            throw new IllegalStateException()
        }
        ensureFiles().files.forEach { f ->
            if (!belongsToRoot(f, rootDirectory)) {
                throw new IllegalStateException("file $f.absolutePath is not within root directory $rootDirectory.absolutePath")
            }
        }


        println "sf" + ensureFiles().getFiles()
        println calcCommonHash(ensureFiles().getFiles())
    }

    static boolean belongsToRoot(File container, File f) {
        pathBelongsToRoot(container.toPath(), f.toPath())
    }

    static boolean pathBelongsToRoot(Path containerPath, Path filePath) {
        if (containerPath.nameCount > filePath.nameCount) {
            false
        } else if (containerPath.root != filePath.root) {
            false
        } else {
            Objects.equals(filePath.subpath(0, containerPath.nameCount), containerPath.subpath(0, containerPath.nameCount))
        }
    }

    static Path subtract(Path base, Path subtractor) {
        if (!pathBelongsToRoot(base, subtractor)) {
            throw new IllegalStateException("file $subtractor is not within root directory $base")
        }
        subtractor.nameCount == base.nameCount ? ZERO_PATH : subtractor.subpath(base.nameCount, subtractor.nameCount)
    }


    String calcCommonHash(Set<File> files) {
        List<File> list = new ArrayList(files)
        Collections.sort(list, COMPARATOR)

        def digest = MessageDigest.getInstance("MD5")
        list.stream().forEachOrdered {


        }

        "dd "
    }


    byte[] calcFileHash(File file) throws IOException {
        Objects.requireNonNull(file)
        /// BufferedReader reader =


        new BufferedReader(Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8)).withCloseable {
            LazyEval<MessageDigest> digest = LazyEval.eval {
                CommonUtils.suppress(new CommonUtils.SupressedExceptionSupplier<MessageDigest>() {
                    @Override
                    MessageDigest execute() throws Exception {
                        return MessageDigest.getInstance("MD5")
                    }
                })
            }


            file.absolutePath.getBytes(StandardCharsets.UTF_8)

            digest.value().digest()


        }


        String line;
        while ((line = reader.readLine()) != null) {
//                if (containsTag(line)) {
//                    continue;
//                }
//                if (isLFE(line)) {
//                    continue;
//                }
//                byte[] chunk = line.getBytes(dataCharset());
//                digest.value().update(chunk);
//            }
//            return digest.value().digest();
//        }
        }
    }

}





