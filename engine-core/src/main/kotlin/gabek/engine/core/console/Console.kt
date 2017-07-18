package gabek.engine.core.console

import com.badlogic.gdx.ApplicationLogger
import com.github.salomonbrys.kodein.Kodein
import gabek.engine.core.console.commands.*

/**
 * @author Gabriel Keith
 * @date 4/18/2017
 */

class Console(val kodein: Kodein): ApplicationLogger {
    var outputProcessor: ConsoleListener? = null
        set(value) {
            field = value
            flush()
        }

    private val commands = HashMap<String, Command>()
    private var isInitialized = false

    private val outputBuffer = StringBuilder()

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
        if(out != null && outputBuffer.isNotEmpty()) {
            out.write(outputBuffer.toString())
            outputBuffer.setLength(0)
        }
    }

    fun processInput(text: String) {
        val split = text.split(" ")

        if(split.isNotEmpty()) {
            val main = split.first()
            val args = Array(split.size-1, {i -> split[i+1]})

            try {
                commands[main]!!.process(args, this, kodein)
            } catch (e: Exception) {
                error("PARSE", "Failed to understand the command.")
            }
            flush()
        }
    }

    fun initializeCommands() {
        if (!isInitialized) {
            isInitialized = true

            addCommand("echo", EchoCommand())

            //addCommand(EchoCommand(this))
            addCommand("ents", EntitieListCommand())
            addCommand("inspect", InspectCommand())
            addCommand("create", CreateCommand())
            addCommand("delete", DeleteCommand())
            addCommand("b2d", Box2dOverlayCommand())
            addCommand("teleport", TeleportCommand())

            addCommand("quit", QuitCommand())
        }
    }

    private fun addCommand(name: String, command: Command) {
        commands.put(name, command)
    }

    interface ConsoleListener {
        fun write(message: String)
    }
}