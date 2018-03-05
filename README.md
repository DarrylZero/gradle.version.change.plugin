This readme file is a draft.


# gradle.resource.copy.plugin
gradle copy resources plugin   ![Build Status](https://travis-ci.org/DarrylZero/gradle.version.change.plugin.svg?branch=release)


                                                                                                                          

To add plugin dependency do the following :
```groovy
buildscript {
    repositories {
        maven { url 'https://clojars.org/repo' }
    }
    dependencies {
        classpath 'com.steammachine.org:gradle.version.change.plugin:1.0.4'
    }
}
```


To enable plugin add line to your gradle script 
```groovy
apply plugin: 'com.steammachine.org.gradle.plugins.version.change'
```

When plugin is applied all files in project src directory are taken into account when calculated files hash. Calculated hash is 
written into file 'publish.properties' into property called 'hash'. Plugin adds a task 'version_manager' into project
the task is not bound by dependencies into any other tasks.
to invoke task actions you must call it in the following way 

```text
gradle version_manager -Daction=nextversion
```
use gradle version_manager -Daction=help to see all possible options

To override default behavior use the folloing syntax :

```groovy
version_manager.config {
  clearFiles() /* clears previouse file set */
  rootDirectory = 'sss' /* sets new root direstory */
  files = 'sss/txt.txt' /* sets new files */
  
  hashStorage {
     file =  project.file '_publish.properties' /* sets new file */
  }

  versionStorage {
     file =  project.file '_publish.properties' /* sets new file */
  }
}
```

If you want to use your own storage do the following

```groovy
import com.steammachine.org.gralde.plugins.version.change.ValueStorage

class CustomStorage implements ValueStorage {

    /**
     * reads actual value from storage
     */
    @Override
    void read() {
      /* your custom code */
    }

    /**
     * writes current value into storage
     */
    void write() {
       /* your custom code */
    }

    /**
     * gets read value
     * @return a read value
     */
    String getValue() {
       /* your custom code */
    }

    /**
     * sets a value into storage
     *
     * @param value a value
     */
    void setValue(String value) {
       /* your custom code */
    }
}

version_manager.config {
  hashStorage(CustomStorage) {
     /* here you set properties of your custom class */
  }

  versionStorage(CustomStorage) {
     /* here you set properties of your custom class */
  }
}
```






The logic is implemented in class 
```groovy
com.steammachine.org.gralde.plugins.version.change.ChangeNotifier
```



```text
Actual built code of latest version can be retreived from
https://clojars.org/com.steammachine.org/gradle.version.change.plugin
```

