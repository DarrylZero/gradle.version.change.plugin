package com.steammachine.org.gralde.plugins.version.change

interface ValueStorage {

    void read()
    void write()
    String getValue()
    void setValue(String value)

}
