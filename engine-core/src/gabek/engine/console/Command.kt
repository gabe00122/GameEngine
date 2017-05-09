package gabek.engine.console

import com.github.salomonbrys.kodein.Kodein

/**
 * @author Gabriel Keith
 * @date 4/18/2017
 */

abstract class Command(val console: Console, val name: String) {
    val kodein: Kodein = console.kodein

    abstract fun command(arguments: String)
}