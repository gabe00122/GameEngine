package gabek.engine.console

import com.badlogic.gdx.ApplicationLogger
import com.github.salomonbrys.kodein.Kodein
import gabek.engine.console.commands.*

/**
 * @author Gabriel Keith
 * @date 4/18/2017
 */

class Console(val kodein: Kodein) : ApplicationLogger {
    var outputProcessor: ConsoleListener? = null
        set(value) {
            field = value
            flush()
        }

    private val commands = HashMap<String, Command>()
    private var isInitialized = false

    private var outputBuffer = StringBuilder()

    override fun error(tag: String, message: String) {
        handleOutput(tag, message)
    }

    override fun error(tag: String, message: String, exception: Throwable) {

    }

    override fun log(tag: String, message: String) {
        handleOutput(tag, message)
    }

    override fun log(tag: String, message: String, exception: Throwable) {

    }

    override fun debug(tag: String, message: String) {
        handleOutput(tag, message)
    }

    override fun debug(tag: String, message: String, exception: Throwable) {

    }

    fun handleOutput(tag: String, message: String) {
        writeln("$tag: $message")
    }

    fun write(message: String) {
        outputBuffer.append(message)
    }

    fun writeln(message: String){
        outputBuffer.append("$message\n")
    }

    private fun flush(){
        val out = outputProcessor
        if(out != null) {
            out.write(outputBuffer.toString())
            outputBuffer = StringBuilder()
        }
    }

    fun processInput(text: String) {
        val list = text.split(' ', limit = 2)
        if (list.size == 1) {
            writeln(text)
            commands[list[0]]?.command("")
            flush()
        } else if (list.size == 2) {
            writeln(text)
            commands[list[0]]?.command(list[1])
            flush()
        }
    }

    fun initializeCommands() {
        if (!isInitialized) {
            isInitialized = true

            addCommand(EchoCommand(this))
            addCommand(EntitieListCommand(this))
            addCommand(InspectCommand(this))
            addCommand(CreateCommand(this))
            addCommand(DeleteCommand(this))

            addCommand(Box2dOverlayCommand(this))
        }
    }

    private fun addCommand(command: Command) {
        commands.put(command.name, command)
    }

    interface ConsoleListener {
        fun write(message: String)
    }
}