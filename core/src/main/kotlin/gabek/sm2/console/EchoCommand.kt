package gabek.sm2.console

import com.github.salomonbrys.kodein.Kodein

/**
 * @author Gabriel Keith
 * @date 4/18/2017
 */

class EchoCommand(console: Console): Command(console, "echo"){


    override fun command(arguments: String) {
        console.write(arguments)
    }
}