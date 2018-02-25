package com.steammachine.org.gralde.plugins.version.change;

import com.steammachine.common.utils.enumerations.EnumComparisonUtils;
import org.junit.jupiter.api.Test;

import java.util.Objects;

class ActionCheck {


    enum ActionTemplate {
        CHECK("check"),
        NEXTVERISON("nextversion"),
        FORCENEXTVERSION("forcenextverison"),
        TAKE("take"),
        HASH("hash");

        final String ident;

        ActionTemplate(String ident) {
            this.ident = Objects.requireNonNull(ident);
        }
    }


    @Test
    void testNameIntegrity() {
        EnumComparisonUtils.checkIfEnumsEqual(ChangeNotifier.Action.class, ActionTemplate.class);
        EnumComparisonUtils.deepCheckIfEnumsAreEqual(ChangeNotifier.Action.class, ActionTemplate.class,
                (action, actionTemplate) -> Objects.equals(action.getIdent(), actionTemplate.ident));
    }

}