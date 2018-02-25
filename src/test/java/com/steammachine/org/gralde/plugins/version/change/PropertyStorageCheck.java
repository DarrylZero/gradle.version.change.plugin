package com.steammachine.org.gralde.plugins.version.change;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PropertyStorageCheck {
    @Test
    void nameIntegrityChacks() {
        assertEquals("com.steammachine.org.gralde.plugins.version.change.PropertyStorage",
                PropertyStorage.class.getName());
    }
}