package com.steammachine.org.gralde.plugins.version.change

/**
 * interface for value storage
 *
 * {@link com.steammachine.org.gralde.plugins.version.change.ValueStorage}
 * com.steammachine.org.gralde.plugins.version.change.ValueStorage
 */
interface ValueStorage {

    /**
     * read actual value from storage
     */
    void read()

    /**
     * write current value into storage
     */
    void write()

    /**
     * get read value
     * @return a read value
     */
    String getValue()

    /**
     * set a value into storage
     *
     * @param value a value
     */
    void setValue(String value)

}
