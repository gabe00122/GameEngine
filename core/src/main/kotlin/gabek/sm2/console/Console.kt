package gabek.sm2.console

import com.badlogic.gdx.ApplicationLogger
import com.github.salomonbrys.kodein.Kodein

/**
 * @author Gabriel Keith
 * @date 4/18/2017
 */

class Console(val kodein: Kodein): ApplicationLogger {
    var outputProcessor: ConsoleListener? = null
        set(value) {
            field = value
            backlog.forEach { value?.write(it) }
            backlog.clear()
        }
    private val backlog = ArrayList<String>()

    private val commands = HashMap<String, Command>()
    private var isInitialized = false

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

    fun handleOutput(tag: String, message: String){
        write("$tag: $message")
    }

    fun write(message: String){
        val listener = outputProcessor
        if(listener != null){
            listener.write(message)
        } else {
            backlog.add(message)
        }
    }

    fun processInput(text: String){
        val list = text.split(' ', limit = 2)
        if(list.size == 1){
            write(text)
            commands[list[0]]?.command("")
        } else if(list.size == 2){
            write(text)
            commands[list[0]]?.command(list[1])
        }
    }

    fun initializeCommands(){
        if(!isInitialized){
            isInitialized = true

            addCommand(EchoCommand(this))
            addCommand(EntitieListCommand(this))
            addCommand(InspectCommand(this))
            addCommand(CreateCommand(this))
            addCommand(DeleteCommand(this))
        }
    }

    private fun addCommand(command: Command){
        commands.put(command.name, command)
    }

    interface ConsoleListener{
        fun write(message: String)
    }
}