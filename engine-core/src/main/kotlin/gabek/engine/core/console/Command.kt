package gabek.engine.core.console

import com.github.salomonbrys.kodein.Kodein

/**
 * @author Gabriel Keith
 * @date 4/18/2017
 */

abstract class Command {
    abstract fun process(args: Array<String>,console: Console, kodein: Kodein)
}