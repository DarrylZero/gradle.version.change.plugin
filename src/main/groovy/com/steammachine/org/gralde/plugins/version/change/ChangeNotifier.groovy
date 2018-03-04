package com.steammachine.org.gralde.plugins.version.change

import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.file.FileTree
import org.gradle.api.internal.file.UnionFileCollection
import org.gradle.api.tasks.TaskAction
import org.gradle.util.ConfigureUtil

import java.nio.charset.StandardCharsets
import java.nio.file.Path
import java.nio.file.Paths
import java.security.MessageDigest
import java.util.function.Consumer
import java.util.regex.Pattern

import static com.steammachine.org.gralde.plugins.version.change.Action.*

/**
 *
 * {@link com.steammachine.org.gralde.plugins.version.change.ChangeNotifier}
 * com.steammachine.org.gralde.plugins.version.change.ChangeNotifier
 */
class ChangeNotifier extends DefaultTask {

    private static final Comparator<File> COMPARATOR = new Comparator<File>() {
        @Override
        int compare(File f1, File f2) {
            return f1.absolutePath.compareTo(f2.absolutePath)
        }
    }
    private static final Path ZERO_PATH = Paths.get("")
    private static final Pattern VERSION_PATTERN = Pattern.compile('^(\\d+)\\.(\\d+)\\.(\\d+)$')
    private static final String MD5 = "MD5"
    public static final String ACTION = 'action'

    private final List<Consumer<String>> loggers = []

    UnionFileCollection files
    File rootDirectory
    ValueStorage hashStorage
    ValueStorage versionStorage

    ChangeNotifier() {
        addLogger(new Consumer<String>() {
            @Override
            void accept(String data) {
                println data
            }
        })
        log('safdasdasgdfasdadsfadsf')
    }

    FileCollection getFiles() {
        return files
    }

    FileCollection ensureFiles() {
        files = files ? files : new UnionFileCollection()
    }

    void setFiles(String... filenames) {
        log("void setFiles($filenames) ")
        filenames.each {
            fn -> ensureFiles().add(project.files(fn))
        }
    }

    void setFiles(FileCollection collection) {
        log("void setFiles($collection) ")
        ensureFiles().add(collection)
    }

    void setFiles(File... files) {
        log("void setFiles($files) ")
        files.each {
            file -> ensureFiles().add(project.files(file))
        }
    }

    void setFiles(FileTree tree) {
        log("void setFiles($tree) ")
        ensureFiles().add(tree)
    }

    void clearFiles() {
        files = null
    }

    void setRootDirectory(File rootDirectory) {
        log("void setRootDirectory($rootDirectory.absolutePath)")
        Objects.requireNonNull(rootDirectory)
        this.rootDirectory = rootDirectory
    }

    void setRootDirectory(String rootDirectory) {
        setRootDirectory(project.file(rootDirectory))
    }

    /**
     * create and configure hash-store
     * @param clazz - not null
     * @param config - not null
     */
    void hashStorage(Class<? extends ValueStorage> clazz, Closure config) {
        Objects.requireNonNull(clazz)
        Objects.requireNonNull(config)
        hashStorage = clazz.newInstance() as ValueStorage
        ConfigureUtil.configure(config, hashStorage)
    }

    /**
     * configure hash-store
     * @param clazz - not null
     * @param config - not null
     */
    void hashStorage(Closure config) {
        Objects.requireNonNull(config)
        ConfigureUtil.configure(config, hashStorage)
    }


    /**
     * create and configure version - storage
     * @param clazz - not null
     * @param config- not null
     */
    void versionStorage(Class<? extends ValueStorage> clazz, Closure config) {
        Objects.requireNonNull(clazz)
        Objects.requireNonNull(config)
        versionStorage = clazz.newInstance() as ValueStorage
        ConfigureUtil.configure(config, versionStorage)
    }

    /**
     * configure version - storage
     * @param clazz - not null
     * @param config- not null
     */
    void versionStorage(Closure config) {
        Objects.requireNonNull(config)
        ConfigureUtil.configure(config, versionStorage)
    }

    void addLogger(Consumer<String> logConsumer) {
        loggers.add logConsumer
    }

