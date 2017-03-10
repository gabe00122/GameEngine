package gabek.sm2.leveltemplete

import com.artemis.World
import com.badlogic.gdx.utils.JsonValue
import gabek.sm2.systems.FactoryManager
import gabek.sm2.tilemap.ArrayGrid

/**
 * @author Gabriel Keith
 */
class LevelTemplate {
    val actors = mutableListOf<ActorTemplate>()

    val tileDefinitions = mutableListOf<Pair<String, String>>()
    val tiles: ArrayGrid<TileTemplate>? = null

    init{
        JsonValue("")
    }
}
