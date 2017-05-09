package gabek.engine.tilemap

import gabek.engine.physics.RFixture

/**
 * @author Gabriel Keith
 */
class TileReference(val typeId: Int) {
    val fixtures = mutableListOf<RFixture>()
}