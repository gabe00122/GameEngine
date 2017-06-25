package gabek.engine.core.tilemap

import gabek.engine.core.physics.RFixture

/**
 * @author Gabriel Keith
 */
class TileReference(val typeId: Int) {
    val fixtures = mutableListOf<RFixture>()
}