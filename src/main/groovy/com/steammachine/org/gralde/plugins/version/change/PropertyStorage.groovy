package com.steammachine.org.gralde.plugins.version.change

import com.steammachine.common.utils.commonutils.CommonUtils
import com.steammachine.commonutils.orderedproperties.OrderedProperties

/**
 * implemantation of ValueStorage for property storage.
 *
 * {@link com.steammachine.org.gralde.plugins.version.change.PropertyStorage}
 * com.steammachine.org.gralde.plugins.version.change.PropertyStorage
 */
class PropertyStorage implements ValueStorage {

    private File file
    private String value
    private String propertyName

    File getFile() {
        return file
    }

    void setFile(File file) {
        this.file = file
    }

    String getPropertyName() {
        return propertyName
    }

    void setPropertyName(String propertyName) {
        this.propertyName = propertyName
    }

    @Override
    void read() {
        if (!file) {
            throw new IllegalStateException("file is not defined")
        }
        if (!propertyName) {
            throw new IllegalStateException("propertyName is not defined")
        }

        new FileInputStream(file).withCloseable {
            def properties = new OrderedProperties()
            properties.load(it)
            value = properties.getProperty(propertyName)
        }
    }

    @Override
    void write() {
        if (!file) {
            throw new IllegalStateException("file is not defined")
        }
        if (!propertyName) {
            throw new IllegalStateException("propertyName is not defined")
        }

        def properties = new OrderedProperties()
        new FileInputStream(file).withCloseable {
            properties.load(it)
        }

        properties.setProperty(propertyName, value)

        new FileOutputStream(file).withCloseable {
            properties.store(it)
        }
    }

    @Override
    String getValue() {
        return value
    }

    @Override
    void setValue(String value) {
        this.value = value
    }
}
