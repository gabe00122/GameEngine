package gabek.engine.core.console

import com.github.salomonbrys.kodein.Kodein

/**
 * @author Gabriel Keith
 * @date 4/18/2017
 */

abstract class Command {
    internal lateinit var console: Console
    var active: Boolean = false

    abstract fun setup(kodein: Kodein)

    fun commandSetup(kodein: Kodein, console: Console){
        this.console = console
        try{
            setup(kodein)
            active = true
        } catch (e: Exception){
            active = false
        }
    }

    abstract fun process(args: Array<String>)
    abstract fun help(level: HelpLevel)
}