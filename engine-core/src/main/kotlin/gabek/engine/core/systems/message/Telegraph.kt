package gabek.engine.core.systems.message

/**
 * @author Gabriel Keith
 * @date 8/9/2017
 */
interface Telegraph{
    fun onReceive(telegraph: Telegram)
}