package com.steammachine.org.gralde.plugins.version.change

/**
 * possible task action descriptions
 */
class ActionHelp {

    static final Map<Action, String> HELP_MAP =
            Collections.unmodifiableMap(new HashMap<Action, String>() {
                {
                    put(Action.CHECK,
                            """
$Action.CHECK.ident - checks if project data is changed
                      """
                    )
                    put(Action.NEXTVERISON, """
$Action.NEXTVERISON.ident - increments version if project data is changed
                      """
                    )
                    put(Action.FORCENEXTVERSION, """
$Action.NEXTVERISON.ident - increments version regarrdless the changes
                      """
                    )
                    put(Action.TAKE, """
$Action.TAKE.ident - takes and shows the written version value 
                      """
                    )
                    put(Action.HASH, """
$Action.HASH.ident - calculates and writes the hash of project data 
                      """
                    )
                }
            })

    public static void main(String[] args) {
        args = null
    }

}
