package com.steammachine.org.gralde.plugins.version.change

/**
 * possible task actions
 */
enum Action {
    CHECK('check'),
    NEXTVERISON('nextversion'),
    FORCENEXTVERSION('forcenextversion'),
    TAKE('take'),
    HASH('hash')

    static final Map<String, Action> NAMES = names()

    static Map<String, Action> names() {
        def value = [:]
        for (Action a : Action.enumConstants) {
            value.put(a.ident, a)
        }
        Collections.unmodifiableMap(value)
    }

    final String ident

    Action(String ident) {
        this.ident = Objects.requireNonNull(ident)
    }

    String getIdent() {
        return ident
    }

    static Action byName(String name) {
        NAMES[name]
    }
}
