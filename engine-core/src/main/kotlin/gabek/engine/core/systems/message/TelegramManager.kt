package gabek.engine.core.systems.message

import com.artemis.EntitySubscription
import gabek.engine.core.systems.PassiveSystem
import gabek.engine.core.util.symbol.SymbolMap

/**
 * @author Gabriel Keith
 * @date 8/9/2017
 */
class TelegramManager: PassiveSystem(){
    private val telegraphResolver = SymbolMap.mapOf<MessageSymbol, ArrayList<EntityTelegraph>>{ ArrayList() }

    fun sendMessage(telegram: Telegram){
        val telegraphs = telegraphResolver[telegram.message]

        for(telegraph in telegraphs){
            if(telegraph.subscription == null){
                telegraph.recipient.onReceive(telegram)
            } else if(telegram.toId == -1){
                val entities = telegraph.subscription.entities
                for(i in 0 until entities.size()){
                    telegram.toId = entities[i]
                    telegraph.recipient.onReceive(telegram)
                }

                telegram.toId = -1
            } else {
                if(telegraph.subscription.activeEntityIds[telegram.toId]){
                    telegraph.recipient.onReceive(telegram)
                }
            }
        }
    }

    fun registerTelegraph(messageSymbol: MessageSymbol, telegraph: Telegraph, subscription: EntitySubscription? = null){
        telegraphResolver[messageSymbol].add(EntityTelegraph(telegraph, subscription))
    }

    private fun dispachMessage(){

    }

    private class EntityTelegraph(
            val recipient: Telegraph,
            val subscription: EntitySubscription?
    )
}