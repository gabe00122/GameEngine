package gabek.engine.core.systems.message

/**
 * @author Gabriel Keith
 * @date 8/9/2017
 */
data class Telegram(
    val message: MessageSymbol,
    var toId: Int = -1,
    val fromId: Int = -1,
    val fromTelegraph: Telegraph? = null
)