    void removeLogger(Consumer<String> logConsumer) {
        loggers.remove logConsumer
    }

    void config(Closure<ChangeNotifier> config) {
        ConfigureUtil.configure(config, this)
    }

    @TaskAction
    protected process() {
        checkStart()
        def command = System.getProperty(ACTION)
        log "command is $command"

        def versionCommand = byName command
        switch (versionCommand) {
            case null:
                unknownCommand(command)
                break

            case HELP:
                help()
                break

            case CHECK:
                checkChanges()
                break

            case NEXTVERISON:
                nextVersion()
                break

            case FORCENEXTVERSION:
                forceNextVersion()
                break

            case TAKE:
                take()
                break

            case HASH:
                hash()
                break

            default:
                unknownCommand(command)
                break
        }
    }

    private log(String data) {
        loggers.each { it.accept(data) }
    }

    private unknownCommand(String command) {
        log("No action found for command $command (project - $project.name)")
        help()
    }

    private void help() {
        log("Possible options are : ")
        for (String message : ActionHelp.HELP_MAP.values()) {
            log('')
            log(message)
        }
    }

    private hash() {
        String hash = calculateHash()
        hashStorage.value = hash
        hashStorage.write()
        log("hash is $hash for project $project.name ")
    }

    private take() {
        versionStorage.read()
        log("version value is " + versionStorage.value + " for project $project.name")
    }

    private forceNextVersion() {
        versionStorage.read()
        if (versionStorage.value == null) {
            log("cannot increment null version value for project $project.name")
        } else if (!VERSION_PATTERN.matcher(versionStorage.value).matches()) {
            log("cannot increment value $versionStorage.value for project $project.name")
        } else {
            log("incrementing value $versionStorage.value for project $project.name")
            def ver = Integer.parseInt(versionStorage.value.split("\\.")[2]) + 1
            versionStorage.value = versionStorage.value.substring(0, versionStorage.value.lastIndexOf(".")) + ".$ver"
            versionStorage.write()
            log("current version is $versionStorage.value for project $project.name")
        }
    }

    private nextVersion() {
        versionStorage.read()
        hashStorage.read()
        if (hashStorage.value == calculateHash()) {
            log("current version is $versionStorage.value for project $project.name")
        } else if (versionStorage.value == null) {
            log("cannot increment null version value for project $project.name")
        } else if (!VERSION_PATTERN.matcher(versionStorage.value).matches()) {
            log("cannot increment value $versionStorage.value for project $project.name")
        } else {
            log("incrementing the version $versionStorage.value for project $project.name")
            def ver = Integer.parseInt(versionStorage.value.split("\\.")[2]) + 1
            versionStorage.value = versionStorage.value.substring(0, versionStorage.value.lastIndexOf(".")) + ".$ver"
            hashStorage.value = calculateHash()
            versionStorage.write()
            hashStorage.write()
            log("current version is $versionStorage.value for project $project.name")
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

    void calcFileHash(File file, MessageDigest digest) {
        Objects.requireNonNull(file)
        if (!file.exists()) {
            log("file $file.absolutePath does not exists")
            return
        }

        def difference = difference(file.toPath(), rootDirectory.toPath())
        for (int i = 0; i < difference.nameCount; i++) {
            digest.update(difference[i].toString().getBytes(StandardCharsets.UTF_8))
        }

        new BufferedInputStream(new FileInputStream(file)).withCloseable {
            def buffer = new byte[1024]
            def read
            while ((read = it.read(buffer)) >= 0) {
                digest.update(buffer, 0, read)
            }
        }
    }

    boolean changed() {
        hashStorage.read()
        return  hashStorage.value != calculateHash()
    }

    private String calcCommonHash(Set<File> files) {
        List<File> list = new ArrayList(files)
        Collections.sort(list, COMPARATOR)

        def digest = MessageDigest.getInstance(MD5)
        list.stream().forEachOrdered {
            calcFileHash(it, digest)
        }

        Base64.encoder.encodeToString(digest.digest())
    }

    private checkChanges() {
        boolean changed = changed()
        log("sourcecode for project $project.name is " + (changed ? "changed" : "not changed"))
    }

    void checkStart() {
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

}





