package com.steammachine.org.gralde.plugins.version.change;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ActionHelpCheck {

    @TestFactory
    Stream<DynamicTest> testNames() {
        return Stream.of(Action.class.getEnumConstants()).
                map(a -> DynamicTest.dynamicTest("Check for " + a.name() + " " +  a.getIdent(),
                () -> assertNotNull(ActionHelp.getHELP_MAP().get(a))));
    }
}