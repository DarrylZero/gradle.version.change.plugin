package com.steammachine.org.gralde.plugins.version.change

/**
 * interface for value storage
 *
 * {@link com.steammachine.org.gralde.plugins.version.change.ValueStorage}
 * com.steammachine.org.gralde.plugins.version.change.ValueStorage
 */
interface ValueStorage {

    /**
     * reads actual value from storage
     */
    void read()

    /**
     * writes current value into storage
     */
    void write()

    /**
     * gets read value
     * @return a read value
     */
    String getValue()

    /**
     * sets a value into storage
     *
     * @param value a value
     */
    void setValue(String value)

}
