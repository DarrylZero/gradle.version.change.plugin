package com.steammachine.org.gralde.plugins.version.change;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ValueStorageCheck {
    @Test
    void checkNameIntegrity() {
        assertEquals("com.steammachine.org.gralde.plugins.version.change.ValueStorage",
                ValueStorage.class.getName());
    }
}