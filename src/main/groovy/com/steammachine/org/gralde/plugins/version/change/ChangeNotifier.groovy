package com.steammachine.org.gralde.plugins.version.change

import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.file.FileTree
import org.gradle.api.internal.file.UnionFileCollection
import org.gradle.api.logging.LogLevel
import org.gradle.api.tasks.TaskAction
import org.gradle.util.ConfigureUtil

import java.nio.charset.StandardCharsets
import java.nio.file.Path
import java.nio.file.Paths
import java.security.MessageDigest
import java.util.regex.Pattern

class ChangeNotifier extends DefaultTask {

    private static final Comparator<File> COMPARATOR = new Comparator<File>() {
        @Override
        int compare(File f1, File f2) {
            return f1.absolutePath.compareTo(f2.absolutePath)
        }
    }
    private static final Path ZERO_PATH = Paths.get("")
    public static final Pattern VERSION_PATTERN = Pattern.compile('^(\\d+)\\.(\\d+)\\.(\\d+)$')

    UnionFileCollection files
    File rootDirectory
    ValueStorage hashStorage
    ValueStorage versionStorage

    FileCollection getFiles() {
        return files
    }

    FileCollection ensureFiles() {
        files = files ? files : new UnionFileCollection()
    }

    void setFiles(String... filenames) {
        getLogger().log(LogLevel.DEBUG, "void setFiles(String ... filenames) ")
        filenames.each {
            fn -> ensureFiles().add(project.files(fn))
        }
    }

    void setFiles(FileCollection collection) {
        getLogger().log(LogLevel.DEBUG, "void setFiles(FileCollection searchclasspath) ")
        ensureFiles().add(collection)
    }

    void setFiles(File... files) {
        getLogger().log(LogLevel.DEBUG, "void setFiles(File searchclasspath) ")
        files.each {
            file -> ensureFiles().add(project.files(file))
        }
    }

    void setFiles(FileTree searchclasspath) {
        getLogger().log(LogLevel.DEBUG, "void setFiles(FileTree searchclasspath) ")
        ensureFiles().add(searchclasspath)
    }

    void setRootDirectory(File rootDirectory) {
        Objects.requireNonNull(rootDirectory)
        this.rootDirectory = rootDirectory
    }

    void setRootDirectory(String rootDirectory) {
        Objects.requireNonNull(rootDirectory)
        this.rootDirectory = project.file(rootDirectory)
    }

    void hashStorage(Class<? extends ValueStorage> clazz, Closure config) {
        Objects.requireNonNull(clazz)
        Objects.requireNonNull(config)
        hashStorage = clazz.newInstance() as ValueStorage
        ConfigureUtil.configure(config, hashStorage)
    }

    void versionStorage(Class<? extends ValueStorage> clazz, Closure config) {
        versionStorage = clazz.newInstance() as ValueStorage
        ConfigureUtil.configure(config, versionStorage)
    }


    @TaskAction
    protected void process() {
        checkStart()
        def versionCommand = System.getProperty('VER')
        if (versionCommand == null) {
            checkChanges()
        } else {
            switch (versionCommand) {
                case "check" :
                    checkChanges()
                    break

                case "inc" :
                    versionStorage.read()
                    hashStorage.read()
                    if (hashStorage.value != calculateHash()) {
                        if (versionStorage.value == null) {
                            logger.log(LogLevel.INFO, "cannot increment null value")
                        } else if (!VERSION_PATTERN.matcher(versionStorage.value).matches()) {
                            logger.log(LogLevel.INFO, "cannot increment value $versionStorage.value")
                        } else {
                            def ver = Integer.parseInt(versionStorage.value.split("\\.")[2])
                            ver++
                            versionStorage.value = versionStorage.value.substring(0, versionStorage.value.lastIndexOf(".")) +
                                    ".$ver";
                            hashStorage.value = calculateHash()
                            versionStorage.write()
                            hashStorage.write()
                        }
                    }
                    break

                case "get" :
                    versionStorage.read()
                    logger.log(LogLevel.INFO, "version value is $versionStorage.value")
                    break

                default:
                    checkChanges()
                    break
            }
        }
    }

    private void checkChanges() {
        hashStorage.read()
        boolean changed = hashStorage.value != calculateHash()
        logger.log(LogLevel.INFO, "sourcecode for project $project.name is " + (changed ? "changed" : "not changed"))
    }

    private void checkStart() {
        if (!hashStorage) {
            throw new IllegalStateException("hashStorage is not set")
        }
        if (!versionStorage) {
            throw new IllegalStateException("versionStorage is not set")
        }
        if (!rootDirectory) {
            throw new IllegalStateException("root directory is not set")
        }
        ensureFiles().files.forEach { f ->
            if (!belongsToRoot(f, rootDirectory)) {
                throw new IllegalStateException("file $f.absolutePath is not within root directory $rootDirectory.absolutePath")
            }
        }
    }

    static boolean belongsToRoot(File f, File container) {
        pathBelongsToRoot(f.toPath(), container.toPath())
    }

    static boolean pathBelongsToRoot(Path filePath, Path containerPath) {
        if (containerPath.nameCount > filePath.nameCount) {
            false
        } else if (containerPath.root != filePath.root) {
            false
        } else {
            Objects.equals(filePath.subpath(0, containerPath.nameCount), containerPath.subpath(0, containerPath.nameCount))
        }
    }

    static Path difference(Path subtrahend, Path reduced) {
        if (!pathBelongsToRoot(subtrahend, reduced)) {
            throw new IllegalStateException("file $reduced is not within directory $subtrahend")
        }
        reduced.nameCount == subtrahend.nameCount ? ZERO_PATH : subtrahend.subpath(reduced.nameCount, subtrahend.nameCount)
    }

    String calculateHash() {
        calcCommonHash(ensureFiles().files)
    }

    private String calcCommonHash(Set<File> files) {
        List<File> list = new ArrayList(files)
        Collections.sort(list, COMPARATOR)

        def digest = MessageDigest.getInstance("MD5")
        list.stream().forEachOrdered {
            calcFileHash(it, digest)
        }

        Base64.encoder.encodeToString(digest.digest())
    }


    void calcFileHash(File file, MessageDigest digest) {
        Objects.requireNonNull(file)
        def difference = difference(file.toPath(), rootDirectory.toPath())

        for (int i = 0; i < difference.nameCount; i++) {
            digest.update(difference[i].toString().getBytes(StandardCharsets.UTF_8))
        }

        def buffer = new byte[1024]
        new BufferedInputStream(new FileInputStream(file)).withCloseable {
            def read
            while ((read = it.read(buffer)) >= 0) {
                digest.update(buffer, 0, read)
            }
        }
    }

}





