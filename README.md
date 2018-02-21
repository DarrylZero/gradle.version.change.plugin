# gradle.resource.copy.plugin
gradle copy resources plugin   ![Build Status](https://travis-ci.org/DarrylZero/gradle.resource.copy.plugin.svg?branch=development)


                                                                                                                          

To add plugin dependency do the following :
```groovy
buildscript {
    repositories {
        maven { url "https://clojars.org/repo" }
    }

    dependencies {
        classpath 'com.steammachine.org:gradle.copy.plugin:0.9.0'
    }
}
```


To enable plugin add line to your gradle script 
```groovy
apply plugin: 'com.steammachine.org.gradle.copyresources.plugin'
```

When plugin is applied all files in the codebase that contain 'resource' in its path 
are copied into target/classes directory.

```text
Actual built code of latest version can be retreived from

https://clojars.org/com.steammachine.org/gradle.copy.plugin
```

